package me.raddari.chip8.instruction;

import me.raddari.chip8.disassembler.Opcode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class Instruction {

    private final Opcode opcode;
    private final List<Argument> args;

    public @NotNull Instruction(@NotNull Opcode opcode, @NotNull List<Argument> args) {
        this.opcode = opcode;
        this.args = new ArrayList<>(args);
    }

    public @NotNull Opcode getOpcode() {
        return opcode;
    }

    public @NotNull List<Argument> getArgs() {
        return args;
    }

    public @NotNull String toAssemblyString() {
        return opcode.getAssemblySymbol();
    }

    @Override
    public String toString() {
        return String.format("%s[opcode=%s, args=%s]",
                             getClass().getSimpleName(), opcode, args);
    }

}
