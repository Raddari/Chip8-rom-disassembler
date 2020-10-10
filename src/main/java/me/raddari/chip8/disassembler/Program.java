package me.raddari.chip8.disassembler;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class Program {

    private final String name;
    private final List<Opcode> opcodes;
    private final Map<Integer, String> labels;

    public Program(@NotNull String name, @NotNull List<Opcode> opcodes, @NotNull Map<Integer, String> labels) {
        this.name = name;
        this.opcodes = List.copyOf(opcodes);
        this.labels = Map.copyOf(labels);
    }

    public String name() {
        return name;
    }

    public List<Opcode> opcodes() {
        return List.copyOf(opcodes);
    }

    public Map<Integer, String> getLabels() {
        return Map.copyOf(labels);
    }

}
