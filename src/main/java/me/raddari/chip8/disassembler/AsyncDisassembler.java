package me.raddari.chip8.disassembler;

import com.google.common.base.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

    }

}
