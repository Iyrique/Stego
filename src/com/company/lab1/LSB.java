package com.company.lab1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class LSB {

    public static void main(String[] args) {
        String coverImagePath = "src/com/company/lab1/image.png";
        String message = "Secret message!!!!!!";
        String stegoImagePath = "src/com/company/lab1/image_stego.png";

        Scanner scanner = new Scanner(System.in);

        System.out.println("LSB - алгоритм");
        System.out.println("---------------------------");
        System.out.print("Выберите, что вы хотите сделать (1. Спрятать; 2. Достать; 3. Спрятать и достать): ");
        int var = scanner.nextInt();
        System.out.println();
        if (var == 1) {
            System.out.print("Выберите вариант ответа (1. Указать путь до изображения; 2. Взять шаблон): ");
            if (scanner.nextInt() == 1) {
                System.out.print("Введите путь: ");
                if (scanner.hasNextLine()) {
                    coverImagePath = scanner.next();
                }

            }
            System.out.println();
            System.out.print("Выберите вариант ответа (1. Ввести текст; 2. Взять шаблон): ");
            if (scanner.nextInt() == 1) {
                System.out.print("Введите сообщение: ");
                if (scanner.hasNextLine()) {
                    message = scanner.next();
                }
            }
            System.out.println();
            starter(coverImagePath, message, stegoImagePath, true, false);
        } else if (var == 2) {
            System.out.print("Выберите вариант ответа (1. Указать путь до изображения; 2. Взять шаблон): ");
            if (scanner.nextInt() == 1) {
                System.out.print("Введите путь: ");
                if (scanner.hasNextLine()) {
                    stegoImagePath = scanner.next();
                }
            }
            System.out.println();
            starter(coverImagePath, message, stegoImagePath, false, true);
        } else if (var == 3) {
            System.out.print("Выберите вариант ответа (1. Указать путь до изображения; 2. Взять шаблон): ");
            if (scanner.nextInt() == 1) {
                System.out.print("Введите путь: ");
                if (scanner.hasNextLine()) {
                    coverImagePath = scanner.next();
                }

            }
            System.out.println();
            System.out.print("Выберите вариант ответа (1. Ввести текст; 2. Взять шаблон): ");
            if (scanner.nextInt() == 1) {
                System.out.print("Введите сообщение: ");
                if (scanner.hasNextLine()) {
                    message = scanner.next();
                }
            }
            System.out.println();
            System.out.print("Выберите вариант ответа (1. Указать путь до изображения; 2. Взять шаблон): ");
            if (scanner.nextInt() == 1) {
                System.out.print("Введите путь: ");
                if (scanner.hasNextLine()) {
                    stegoImagePath = scanner.next();
                }
            }
            System.out.println();
            starter(coverImagePath, message, stegoImagePath, true, true);
        } else {
            System.err.println("Вы ввели не тот вариант ответа! Попробуйте еще раз!");
        }

    }

    private static void starter(String coverImagePath, String message, String stegoImagePath, boolean hide, boolean extract) {
        if (hide) {
            hideMessage(coverImagePath, message, stegoImagePath);
        }
        if (extract) {
            String extractedMessage = extractMessage(stegoImagePath);
            System.out.println("Extracted message: " + extractedMessage);
        }
    }

    private static void hideMessage(String coverImagePath, String message, String stegoImagePath) {
        try {
            BufferedImage coverImage = ImageIO.read(new File(coverImagePath));
            int imageWidth = coverImage.getWidth();
            int imageHeight = coverImage.getHeight();
            int maxMessageLength = (imageWidth * imageHeight) / 8;
            if (message.length() + 8 > maxMessageLength) {
                System.out.println("Сообщение слишком длинное для данного изображения");
                return;
            }

            // Добавление информации о длине сообщения в изображение
            String messageLengthBinary = String.format("%32s", Integer.toBinaryString(message.length())).replace(' ', '0');
            int index = 0;
            for (int i = 0; i < 32; i++) {
                int pixel = coverImage.getRGB(index % imageWidth, index / imageWidth);
                pixel &= 0xFFFFFFFE; // Сбросим младший бит
                pixel |= (messageLengthBinary.charAt(i) - '0'); // Установим младший бит в зависимости от сообщения
                coverImage.setRGB(index % imageWidth, index / imageWidth, pixel);
                index++;
            }

            // Скрытие сообщения в изображении
            byte[] messageBytes = message.getBytes();
            System.out.println(Arrays.toString(messageBytes));
            for (byte b : messageBytes) {
                String num = "";
                for (int j = 7; j >= 0; j--) {
                    int pixel = coverImage.getRGB(index % imageWidth, index / imageWidth);
                    pixel &= 0xFFFFFFFE; // Сбросим младший бит
                    pixel |= ((b >> (7 - j)) & 1); // Установим младший бит в зависимости от байта сообщения
                    num += ((b >> (7 - j)) & 1);
                    coverImage.setRGB(index % imageWidth, index / imageWidth, pixel);
                    index++;
                }
            }

            // Сохранение изображения с сообщением
            ImageIO.write(coverImage, "png", new File(stegoImagePath));
            System.out.println("Сообщение успешно скрыто в изображении");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Извлечение сообщения из контейнера изображения
    private static String extractMessage(String stegoImagePath) {
        try {
            BufferedImage stegoImage = ImageIO.read(new File(stegoImagePath));
            int imageWidth = stegoImage.getWidth();

            // Извлечение длины сообщения из изображения
            StringBuilder lengthBinary = new StringBuilder();
            int index = 0;
            for (int i = 0; i < 32; i++) {
                int pixel = stegoImage.getRGB(index % imageWidth, index / imageWidth);
                lengthBinary.append(pixel & 1); // Извлекаем младший бит
                index++;
            }
            int messageLength = Integer.parseInt(lengthBinary.toString(), 2);

            // Извлечение сообщения из изображения
            StringBuilder messageBinary = new StringBuilder();
            for (int i = 0; i < messageLength; i++) {
                for (int j = 7; j >= 0; j--) {
                    int pixel = stegoImage.getRGB((index + j) % imageWidth, (index + j) / imageWidth);
                    messageBinary.append(pixel & 1); // Извлекаем младший бит

                }
                index += 8;
            }

            // Преобразование бинарного сообщения в строку
            StringBuilder extractedMessage = new StringBuilder();

            for (int i = 0; i < messageBinary.length(); i += 8) {
                extractedMessage.append((char) Integer.parseInt(messageBinary.substring(i, i + 8), 2));
            }
            byte[] messageBytes = extractedMessage.toString().getBytes();
            System.out.println(Arrays.toString(messageBytes));
            return extractedMessage.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

