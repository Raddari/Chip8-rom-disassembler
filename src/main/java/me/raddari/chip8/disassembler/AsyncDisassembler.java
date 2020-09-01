package me.raddari.chip8.disassembler;

import com.google.common.base.Stopwatch;
import me.raddari.chip8.instruction.Argument;
import me.raddari.chip8.instruction.Instruction;
import me.raddari.chip8.util.Numbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class AsyncDisassembler implements Disassembler {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public @NotNull List<Instruction> disassemble(@NotNull File romFile) {
        if (!romFile.isFile()) {
            throw new IllegalArgumentException("romFile must point to a valid file");
        }
        var instructions = new ArrayList<Instruction>();
        var ioThread = new Thread(() -> readData(instructions, romFile), "IOThread");

        LOGGER.info("Reading {}", romFile.getName());
        var stopwatch = Stopwatch.createStarted();
        ioThread.start();

        while (ioThread.isAlive()) {
            try {
                ioThread.join(1000);
                LOGGER.info("Still reading...");
            } catch (InterruptedException e) {
                LOGGER.warn("Main thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        stopwatch.stop();
        var elapsed = stopwatch.elapsed();

        LOGGER.info("Read {} opcodes in {} millis", instructions.size(), elapsed.toMillis());

        return instructions;
    }

    private static void readData(@NotNull List<Instruction> dest, @NotNull File romFile) {
        try (var stream = new DataInputStream(new FileInputStream(romFile))) {
            var raw = new byte[2];
            while (stream.read(raw, 0, 2) > 0) {
                var bytes = Numbers.bytesToHex(raw);
                dest.add(interpretBytes(bytes, raw));
            }
        } catch (IOException e) {
            LOGGER.error("IOException occured reading file", e);
        }
    }

    private static Instruction interpretBytes(String bytes, byte[] raw) {
        var opcode = Opcode.find(bytes);
        var args = new ArrayList<Argument>();

        for (var argType : opcode.opcodeArgTypes()) {
            var value = switch (argType) {
                case ADDRESS -> (((int) raw[0]) & 0x0F) + (((int) raw[1]) & 0xFF);
                case CONSTANT -> (((int) raw[1]) & 0xFF);
                case REGX -> (((int) raw[0]) & 0x0F);
                case REGY -> (((int) raw[1]) & 0xF0);
            };

            args.add(Argument.of(argType, value));
        }

        return new Instruction(opcode, args);
    }

}
