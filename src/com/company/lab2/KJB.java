package com.company.lab2;

import com.company.utils.BinaryConverter;

import java.awt.image.BufferedImage;

public class KJB {

    private static final double LAMDA = 1;

    private static final int SIGMA = 2;

    private static final String LAST_BINARY = "1111111111111110";

    public static BufferedImage embedMessage(BufferedImage image, String message) {
        // Преобразование сообщения в двоичный формат
        String binaryMessage = BinaryConverter.stringToBinary(message);

        binaryMessage += LAST_BINARY;

        // Получение размеров изображения
        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder sb = new StringBuilder();

        int x1 = 0;
        int y1 = 0;
        outer:
        for (int y = SIGMA; y < height - SIGMA; y += SIGMA + 1) {
            for (int x = SIGMA; x < width - SIGMA; x += SIGMA + 1) {
                // Получаем синюю цветовую компоненту пикселя
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Извлекаем бит сообщения
                if ((y1 * width + x1) < binaryMessage.length()) {
                    char msgBit = binaryMessage.charAt(y1 * width + x1);
                    sb.append(msgBit);
                    blue = changeBlueValue(blue, calculateBrightness(red, green, blue), msgBit);

                    // Обновляем пиксель с измененной синей компонентой
                    int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    image.setRGB(x, y, newPixel);
                } else {
                    break outer;
                }
                x1++;
            }
            y1++;
            x1 = 0;
        }
        System.out.println(sb);
        return image;
    }

    public static String extractMessage(BufferedImage image) {
        StringBuilder extractedMessage = new StringBuilder();
        int width = image.getWidth();
        int height = image.getHeight();
        outer:
        for (int y = SIGMA; y < height - SIGMA; y += SIGMA + 1) {
            for (int x = SIGMA; x < width - SIGMA; x += SIGMA + 1) {
                int pixel = image.getRGB(x, y);
                int blue = pixel & 0xFF;
                if (blue > average(image, x, y)) {
                    extractedMessage.append(1);
                } else {
                    extractedMessage.append(0);
                }
                if (extractedMessage.toString().contains(LAST_BINARY)) {
                    break outer;
                }
            }
        }
        String answer = BinaryConverter.binaryToString(extractedMessage.toString());
        return answer.substring(0, answer.length() - 2);
    }

    private static double average(BufferedImage image, int x, int y) {
        double answer = 0;
        for (int i = 1; i <= SIGMA; i++) {
            int pixel1 = image.getRGB(x, y + i);
            int blue1 = pixel1 & 0xFF;
            int pixel2 = image.getRGB(x, y - i);
            int blue2 = pixel2 & 0xFF;
            int pixel3 = image.getRGB(x + i, y);
            int blue3 = pixel3 & 0xFF;
            int pixel4 = image.getRGB(x - i, y);
            int blue4 = pixel4 & 0xFF;
            answer += (blue1 + blue2 + blue3 + blue4);
        }
        return answer / (4 * SIGMA);
    }

    private static int changeBlueValue(int bxy, double Lxy, char bit) {
        int result;
        if (bit == '1') {
            result = (int) (bxy + LAMDA * Lxy);
            if (result > 255) {
                result = 255;
            }
        } else {
            result = (int) (bxy - LAMDA * Lxy);
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
