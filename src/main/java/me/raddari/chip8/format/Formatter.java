package me.raddari.chip8.format;

import me.raddari.chip8.config.Configuration;
import org.jetbrains.annotations.NotNull;

/**
 * <p>{@code Formatter} is able to format a given instance of {@link T} to a string. A {@link Configuration}
 * is used to determine exactly how the instance is to be formatted.
 *
 * @param <T> object type to be formatted
 */
public interface Formatter<T> {

    /**
     * Formats the given {@link T} instance to a string.
     *
     * @param object instance to format
     * @return string formatted {@link T}
     */
    @NotNull String formatToString(T object);

    /**
     * Uses the given configuration for formatting rules.
     * Returns a formatter instance for easy chaining. Depending on the
     * implementation, this may or may not be the current formatter instance.
     *
     * @param config format configuration to use
     * @return {@link Formatter} instance
     */
    @NotNull Formatter<T> useConfiguration(@NotNull Configuration config);

}
