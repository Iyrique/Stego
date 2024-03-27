package com.company.lab1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class LSB {

    public static void main(String[] args) {
        String coverImagePath = "src/com/company/lab1/image.png"; //Ссылка на изображение-шаблон
        String message = "Secret message!!!!!!"; //Сообщение
        String stegoImagePath = "src/com/company/lab1/image_stego.png"; //Ссылка на изображение с сообщением

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
            System.out.println("Отношение сигнал шум: " + 1 / calculateNMSE(coverImagePath, stegoImagePath));
            System.out.println("Пиковое отношение сигнал-шум: " + calculatePSNR(coverImagePath, stegoImagePath));
        } else {
            System.err.println("Вы ввели не тот вариант ответа! Попробуйте еще раз!");
        }

    }

    //Функция, которая запускает функции скрытия и извлечения сообщения
    private static void starter(String coverImagePath, String message, String stegoImagePath, boolean hide, boolean extract) {
        if (hide) {
            hideMessage(coverImagePath, message, stegoImagePath);
        }
        if (extract) {
            String extractedMessage = extractMessage(stegoImagePath);
            System.out.println("Extracted message: " + extractedMessage);
        }
    }

    //Функция скрытия сообщения
    private static void hideMessage(String coverImagePath, String message, String stegoImagePath) {
        double maxAbsoluteError = 0; // Максимальное абсолютное отклонение
        try {
            BufferedImage coverImage = ImageIO.read(new File(coverImagePath)); //Считываем изображение
            int imageWidth = coverImage.getWidth(); //Получаем длину и ширину
            int imageHeight = coverImage.getHeight();
            int maxMessageLength = (imageWidth * imageHeight) / 8; // получаем максимально возможную длину сообщения
            if (message.length() + 8 > maxMessageLength) {  //Проверка на то, подойдет ли изображение для сокрытия или нет)
                System.out.println("Сообщение слишком длинное для данного изображения");
                return;
            }

            // Добавление информации о длине сообщения в изображение
            String messageLengthBinary = String.format("%32s", Integer.toBinaryString(message.length())).replace(' ', '0');
            int index = 0;
            for (int i = 0; i < 32; i++) { //В первые 32 байта в последний бит записываем информацию о длине изображения
                int pixel = coverImage.getRGB(index % imageWidth, index / imageWidth); //Получаем пиксель
                pixel &= 0xFFFFFFFE; // Сбросим младший бит
                pixel |= (messageLengthBinary.charAt(i) - '0'); // Установим младший бит в зависимости от сообщения
                coverImage.setRGB(index % imageWidth, index / imageWidth, pixel); //устанавливаем пиксель в результат
                index++; //Двигаемся дальше
            }

            // Скрытие сообщения в изображении
            byte[] messageBytes = message.getBytes(); //Преобразуем сообщение в последовательность байтов
            System.out.println(Arrays.toString(messageBytes)); //Вывод в консоль для проверки
            for (byte b : messageBytes) { //Проходим по байтовому представлению сообщения
                String num = "";
                for (int j = 7; j >= 0; j--) {
                    int pixel = coverImage.getRGB(index % imageWidth, index / imageWidth);
                    int oldPixel = pixel;
                    pixel &= 0xFFFFFFFE; // Сбросим младший бит
                    pixel |= ((b >> (7 - j)) & 1); // Установим младший бит в зависимости от байта сообщения
                    num += ((b >> (7 - j)) & 1);
                    coverImage.setRGB(index % imageWidth, index / imageWidth, pixel);
                    index++;

                    // Вычисление максимального абсолютного отклонения
                    double pixelMAE = calculateMAE(oldPixel, pixel);
                    if (pixelMAE > maxAbsoluteError) {
                        maxAbsoluteError = pixelMAE;
                    }
                }
            }

            // Сохранение изображения с сообщением
            ImageIO.write(coverImage, "png", new File(stegoImagePath));
            System.out.println("Сообщение успешно скрыто в изображении");
            System.out.println("Максимальное абсолютное отклонение: " + maxAbsoluteError );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Извлечение сообщения из контейнера изображения
    private static String extractMessage(String stegoImagePath) {
        try {
            BufferedImage stegoImage = ImageIO.read(new File(stegoImagePath)); //Считываем изображение
            int imageWidth = stegoImage.getWidth(); // Получаем длину изображения

            // Извлечение длины сообщения из изображения
            StringBuilder lengthBinary = new StringBuilder();
            int index = 0;
            for (int i = 0; i < 32; i++) {
                int pixel = stegoImage.getRGB(index % imageWidth, index / imageWidth);
                lengthBinary.append(pixel & 1); // Извлекаем младший бит
                index++;
            }
            int messageLength = Integer.parseInt(lengthBinary.toString(), 2); //Преобразуем строку в целочисленный тип

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
            return extractedMessage.toString(); //Возвращаем сообщение
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Максимальное абсолютное отклонение (Мю maxD)
    private static double calculateMAE(int originalRGB, int newRGB) {
        int originalRed = (originalRGB >> 16) & 0xFF; //Получаем красный в оригинале
        int originalGreen = (originalRGB >> 8) & 0xFF; //Получаем зеленый в оригинале
        int originalBlue = originalRGB & 0xFF; //Получаем синий в оригинале

        int newRed = (newRGB >> 16) & 0xFF; //Получаем новый красный
        int newGreen = (newRGB >> 8) & 0xFF; //Получаем новый зеленый
        int newBlue = newRGB & 0xFF; //Получаем новый синий

        double redDiff = Math.abs(originalRed - newRed); //Считаем разницу
        double greenDiff = Math.abs(originalGreen - newGreen); //Считаем разницу
        double blueDiff = Math.abs(originalBlue - newBlue); //Считаем разницу

        return Math.max(Math.max(redDiff, greenDiff), blueDiff); // возвращаем максимальное
    }

    //ЗДесь считается нормированное среднее отклонение, необходимое для отношения сигнал шум (все как в формуле)
    private static double calculateNMSE(String originalImagePath, String modifiedImagePath) {
        double sumSquaredErrors = 0.0;

        try {
            BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
            BufferedImage modifiedImage = ImageIO.read(new File(modifiedImagePath));

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int sum = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int originalRGB = originalImage.getRGB(x, y);
                    int modifiedRGB = modifiedImage.getRGB(x, y);

                    double originalRed = (originalRGB >> 16) & 0xFF;
                    double originalGreen = (originalRGB >> 8) & 0xFF;
                    double originalBlue = originalRGB & 0xFF;

                    double modifiedRed = (modifiedRGB >> 16) & 0xFF;
                    double modifiedGreen = (modifiedRGB >> 8) & 0xFF;
                    double modifiedBlue = modifiedRGB & 0xFF;

                    double squaredErrorRed = Math.pow(originalRed - modifiedRed, 2);
                    double squaredErrorGreen = Math.pow(originalGreen - modifiedGreen, 2);
                    double squaredErrorBlue = Math.pow(originalBlue - modifiedBlue, 2);

                    sum += Math.pow(originalRGB, 2);
                    sumSquaredErrors += squaredErrorRed + squaredErrorGreen + squaredErrorBlue;
                }
            }

            double meanSquaredError = sumSquaredErrors / (width * height * 3);
            double rootMeanSquaredError = Math.sqrt(meanSquaredError);

            return rootMeanSquaredError / sum; // Нормирование по максимальному значению пикселя
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    //ЗДесь считается пиковое отношение сигнал-шум (все как в формуле)
    private static double calculatePSNR(String originalImagePath, String modifiedImagePath) {
        double ans = 0;
        double sumSquaredErrors = 0.0;
        double maxValue = 0;
        try {
            BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
            BufferedImage modifiedImage = ImageIO.read(new File(modifiedImagePath));

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            ans = width * height;
            maxValue = Math.pow(originalImage.getRGB(1,1), 2);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int originalRGB = originalImage.getRGB(x, y);
                    int modifiedRGB = modifiedImage.getRGB(x, y);

                    double sqrOrig = Math.pow(originalRGB, 2);
                    if (sqrOrig > maxValue) {
                        maxValue = sqrOrig;
                    }

                    double originalRed = (originalRGB >> 16) & 0xFF;
                    double originalGreen = (originalRGB >> 8) & 0xFF;
                    double originalBlue = originalRGB & 0xFF;

                    double modifiedRed = (modifiedRGB >> 16) & 0xFF;
                    double modifiedGreen = (modifiedRGB >> 8) & 0xFF;
                    double modifiedBlue = modifiedRGB & 0xFF;

                    double squaredErrorRed = Math.pow(originalRed - modifiedRed, 2);
                    double squaredErrorGreen = Math.pow(originalGreen - modifiedGreen, 2);
                    double squaredErrorBlue = Math.pow(originalBlue - modifiedBlue, 2);


                    sumSquaredErrors += squaredErrorRed + squaredErrorGreen + squaredErrorBlue;
                }
            }

            double meanSquaredError = sumSquaredErrors / (width * height * 3);
            double rootMeanSquaredError = Math.sqrt(meanSquaredError);

            return ans * maxValue / rootMeanSquaredError;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

}

