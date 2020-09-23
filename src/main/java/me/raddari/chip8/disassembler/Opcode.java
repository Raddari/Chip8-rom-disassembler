package me.raddari.chip8.disassembler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Opcode {

    private final Kind kind;
    private final List<Argument> args;

    private Opcode(Kind kind, List<Argument> args) {
        this.kind = kind;
        this.args = args;
    }

    public @NotNull String toAssemblyString() {
        return "";
    }

    public enum Kind {

    }

}
