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
import java.util.List;

final class AsyncDisassembler implements Disassembler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int ADDRESS_MASK = 0x0FFF;
    private static final int CONSTANT_8_MASK = 0x00FF;
    private static final int CONSTANT_4_MASK = 0x000F;
    private static final int REG_X_MASK = 0x0F00;
    private static final int REG_Y_MASK = 0x00F0;

    @Override
    public @NotNull Program disassemble(@NotNull File romFile) {
        if (!romFile.isFile()) {
            LOGGER.error("Rom file {} must point to a valid file", romFile.getAbsolutePath());
            throw new IllegalArgumentException("Invalid rom file");
        }
        final var name = romFile.getName();
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
        return new Program(name, opcodes);
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
        LOGGER.debug("Reading bytes {}", hexStr);
        var combined = Integer.parseInt(hexStr, 16);

        var kind = Opcode.Kind.parseString(hexStr);
        var args = parseArguments(kind, combined, hexStr);

        return Opcode.create(hexStr, kind, args);
    }

    private static List<Argument> parseArguments(Opcode.Kind kind, int combinedBytes, String hexStr) {
        var args = new ArrayList<Argument>();
        var argTypes = kind.getArgTypes();

        for (var argType : argTypes) {
            args.add(createArgument(argType, combinedBytes));
        }

        LOGGER.debug("Registered {} args for opcode {} ({})", args.size(), hexStr, kind.getSymbol());
        if (LOGGER.isDebugEnabled()) {
            for (var arg : args) {
                LOGGER.debug("{}: 0x{}", arg.getType(), Integer.toHexString(arg.getValue()));
            }
        }

        return args;
    }

    private static Argument createArgument(Argument.Type argType, int combinedBytes) {
        return switch (argType) {
            case ADDRESS -> Argument.of(argType, combinedBytes & ADDRESS_MASK);
            case CONSTANT_4 -> Argument.of(argType, combinedBytes & CONSTANT_4_MASK);
            case CONSTANT_8 -> Argument.of(argType, combinedBytes & CONSTANT_8_MASK);
            case REGISTER_X -> Argument.of(argType, (combinedBytes & REG_X_MASK) >> 8);
            case REGISTER_Y -> Argument.of(argType, (combinedBytes & REG_Y_MASK) >> 4);
        };
    }

}
