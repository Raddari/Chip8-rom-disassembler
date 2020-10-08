package me.raddari.chip8.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Defines the common methods for a configuration which is readable by this program. A configuration is very similar
 * to a {@link java.util.Map}, to the point where most implementations can use a {@code Map} as the underlying data
 * structure.
 * <p>All methods in a configuration must not accept or return {@code null}, EXCEPT FOR
 * <ul>
 *   <li>
 *     {@link #get(String)} and its overloads, which MAY return {@code null} or receive {@code null} as its
 *     default parameter.
 *   </li>
 *   <li>
 *     {@link #set(String, Object)} which MAY receive {@code null} as its object parameter, to remove a config option.
 *   </li>
 * </ul>
 */
public interface Configuration {

    /**
     * Gets the value associated to the given path.
     *
     * @param path config value path
     * @return {@code Object} associated with the given path, or {@code null} if it does not exist.
     */
    @Nullable Object get(@NotNull String path);

    @Nullable Object get(@NotNull String path, @Nullable Object def);

    void set(@NotNull String path, @Nullable Object value);

    boolean isString(@NotNull String path);

    @NotNull String getString(@NotNull String path);

    @NotNull String getString(@NotNull String path, @NotNull String def);

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

}
