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
    private Configuration formatConfig;

    public AssemblyFileFormatter(@NotNull Configuration formatConfig) {
        this.formatConfig = formatConfig;
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
            writer.append("\n");
            LOGGER.info("Wrote program to {}", file.getName());
        } catch (IOException e) {
            LOGGER.error("IOException occured writing to file", e);
        }
    }

    @Override
    public @NotNull Formatter<Program> useConfiguration(@NotNull Configuration config) {
        this.formatConfig = config;
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
        if (formatConfig.getBoolean("opcode.prepend")) {
            builder.append(formatConfig.getString("opcode.prefix"));
            var bytes = line.getOpcodeBytes();
            bytes = formatConfig.getBoolean("uppercase_hex") ? bytes.toUpperCase() : bytes.toLowerCase();
            builder.append(bytes);
            builder.append(formatConfig.getString("opcode.suffix"));
        }
    }

    private void formatInstruction(Opcode line, StringBuilder builder) {
        builder.append(line.getKind().getSymbol());
        if (!line.getArgs().isEmpty()) {
            builder.append(" ");
        }
    }

    private void formatArguments(Opcode line, StringBuilder builder) {
        var argJoiner = new StringJoiner(formatConfig.getString("argument.separator"));

        for (var arg : line.getArgs()) {
            var configCategory = arg.getType().configCategory();
            var upperHex = formatConfig.getBoolean("uppercase_hex");

            var prefix = formatConfig.getString(String.format("%s.prefix", configCategory));
            var suffix = formatConfig.getString(String.format("%s.suffix", configCategory));
            var useHex = formatConfig.getBoolean(String.format("%s.use_hex", configCategory));

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
