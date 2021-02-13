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
 *     default argument.
 *   </li>
 *   <li>
 *     {@link #set(String, Object)} which MAY receive {@code null} as its object argument, to remove a config option.
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

    /**
     * Gets the value associated to the given path, or a default value
     *
     * @param path config value path
     * @return {@code Object} associated with the given path, or {@code def} if it does not exist.
     */
    @Nullable Object get(@NotNull String path, @Nullable Object def);

    /**
     * Sets the configuration value associated with the given path.
     *
     * @param path  config value path
     * @param value object to associate path with value
     */
    void set(@NotNull String path, @Nullable Object value);

    /**
     * Determines if the given path is a string.
     *
     * @param path config value path
     * @return {@code true} if the configuration key is a string
     */
    boolean isString(@NotNull String path);

    /**
     * {@link String} version of {@link #get(String)}.
     */
    @NotNull String getString(@NotNull String path);

    /**
     * {@link String} version of {@link #get(String, Object)}.
     * Note that unlike {@link #get(String, Object)}, this method <strong>DOES NOT</strong> accept {@code null} for its
     * {@code def} argument. Return a blank string instead.
     */
    @NotNull String getString(@NotNull String path, @NotNull String def);

    /**
     * {@code boolean} version of {@link #isString(String)}.
     */
    boolean isBoolean(@NotNull String path);

    /**
     * {@code boolean} version of {@link #get(String)}.
     */
    boolean getBoolean(@NotNull String path);

    /**
     * {@code boolean} version of {@link #get(String, Object)}.
     */
    boolean getBoolean(@NotNull String path, boolean def);

    /**
     * {@code int} version of {@link #isString(String)}.
     */
    boolean isInt(@NotNull String path);

    /**
     * {@code int} version of {@link #get(String)}.
     */
    int getInt(@NotNull String path);

    /**
     * {@code int} version of {@link #get(String, Object)}.
     */
    int getInt(@NotNull String path, int def);

    /**
     * {@code long} version of {@link #isString(String)}.
     */
    boolean isLong(@NotNull String path);

    /**
     * {@code long} version of {@link #get(String)}.
     */
    long getLong(@NotNull String path);

    /**
     * {@code long} version of {@link #get(String, Object)}.
     */
    long getLong(@NotNull String path, long def);

    /**
     * {@code double} version of {@link #isString(String)}.
     */
    boolean isDouble(@NotNull String path);

    /**
     * {@code double} version of {@link #get(String)}.
     */
    double getDouble(@NotNull String path);

    /**
     * {@code double} version of {@link #get(String, Object)}.
     */
    double getDouble(@NotNull String path, double def);

}
