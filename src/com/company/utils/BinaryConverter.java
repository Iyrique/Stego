package com.company.utils;

public class BinaryConverter {

    public static String stringToBinary(String input) {
        StringBuilder binaryString = new StringBuilder();

        for (char c : input.toCharArray()) {
            String binaryChar = Integer.toBinaryString(c);

            while (binaryChar.length() < 8) {
                binaryChar = "0" + binaryChar;
            }
            binaryString.append(binaryChar);
        }

        return binaryString.toString();
    }

    public static String binaryToString(String binaryInput) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < binaryInput.length(); i += 8) {
            String byteString = binaryInput.substring(i, Math.min(i + 8, binaryInput.length()));
            char character = (char) Integer.parseInt(byteString, 2);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    public static String intToBinary(int number) {
        StringBuilder binary = new StringBuilder();
        while (number > 0) {
            binary.insert(0, number % 2);
            number /= 2;
        }
        return binary.toString();
    }

    public static int binaryToInt(String binary) {
        int number = 0;
        int power = 0;
        for (int i = binary.length() - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                number += Math.pow(2, power);
            }
            power++;
        }
        return number;
    }
}
