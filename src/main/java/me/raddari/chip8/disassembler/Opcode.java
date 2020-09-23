package me.raddari.chip8.disassembler;

import com.google.common.base.MoreObjects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Opcode {

    private final Kind kind;
    private final List<Argument> args;

    private Opcode(Kind kind, List<Argument> args) {
        this.kind = kind;
        this.args = args;
    }

    public static @NotNull Opcode create(@NotNull Kind kind, @NotNull List<Argument> args) {
        return new Opcode(kind, args);
    }

    public @NotNull String toAssemblyString() {
        return kind.name();
    }

    public @NotNull Kind getKind() {
        return kind;
    }

    public @NotNull List<Argument> getArgs() {
        return List.copyOf(args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Opcode opcode = (Opcode) o;
        return kind == opcode.kind &&
                args.equals(opcode.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, args);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("kind", kind)
                .add("args", args)
                .toString();
    }

    @SuppressWarnings("java:S115") // Constant naming conventions
    public enum Kind {

        $0000("nop"), $00E0("cls"), $00EE("ret"),
        $0NNN("sys"), $2NNN("call"), $1NNN("jp"),
        $BNNN("jp"), $3XNN("se"), $4XNN("sne"),
        $5XY0("se"), $9XY0("sne"), $EX9E("skp"),
        $EXA1("sknp"), $FX0A("ld"), $6XNN("ld"),
        $8XY0("ld"), $FX07("ld"), $FX15("ld"),
        $FX18("ld"), $ANNN("ld"), $FX29("ld"),
        $FX55("ld"), $FX65("ld"), $FX1E("add"),
        $7XNN("add"), $8XY4("add"), $8XY5("sub"),
        $8XY7("subn"), $8XY1("or"), $8XY2("and"),
        $8XY3("xor"), $8XY6("shr"), $8XYE("shl"),
        $FX33("bcd"), $CXNN("rnd"), $DXYN("drw");

        private static final Logger LOGGER = LogManager.getLogger();
        private final String symbol;
        private final List<Argument.Type> argTypes;

        Kind(@NotNull String symbol) {
            this.symbol = symbol;
            this.argTypes = generateArgTypes();
        }

        public static @NotNull Kind parseString(@NotNull String string) {
            if (string.charAt(0) == '$') {
                string = string.substring(1);
            }
            string = string.toLowerCase();
            return $0000;
        }

        private List<Argument.Type> generateArgTypes() {
            var args = new ArrayList<Argument.Type>();
            var id = name().substring(1);

            if (id.contains("NNN")) {
                args.add(Argument.Type.ADDRESS);
            } else if (id.contains("NN")) {
                args.add(Argument.Type.CONSTANT_8);
            } else if (id.contains("N")) {
                args.add(Argument.Type.CONSTANT_4);
            }

            if (id.contains("X")) {
                args.add(Argument.Type.REGISTER_X);
            }
            if (id.contains("Y")) {
                args.add(Argument.Type.REGISTER_Y);
            }

            return args;
        }

        public @NotNull String getSymbol() {
            return symbol;
        }

        public @NotNull List<Argument.Type> getArgTypes() {
            return List.copyOf(argTypes);
        }

    }

}
