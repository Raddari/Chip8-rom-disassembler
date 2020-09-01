package me.raddari.chip8.util;

import org.jetbrains.annotations.NotNull;

public final class Numbers {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private Numbers() {
        // No instances
    }

    public static @NotNull String bytesToHex(byte[] bytes) {
        var hexChars = new char[bytes.length * 2];
        for (var j = 0; j < bytes.length; j++) {
            var v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
