package com.company.lab2;

import com.company.utils.ImageWorker;
import com.company.utils.Metrics;

import java.awt.image.BufferedImage;
import java.io.File;


public class Main {

    public static void main(String[] args) {
        String message = "SUPER SECRET MESSAGE!!!";
        String originalImage = "src/com/company/lab2/image.png";
        String stegoImage = "src/com/company/lab2/embedded.png";
        BufferedImage image = ImageWorker.loadImage(new File(originalImage));

        BufferedImage embeddedImage = KJB.embedMessage(image, message);

        ImageWorker.saveImage(embeddedImage, stegoImage);
        embeddedImage = ImageWorker.loadImage(new File(stegoImage));
        System.out.println(KJB.extractMessage(embeddedImage));

        System.out.println("Отношение сигнал шум: " + Metrics.calculateSNR(originalImage, stegoImage));

        System.out.println("Среднее квадратичное отклонение: " + Metrics.calculateMSE(originalImage, stegoImage));

        System.out.println("Среднее квадратичное отклонение лапласиана: " + Metrics.calculateLMSE(originalImage, stegoImage));
    }


}
