package me.raddari.chip8.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class FileConfiguration implements Configuration {

    private static final Logger LOGGER = LogManager.getLogger();
    protected final char pathSeparator;
    protected final Map<String, Object> pathMap;

    protected FileConfiguration(char pathSeparator) {
        this.pathSeparator = pathSeparator;
        pathMap = new LinkedHashMap<>();
    }

    public abstract FileConfiguration loadFromString(@NotNull String contents);

    public FileConfiguration load(@NotNull Reader reader) throws IOException {
        var br = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        var builder = new StringBuilder();

        for (var line = br.readLine(); line != null; line = br.readLine()) {
            builder.append(line);
            builder.append("\n");
        }
        return loadFromString(builder.toString());
    }

    public FileConfiguration load(@NotNull String filePath) throws IOException {
        return load(new File(filePath));
    }

    public FileConfiguration load(@NotNull File file) throws IOException {
        LOGGER.debug("Reading config file {}", file.getName());
        var stream = new FileInputStream(file);
        return load(new InputStreamReader(stream, Charset.defaultCharset()));
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return get(path, null);
    }

    @Override
    public @Nullable Object get(@NotNull String path, @Nullable Object def) {
        return pathMap.getOrDefault(path, def);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (get(path) != null && value == null) {
            pathMap.remove(path);
        }
        pathMap.put(path, value);
    }

    @Override
    public boolean isString(@NotNull String path) {
        return get(path) instanceof String;
    }

    @Override
    public @NotNull String getString(@NotNull String path) {
        return getString(path, "");
    }

    @Override
    public @NotNull String getString(@NotNull String path, @NotNull String def) {
        var value = get(path);
        return value instanceof String ? (String) value : def;
    }

    @Override
    public boolean isBoolean(@NotNull String path) {
        return get(path) instanceof Boolean;
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return getBoolean(path, false);
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        var value = get(path);
        return value instanceof Boolean ? (Boolean) value : def;
    }

    @Override
    public boolean isInt(@NotNull String path) {
        return get(path) instanceof Integer;
    }

    @Override
    public int getInt(@NotNull String path) {
        return getInt(path, 0);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        var value = get(path);
        return value instanceof Number ? ((Number) value).intValue() : def;
    }

    @Override
    public boolean isLong(@NotNull String path) {
        return get(path) instanceof Long;
    }

    @Override
    public long getLong(@NotNull String path) {
        return getLong(path, 0L);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        var value = get(path);
        return value instanceof Number ? ((Number) value).longValue() : def;
    }

    @Override
    public boolean isDouble(@NotNull String path) {
        return get(path) instanceof Double;
    }

    @Override
    public double getDouble(@NotNull String path) {
        return getDouble(path, 0.0D);
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        var value = get(path);
        return value instanceof Number ? ((Number) value).doubleValue() : def;
    }

}
