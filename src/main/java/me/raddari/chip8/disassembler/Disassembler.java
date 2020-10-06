package me.raddari.chip8.disassembler;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface Disassembler {

    @NotNull Program disassemble(@NotNull File romFile);

    static @NotNull Disassembler create() {
        return new AsyncDisassembler();
    }

}
