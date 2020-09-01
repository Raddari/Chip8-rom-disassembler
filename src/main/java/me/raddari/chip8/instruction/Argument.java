package me.raddari.chip8.instruction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Argument {

    private final ArgType argType;
    private final int value;

    Argument(ArgType argType, int value) {
        this.argType = argType;
        this.value = value;
    }

    public static @NotNull Argument of(@NotNull ArgType argType, int value) {
        return new Argument(argType, value);
    }

    public @NotNull ArgType getArgType() {
        return argType;
    }

    public int getValue() {
        return value;
    }


    public enum ArgType {
        ADDRESS(), CONSTANT(), REGX("#"), REGY("#");

        private final String prefix;

        ArgType() {
            this("");
        }

        ArgType(@NotNull String prefix) {
            this.prefix = prefix;
        }

        public @NotNull String getPrefix() {
            return prefix;
        }

        @Contract("null -> null")
        public static @Nullable ArgType decode(@Nullable String pattern) {
            if (pattern == null) {
                return null;
            }

            return switch (pattern) {
                case "NNN" -> ADDRESS;
                case "NN", "N" -> CONSTANT;
                case "X" -> REGX;
                case "Y" -> REGY;
                default -> null;
            };
        }

    }

}
