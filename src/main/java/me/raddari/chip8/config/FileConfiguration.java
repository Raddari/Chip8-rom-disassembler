package me.raddari.chip8.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
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

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable Object get(@NotNull String path, @Nullable Object def) {
        return null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {

    }

    @Override
    public boolean isString(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable String getString(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable String getString(@NotNull String path, @NotNull String def) {
        return null;
    }

    @Override
    public boolean isBoolean(@NotNull String path) {
        return false;
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return false;
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        return false;
    }

    @Override
    public boolean isInt(@NotNull String path) {
        return false;
    }

    @Override
    public int getInt(@NotNull String path) {
        return 0;
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        return 0;
    }

    @Override
    public boolean isLong(@NotNull String path) {
        return false;
    }

    @Override
    public long getLong(@NotNull String path) {
        return 0;
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        return 0;
    }

    @Override
    public boolean isDouble(@NotNull String path) {
        return false;
    }

    @Override
    public double getDouble(@NotNull String path) {
        return 0;
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        return 0;
    }

    @Override
    public boolean isList(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable <T> List<T> getList(@NotNull String path) {
        return null;
    }

    public String pathSeparator() {
        return pathSeparator;
    }

}
