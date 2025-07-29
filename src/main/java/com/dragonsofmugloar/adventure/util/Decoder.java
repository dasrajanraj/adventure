package com.dragonsofmugloar.adventure.util;

public class Decoder {

    public static String decodeRot13(String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                result.append((char) (((c - 'a' + 13) % 26) + 'a'));
            } else if (c >= 'A' && c <= 'Z') {
                result.append((char) (((c - 'A' + 13) % 26) + 'A'));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static String decodeBase64(String input) {
        return new String(java.util.Base64.getDecoder().decode(input));
    }
}