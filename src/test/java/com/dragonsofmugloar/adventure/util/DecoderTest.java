package com.dragonsofmugloar.adventure.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class DecoderTest {

    @Test
    void decodeRot13_shouldReturnDecodedStringForLowercase() {
        String input = "uryyb";
        String expected = "hello";

        String result = Decoder.decodeRot13(input);

        assertEquals(expected, result);
    }

    @Test
    void decodeRot13_shouldReturnDecodedStringForUppercase() {
        String input = "URYYB";
        String expected = "HELLO";

        String result = Decoder.decodeRot13(input);

        assertEquals(expected, result);
    }

    @Test
    void decodeRot13_shouldPreserveNonAlphabeticCharacters() {
        String input = "Uryyb, Jbeyq! 123";
        String expected = "Hello, World! 123";

        String result = Decoder.decodeRot13(input);

        assertEquals(expected, result);
    }

    @Test
    void decodeRot13_shouldBeSymmetric() {
        String original = "TheQuickBrownFox123!";
        String encoded = Decoder.decodeRot13(original);
        String decoded = Decoder.decodeRot13(encoded);

        assertEquals(original, decoded);
    }

    @Test
    void decodeBase64_shouldReturnDecodedString() {
        String input = "SGVsbG8gd29ybGQ=";
        String expected = "Hello world";

        String result = Decoder.decodeBase64(input);

        assertEquals(expected, result);
    }

    @Test
    void decodeBase64_shouldHandleEmptyString() {
        String input = "";
        String expected = "";

        String result = Decoder.decodeBase64(input);

        assertEquals(expected, result);
    }

    @Test
    void decodeBase64_shouldThrowExceptionForInvalidBase64() {
        String invalidInput = "!!notbase64==";

        assertThrows(IllegalArgumentException.class, () -> {
            Decoder.decodeBase64(invalidInput);
        });
    }
}