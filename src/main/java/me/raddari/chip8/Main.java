package me.raddari.chip8;

import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.disassembler.Opcode;

import java.io.File;

public final class Main {

    public static void main(String[] args) {
        var romFile = new File(Main.class.getResource("rom/RandomNumberTest.ch8").getPath());
        Disassembler.create()
                .disassemble(romFile)
                .stream()
                .map(Opcode::toAssemblyString)
                .forEach(System.out::println);
    }

}
