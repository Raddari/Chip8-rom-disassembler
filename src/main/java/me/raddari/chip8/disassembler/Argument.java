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

    public @NotNull Type type() {
        return type;
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Argument)) {
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
        ADDRESS, CONSTANT_4, CONSTANT_8, REGISTER_X, REGISTER_Y;

        private final String configCategory;

        Type() {
            this.configCategory = generateConfigCategory();
        }

        public boolean isAddress() {
            return this == ADDRESS;
        }

        public boolean isConstant() {
            return this == CONSTANT_4 || this == CONSTANT_8;
        }

        public boolean isRegister() {
            return this == REGISTER_X || this == REGISTER_Y;
        }

        public @NotNull String configCategory() {
            return configCategory;
        }

        private String generateConfigCategory() {
            var underscore = name().indexOf('_');
            return underscore > -1 ? name().substring(0, underscore).toLowerCase() : name().toLowerCase();
        }

    }

}
