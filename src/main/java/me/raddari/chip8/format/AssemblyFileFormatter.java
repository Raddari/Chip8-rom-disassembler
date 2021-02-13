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
import java.util.Map;
import java.util.StringJoiner;

public final class AssemblyFileFormatter implements Formatter<Program> {

    private static final Logger LOGGER = LogManager.getLogger();
    private Configuration config;

    private AssemblyFileFormatter(@NotNull Configuration config) {
        this.config = config;
    }

    public static @NotNull AssemblyFileFormatter withConfig(@NotNull Configuration config) {
        return new AssemblyFileFormatter(config);
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
        var labelMap = program.getLabels();

        var lineCounter = 0x200;
        for (var line : program.opcodes()) {
            var innerBuilder = new StringBuilder();
            if (config.getBoolean("formatter.use_labels")) {
                formatLabel(labelMap, lineCounter, builder);
            }
            if (config.getBoolean("formatter.opcode.prepend")) {
                formatOpcodeBytes(line, innerBuilder);
            }
            formatInstruction(line, innerBuilder);
            formatArguments(labelMap, line, innerBuilder);

            innerBuilder.append("\n");
            builder.append(innerBuilder);
            lineCounter += 2;
        }

        return builder.toString();
    }

    private void formatLabel(Map<Integer, String> labelMap, int lineNumber, StringBuilder builder) {
        if (labelMap.containsKey(lineNumber)) {
            var indent = " ".repeat(config.getInt("formatter.label.indent"));
            var labelName = labelMap.get(lineNumber);
            labelName = config.getBoolean("formatter.label.uppercase") ? labelName.toUpperCase() : labelName;

            builder.append(indent);
            builder.append(config.getString("formatter.label.prefix"));
            builder.append(labelName);
            builder.append(config.getString("formatter.label.suffix"));
            builder.append("\n");
        }
    }

    private void formatOpcodeBytes(Opcode line, StringBuilder builder) {
        var indent = " ".repeat(config.getInt("formatter.instruction.indent"));
        var bytes = line.getOpcodeBytes();
        bytes = config.getBoolean("formatter.uppercase_hex") ? bytes.toUpperCase() : bytes.toLowerCase();

        builder.append(indent);
        builder.append(config.getString("formatter.opcode.prefix"));
        builder.append(bytes);
        builder.append(config.getString("formatter.opcode.suffix"));
    }

    private void formatInstruction(Opcode line, StringBuilder builder) {
        var indent = " ".repeat(config.getInt("formatter.instruction.indent"));
        if (!config.getBoolean("formatter.opcode.prepend")) {
            builder.append(indent);
        }

        builder.append(line.getKind().getSymbol());
        if (!line.getArgs().isEmpty()) {
            builder.append(" ");
        }
    }

    private void formatArguments(Map<Integer, String> labelMap, Opcode line, StringBuilder builder) {
        var argJoiner = new StringJoiner(config.getString("formatter.argument.separator"));

        for (var arg : line.getArgs()) {
            var configCategory = arg.type().configCategory();
            var upperHex = config.getBoolean("formatter.uppercase_hex");

            var prefix = config.getString(String.format("formatter.%s.prefix", configCategory));
            var suffix = config.getString(String.format("formatter.%s.suffix", configCategory));
            var useHex = config.getBoolean(String.format("formatter.%s.use_hex", configCategory));

            var value = useHex ? Integer.toHexString(arg.value()) : String.valueOf(arg.value());
            value = upperHex ? value.toUpperCase() : value.toLowerCase();

            if (line.hasJump() && config.getBoolean("formatter.use_labels")) {
                value = labelMap.get(arg.value());
                value = config.getBoolean("formatter.label.uppercase") ? value.toUpperCase() : value;
                prefix = "";
                suffix = "";
            }

            var argument = String.format("%s%s%s", prefix, value, suffix);
            argJoiner.add(argument);
        }

        builder.append(argJoiner.toString());
    }

}
