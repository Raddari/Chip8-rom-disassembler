package me.raddari.chip8.disassembler;

import com.google.common.base.Stopwatch;
import me.raddari.chip8.util.Numbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class AsyncDisassembler implements Disassembler {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public @NotNull List<Opcode> disassemble(@NotNull File romFile) {
        if (!romFile.isFile()) {
            LOGGER.error("romFile {} must point to a valid file", romFile.getPath());
            return Collections.emptyList();
        }
        var opcodes = new ArrayList<Opcode>();
        var ioThread = new Thread(() -> readData(opcodes, romFile), "IOThread");
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
        LOGGER.info("Read {} opcodes in {} millis", opcodes.size(), elapsed.toMillis());
        return opcodes;
    }

    private static void readData(List<? super Opcode> dest, File romFile) {
        try (var reader = new DataInputStream(new FileInputStream(romFile))) {
            var bytes = new byte[2];
            while (reader.read(bytes, 0, 2) > 0) {
                var opcode = bytesToOpcode(bytes);
                dest.add(opcode);
            }
        } catch (IOException e) {
            LOGGER.error("IOException occured reading file", e);
        }
    }

    private static Opcode bytesToOpcode(byte[] bytes) {
        var hexStr = Numbers.bytesToHex(bytes);
        LOGGER.debug("Read bytes {}", hexStr);
        var combined = Integer.parseInt(hexStr, 16);

        var kind = Opcode.Kind.parseString(hexStr);
        var args = parseArguments(kind, combined, hexStr);

        return Opcode.create(kind, args);
    }

    private static List<Argument> parseArguments(Opcode.Kind kind, int combinedBytes, String hexStr) {
        var args = new ArrayList<Argument>();
        var argTypes = kind.getArgTypes();
        var values = OpcodeValues.generateFrom(combinedBytes);

        for (var argType : argTypes) {
            args.add(createArgument(argType, values));
        }

        LOGGER.debug("Registered {} args for opcode {} ({})", args.size(), hexStr, kind.getSymbol());
        for (var arg : args) {
            LOGGER.debug("{}: {}", arg.getType(), arg.getValue());
        }

        return args;
    }

    private static Argument createArgument(Argument.Type argType, OpcodeValues opcodeValues) {
        return switch (argType) {
            case ADDRESS -> Argument.of(argType, opcodeValues.address);
            case CONSTANT_4 -> Argument.of(argType, opcodeValues.constant4);
            case CONSTANT_8 -> Argument.of(argType, opcodeValues.constant8);
            case REGISTER_X -> Argument.of(argType, opcodeValues.regX);
            case REGISTER_Y -> Argument.of(argType, opcodeValues.regY);
        };
    }

    private static final class OpcodeValues {

        final int address;
        final int constant4;
        final int constant8;
        final int regX;
        final int regY;

        OpcodeValues(int address, int constant4, int constant8, int regX, int regY) {
            this.address = address;
            this.constant4 = constant4;
            this.constant8 = constant8;
            this.regX = regX;
            this.regY = regY;
        }

        static @NotNull OpcodeValues generateFrom(int opcodeBytes) {
            var address = opcodeBytes & 0x0FFF;
            var constant4 = opcodeBytes & 0x000F;
            var constant8 = opcodeBytes & 0x00FF;
            var regX = opcodeBytes & 0x0F00;
            var regY = opcodeBytes & 0x00F0;
            return new OpcodeValues(address, constant4, constant8, regX, regY);
        }

    }

}
