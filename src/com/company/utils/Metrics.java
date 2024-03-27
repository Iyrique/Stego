package com.company.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Metrics {

    // Максимальное абсолютное отклонение (Мю maxD)
    public static double calculateMAE(int originalRGB, int newRGB) {
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
    public static double calculateNMSE(String originalImagePath, String modifiedImagePath) {
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
    public static double calculatePSNR(String originalImagePath, String modifiedImagePath) {
        double ans = 0;
        double sumSquaredErrors = 0.0;
        double maxValue = 0;
        try {
            BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
            BufferedImage modifiedImage = ImageIO.read(new File(modifiedImagePath));

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            ans = width * height;
            maxValue = Math.pow(originalImage.getRGB(1, 1), 2);
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
