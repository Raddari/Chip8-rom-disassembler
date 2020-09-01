package me.raddari.chip8.instruction;

import me.raddari.chip8.disassembler.Opcode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        var sb = new StringBuilder();
        sb.append(opcode.getAssemblySymbol());

        if (!args.isEmpty()) {
            sb.append(" ");
            for (var it = args.listIterator(); it.hasNext(); ) {
                var element = it.next();
                if (element.getArgType() == Argument.ArgType.REGISTER) {
                    sb.append(Argument.ArgType.REGISTER.getPrefix())
                            .append("r")
                            .append(Integer.toHexString(element.getValue()));
                } else {
                    sb.append(element.getValue());
                }

                if (it.hasNext()) {
                    sb.append(",");
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%s[opcode=%s, args=%s]",
                             getClass().getSimpleName(), opcode, args);
    }

}
