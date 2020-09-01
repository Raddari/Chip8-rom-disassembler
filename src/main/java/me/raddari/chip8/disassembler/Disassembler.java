package me.raddari.chip8.disassembler;

import me.raddari.chip8.instruction.Instruction;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface Disassembler {

    /**
     * Create a disassembler. Guaranteed to not be {@code null}.
     * @return new {@link Disassembler} instance
     */
    static @NotNull Disassembler create() {
        return new AsyncDisassembler();
    }
    /**
     * Disassemble a ROM into assembly code.
     * @param romFile Chip8 executable binary file
     * @return binary file composed into list of assembly instructions
     */
    @NotNull List<Instruction> disassemble(@NotNull File romFile);

}
