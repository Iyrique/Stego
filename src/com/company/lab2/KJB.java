package com.company.lab2;

import java.awt.image.BufferedImage;

public class KJB {

    private static final double LAMDA = 0.1;
    private static final int SIGMA = 2;

    public static BufferedImage embedMessage(BufferedImage image, String message) {
        // Преобразование сообщения в двоичный формат
        String binaryMessage = BinaryConverter.stringToBinary(message);

        // Получение длины сообщения
        int messageLength = binaryMessage.length();

        // Запись длины сообщения в начало
        String lengthBinary = BinaryConverter.intToBinary(messageLength);
        while (lengthBinary.length() < 32) { // Для примера, считаем, что длина сообщения не превышает 2^32 - 1
            lengthBinary = "0" + lengthBinary;
        }
        String lengthString = BinaryConverter.binaryToString(lengthBinary);
        String lengthMessage = BinaryConverter.stringToBinary(lengthString);
        binaryMessage = lengthMessage + binaryMessage;

        // Получение размеров изображения
        int width = image.getWidth();
        int height = image.getHeight();

        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Получаем синюю цветовую компоненту пикселя
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Извлекаем бит сообщения
                if ((y * width + x) < binaryMessage.length()) {
                    char msgBit = binaryMessage.charAt(y * width + x);

                    blue = changeBlueValue(blue, calculateBrightness(red, green, blue), msgBit);

                    // Обеспечиваем, чтобы значение синего оставалось в диапазоне [0, 255]
                    blue = Math.max(0, Math.min(255, blue));

                    // Обновляем пиксель с измененной синей компонентой
                    pixel = (pixel & 0xff00ffff) | (blue << 16);
                    image.setRGB(x, y, pixel);
                } else {
                    break outer;
                }
            }
        }
        return image;
    }

    public static String extractMessage(BufferedImage image) {
        StringBuilder extractedMessage = new StringBuilder();
        int width = image.getWidth();
        int height = image.getHeight();



        return extractedMessage.toString();
    }



    private static int changeBlueValue(int bxy, double Lxy, char bit) {
        int result;
        if (bit == '1') {
            result = (int) (bxy - LAMDA * Lxy);
            if (result > 255) {
                result = 255;
            }
        } else {
            result = (int) (bxy + LAMDA * Lxy);
            if (result < 0) {
                result = 0;
            }
        }
        return result;
    }

    private static int calculateBrightness(int r, int g, int b) {
        return (int) (0.299 * r + 0.587 * g + 0.114 * b);
    }
}
