package me.raddari.chip8.disassembler;

import me.raddari.chip8.instruction.Argument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ? Future: this enum badly needs refactoring
 */
@SuppressWarnings("java:S115")
public enum Opcode {

    $0000("nop"),  $00E0("cls"),  $00EE("ret"),
    $0NNN("sys"),  $2NNN("call"), $1NNN("jp"),
    $BNNN("jp"),   $3XNN("se"),   $4XNN("sne"),
    $5XY0("se"),   $9XY0("sne"),  $EX9E("skp"),
    $EXA1("sknp"), $FX0A("ld"),   $6XNN("ld"),
    $8XY0("ld"),   $FX07("ld"),   $FX15("ld"),
    $FX18("ld"),   $ANNN("ld"),   $FX29("ld"),
    $FX55("ld"),   $FX65("ld"),   $FX1E("add"),
    $7XNN("add"),  $8XY4("add"),  $8XY5("sub"),
    $8XY7("subn"), $8XY1("or"),   $8XY2("and"),
    $8XY3("xor"),  $8XY6("shr"),  $8XYE("shl"),
    $FX33("bcd"), $CXNN("rnd"), $DXYN("drw");

    private static final Logger LOGGER = LogManager.getLogger();
    private final String symbol;
    private final List<Argument.ArgType> argTypes;

    Opcode(@NotNull String symbol) {
        this.symbol = symbol;
        this.argTypes = createArgTypes();
    }

    public List<Argument.ArgType> opcodeArgTypes() {
        return new ArrayList<>(argTypes);
    }

    public @NotNull String getAssemblySymbol() {
        return symbol;
    }

    public static @NotNull Opcode find(@NotNull String opcode) {
        opcode = formatOpcode(opcode);

        return switch (opcode.charAt(0)) {
            case '0' -> find0(opcode);
            case '8' -> find8(opcode);
            case 'e' -> findE(opcode);
            case 'f' -> findF(opcode);
            default -> OPCODE_HASH1_LOOKUP.getOrDefault(hash1(opcode), $0000);
        };
    }

    private List<Argument.ArgType> createArgTypes() {
        var argTypes = new ArrayList<Argument.ArgType>();
        var op = name();

        for (var s : List.of("NNN", "NN", "N", "X", "Y")) {
            if (op.contains(s)) {
                argTypes.add(Argument.ArgType.decode(s));
                op = op.replace(s, "");
            }
        }

        return argTypes;
    }

    private static String formatOpcode(String opcode) {
        opcode = opcode.toLowerCase();
        if (opcode.charAt(0) == '$') {
            opcode = opcode.substring(1);
        }
        return opcode;
    }

    private static Opcode lookupOpcode(String opcode, Map<String, Opcode> opcodeMap) {
        var value = opcodeMap.get(opcode);
        if (value == null) {
            var upper = "$" + opcode.toUpperCase();
            var nullop = $0000.name();
            LOGGER.warn("Invalid opcode ({}), using nop ({})", upper, nullop);
            value = $0000;
        }
        return value;
    }

    private static Opcode find0(String opcode) {
        if (opcode.equals("0000")) {
            return $0000;
        }
        if (opcode.equals("00e0")) {
            return $00E0;
        }
        if (opcode.equals("00ee")) {
            return $00EE;
        }
        return $0NNN;
    }

    private static Opcode find8(String opcode) {
        return lookupOpcode(hash2(opcode), OPCODE_HASH2_LOOKUP);
    }

    private static Opcode findE(String opcode) {
        return lookupOpcode(hash3(opcode), OPCODE_HASH3_LOOKUP);
    }

    private static Opcode findF(String opcode) {
        return lookupOpcode(hash3(opcode), OPCODE_HASH3_LOOKUP);
    }

    private static String hash1(String opcode) {
        opcode = formatOpcode(opcode);
        return String.format("%c", opcode.charAt(0));
    }

    private static String hash2(String opcode) {
        opcode = formatOpcode(opcode);
        return String.format("%c%c", opcode.charAt(0), opcode.charAt(3));
    }

    private static String hash3(String opcode) {
        opcode = formatOpcode(opcode);
        return String.format("%c%c%c", opcode.charAt(0), opcode.charAt(2), opcode.charAt(3));
    }

    private static final Map<String, Opcode> OPCODE_HASH1_LOOKUP;
    private static final Map<String, Opcode> OPCODE_HASH2_LOOKUP;
    private static final Map<String, Opcode> OPCODE_HASH3_LOOKUP;

    static {
        OPCODE_HASH1_LOOKUP = new HashMap<>();
        OPCODE_HASH2_LOOKUP = new HashMap<>();
        OPCODE_HASH3_LOOKUP = new HashMap<>();

        for (var op : values()) {
            var hash1 = hash1(op.name());
            OPCODE_HASH1_LOOKUP.put(hash1, op);

            var hash2 = hash2(op.name());
            OPCODE_HASH2_LOOKUP.put(hash2, op);

            var hash3 = hash3(op.name());
            OPCODE_HASH3_LOOKUP.put(hash3, op);
        }
    }

}
