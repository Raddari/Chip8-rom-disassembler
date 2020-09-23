package me.raddari.chip8.disassembler;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class Opcode {

    private final Kind kind;
    private final List<Argument> args;

    private Opcode(Kind kind, List<Argument> args) {
        this.kind = kind;
        this.args = args;
    }

    public static @NotNull Opcode create(@NotNull Kind kind, @NotNull List<Argument> args) {
        return new Opcode(kind, args);
    }

    public @NotNull String toAssemblyString() {
        return "";
    }

    public @NotNull Kind getKind() {
        return kind;
    }

    public @NotNull List<Argument> getArgs() {
        return List.copyOf(args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Opcode opcode = (Opcode) o;
        return kind == opcode.kind &&
                args.equals(opcode.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, args);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("kind", kind)
                .add("args", args)
                .toString();
    }

    public enum Kind {

    }

}
