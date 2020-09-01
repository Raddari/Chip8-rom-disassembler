package me.raddari.chip8.instruction;

import org.jetbrains.annotations.NotNull;

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
        ADDRESS(), CONSTANT(), REGISTER("#");

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

    }

}
