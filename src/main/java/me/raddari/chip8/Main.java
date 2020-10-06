package me.raddari.chip8;

import me.raddari.chip8.config.JsonConfiguration;
import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.format.AssemblyFileFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }

        try {
            var configStream = Main.class.getResourceAsStream("/assets/format/default.json");
            var configReader = new BufferedReader(new InputStreamReader(configStream));
            var config = new JsonConfiguration().load(configReader);
            var formatter = new AssemblyFileFormatter(config);
            var romFile = new File(args[0]);

            if (romFile.isFile()) {
                var program = Disassembler.create().disassemble(romFile);
                var outFile = new File(romFile.getName() + ".asm");
                formatter.formatToFile(program, outFile);

            } else {
                LOGGER.fatal("Argument {} must point to a rom file", romFile.getAbsolutePath());
                printHelp();
            }

        } catch (IOException e) {
            LOGGER.error("IOException occured disassembling file", e);
        }


    }

    private static void printHelp() {
        System.out.println("Usage: Chip8-rom-disassembler <rom-path>");
    }

}
