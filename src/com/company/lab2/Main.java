package com.company.lab2;

import com.company.utils.ImageWorker;

import java.awt.image.BufferedImage;
import java.io.File;


public class Main {

    public static void main(String[] args) {
        String message = "SUPER SECRET MESSAGE";
        BufferedImage image = ImageWorker.loadImage(new File("src/com/company/lab2/image.png"));

        BufferedImage embeddedImage = KJB.embedMessage(image, message);

        ImageWorker.saveImage(embeddedImage, "src/com/company/lab2/embedded.png");
        embeddedImage = ImageWorker.loadImage(new File("src/com/company/lab2/embedded.png"));
        System.out.println(KJB.extractMessage(embeddedImage));
    }


}
