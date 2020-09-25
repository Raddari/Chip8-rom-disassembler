package me.raddari.chip8;

import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.disassembler.Opcode;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        var options = new Options();

        Option[] optionsArr = {
                Option.builder("r")
                        .longOpt("rom")
                        .hasArg()
                        .argName("rom-path")
                        .desc("path to rom binary file")
                        .build(),

                Option.builder("h")
                        .longOpt("help")
                        .desc("show this message")
                        .hasArg(false)
                        .build()
        };

        for (var option : optionsArr) {
            options.addOption(option);
        }

        var formatter = new HelpFormatter();
        var parser = new DefaultParser();

        try {
            var cmd = parser.parse(options, args);
            if (cmd.hasOption("r")) {
                var romFile = new File(cmd.getOptionValue("r", ""));

                Disassembler.create()
                        .disassemble(romFile)
                        .stream()
                        .map(Opcode::toAssemblyString)
                        .forEach(System.out::println);
            } else {
                printHelp(formatter, options);
            }
            if (cmd.hasOption("h")) {
                printHelp(formatter, options);
            }

        } catch (ParseException e) {
            LOGGER.fatal("ParseException occured reading arguments", e);
        }

    }

    private static void printHelp(HelpFormatter formatter, Options options) {
        formatter.printHelp("Chip8-rom-disassembler", options);
    }

}
