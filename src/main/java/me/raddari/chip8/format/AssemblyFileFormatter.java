package me.raddari.chip8.format;

import me.raddari.chip8.config.Configuration;
import me.raddari.chip8.disassembler.Opcode;
import me.raddari.chip8.disassembler.Program;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public final class AssemblyFileFormatter implements Formatter<Program> {

    private static final Logger LOGGER = LogManager.getLogger();
    private Configuration config;

    public AssemblyFileFormatter(@NotNull Configuration config) {
        this.config = config;
    }

    public void formatToFile(@NotNull Program program, @NotNull File file) {
        try {
            if (!file.createNewFile()) {
                LOGGER.warn("File {} already exists, will be overwritten!", file.getName());
            }
        } catch (IOException e) {
            LOGGER.error("IOException occured creating file", e);
            return;
        }

        try (var writer = new FileWriter(file)) {
            writer.append(formatToString(program));
            LOGGER.info("Wrote program to {}", file.getName());
        } catch (IOException e) {
            LOGGER.error("IOException occured writing to file", e);
        }
    }

    @Override
    public @NotNull Formatter<Program> useConfiguration(@NotNull Configuration config) {
        this.config = config;
        return this;
    }

    @Override
    public @NotNull String formatToString(@NotNull Program program) {
        var builder = new StringBuilder();
        builder.append("; ").append(program.name()).append("\n\n");

        for (var line : program.opcodes()) {
            formatOpcodeBytes(line, builder);
            formatInstruction(line, builder);
            formatArguments(line, builder);
            builder.append("\n");
        }

        return builder.toString();
    }

    private void formatOpcodeBytes(Opcode line, StringBuilder builder) {
        if (config.getBoolean("formatter.opcode.prepend")) {
            builder.append(config.getString("formatter.opcode.prefix"));
            var bytes = line.getOpcodeBytes();
            bytes = config.getBoolean("formatter.uppercase_hex") ? bytes.toUpperCase() : bytes.toLowerCase();
            builder.append(bytes);
            builder.append(config.getString("formatter.opcode.suffix"));
        }
    }

    private void formatInstruction(Opcode line, StringBuilder builder) {
        builder.append(line.getKind().getSymbol());
        if (!line.getArgs().isEmpty()) {
            builder.append(" ");
        }
    }

    private void formatArguments(Opcode line, StringBuilder builder) {
        var argJoiner = new StringJoiner(config.getString("formatter.argument.separator"));

        for (var arg : line.getArgs()) {
            var configCategory = arg.getType().configCategory();
            var upperHex = config.getBoolean("formatter.uppercase_hex");

            var prefix = config.getString(String.format("formatter.%s.prefix", configCategory));
            var suffix = config.getString(String.format("formatter.%s.suffix", configCategory));
            var useHex = config.getBoolean(String.format("formatter.%s.use_hex", configCategory));

            var value = useHex ? Integer.toHexString(arg.getValue()) : String.valueOf(arg.getValue());
            if (useHex) {
                value = upperHex ? value.toUpperCase() : value.toLowerCase();
            }

            var argument = String.format("%s%s%s", prefix, value, suffix);
            argJoiner.add(argument);
        }

        builder.append(argJoiner.toString());
    }

}
