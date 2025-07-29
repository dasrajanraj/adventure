package com.dragonsofmugloar.adventure.util;

public class Decoder {
    
    public static String decodeBase64(String input) {
        return new String(java.util.Base64.getDecoder().decode(input));
    }
}