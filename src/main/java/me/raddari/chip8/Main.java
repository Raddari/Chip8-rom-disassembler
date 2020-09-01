package me.raddari.chip8;

import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.instruction.Instruction;

import java.io.File;

public final class Main {

    public static void main(String[] args) {
        var romPath = Main.class.getClassLoader().getResource("rom/RandomNumberTest.ch8").getPath();
        Disassembler.create()
                .disassemble(new File(romPath))
                .stream()
                .map(Instruction::toAssemblyString)
                .forEach(System.out::println);
    }

}
