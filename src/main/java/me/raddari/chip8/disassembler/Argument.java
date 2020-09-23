package me.raddari.chip8.disassembler;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Argument {

    private final Type type;
    private final int value;

    public static @NotNull Argument of(@NotNull Type type, int value) {
        return new Argument(type, value);
    }

    private Argument(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public @NotNull Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Argument argument = (Argument) o;
        return value == argument.value &&
                type == argument.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("value", value)
                .toString();
    }

    public enum Type {
        ADDRESS, REGISTER, CONSTANT
    }

}
