package me.raddari.chip8;

import me.raddari.chip8.config.JsonConfiguration;
import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.format.AssemblyFileFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }

        var formatConfigFile = new File(
                Main.class.getClassLoader().getResource("assets/format/default.json").getPath());
        try {
            var config = new JsonConfiguration().load(formatConfigFile);
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
