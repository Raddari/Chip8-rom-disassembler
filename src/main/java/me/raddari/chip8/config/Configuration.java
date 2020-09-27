package me.raddari.chip8.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Configuration {

    boolean isString(@NotNull String path);

    @Nullable String getString(@NotNull String path);

    @Nullable String getString(@NotNull String path, @NotNull String def);

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

    <T> @Nullable List<T> getList(@NotNull String path);

}
