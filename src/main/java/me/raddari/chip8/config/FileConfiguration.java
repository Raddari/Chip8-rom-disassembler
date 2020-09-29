package me.raddari.chip8.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class FileConfiguration implements Configuration {

    private static final Logger LOGGER = LogManager.getLogger();
    private final String pathSeparator;
    protected final Map<String, Object> pathMap;

    protected FileConfiguration(@NotNull String pathSeparator) {
        this.pathSeparator = pathSeparator;
        pathMap = new LinkedHashMap<>();
    }

    public abstract void loadFromString(@NotNull String contents);

    public void load(@NotNull Reader reader) throws IOException {
        var br = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        var builder = new StringBuilder();

        for (var line = br.readLine(); line != null; line = br.readLine()) {
            builder.append(line);
            builder.append("\n");
        }
        loadFromString(builder.toString());
    }

    public void load(@NotNull String filePath) throws IOException {
        load(new File(filePath));
    }

    public void load(@NotNull File file) throws IOException {
        LOGGER.debug("Reading config file {}", file.getName());
        var stream = new FileInputStream(file);
        load(new InputStreamReader(stream, Charset.defaultCharset()));
    }

    public String pathSeparator() {
        return pathSeparator;
    }

}
