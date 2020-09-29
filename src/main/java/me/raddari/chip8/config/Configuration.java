package me.raddari.chip8.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Configuration {

    @Nullable Object get(@NotNull String path);

    @Nullable Object get(@NotNull String path, @Nullable Object def);

    void set(@NotNull String path, @Nullable Object value);

    boolean isString(@NotNull String path);

    @Nullable String getString(@NotNull String path);

    @Nullable String getString(@NotNull String path, @Nullable String def);

    boolean isBoolean(@NotNull String path);

    boolean getBoolean(@NotNull String path);

    boolean getBoolean(@NotNull String path, boolean def);

    boolean isInt(@NotNull String path);

    int getInt(@NotNull String path);

    int getInt(@NotNull String path, int def);

    boolean isLong(@NotNull String path);

    long getLong(@NotNull String path);

    long getLong(@NotNull String path, long def);

    boolean isDouble(@NotNull String path);

    double getDouble(@NotNull String path);

    double getDouble(@NotNull String path, double def);

    boolean isList(@NotNull String path);

    /**
     * Gets the configuration path as a list. If it does not exist as a
     * list, return an empty list.
     *
     * @param path value path
     * @return list of values at the given config path
     */
    @SuppressWarnings("java:S1452") // Wildcard return types
    @NotNull List<?> getList(@NotNull String path);

    @SuppressWarnings("java:S1452") // Wildcard return types
    @NotNull List<?> getList(@NotNull String path, @NotNull List<?> def);

}
