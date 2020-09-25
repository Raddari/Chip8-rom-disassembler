package me.raddari.chip8.disassembler;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface Disassembler {

    @NotNull List<Opcode> disassemble(@NotNull File romFile);

    static @NotNull Disassembler create() {
        return new AsyncDisassembler();
    }

}
