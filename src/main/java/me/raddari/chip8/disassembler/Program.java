package me.raddari.chip8.disassembler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Program {

    private final String name;
    private final List<Opcode> opcodes;

    public Program(@NotNull String name, @NotNull List<Opcode> opcodes) {
        this.name = name;
        this.opcodes = List.copyOf(opcodes);
    }

    public String name() {
        return name;
    }

    public List<Opcode> opcodes() {
        return List.copyOf(opcodes);
    }

}
