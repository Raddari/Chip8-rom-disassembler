package me.raddari.chip8;

import me.raddari.chip8.config.Configuration;
import me.raddari.chip8.config.JsonConfiguration;
import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.format.AssemblyFileFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }

        try {
            var config = getCustomConfiguration();
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

    private static Configuration getCustomConfiguration() throws IOException {
        var currentDir = new File(".");
        var files = currentDir.listFiles((dir, name) -> name.equalsIgnoreCase("config.json"));

        Reader configReader;
        if (files.length > 0) {
            LOGGER.info("Custom config file found in current directory");
            configReader = new BufferedReader(new FileReader(files[0]));
        } else {
            LOGGER.warn("No custom config file found in current directory, using default config");
            LOGGER.warn(
                    "If you would like to use a custom config, copy the default from assets/config/default.json"
                            + " into the directory containing this jar file, and rename it to config.json");

            var configStream = Main.class.getResourceAsStream("/assets/config/default.json");
            configReader = new BufferedReader(new InputStreamReader(configStream));
        }
        return new JsonConfiguration().load(configReader);
    }

    private static void printHelp() {
        System.out.println("Usage: Chip8-rom-disassembler <rom-path>");
    }

}
