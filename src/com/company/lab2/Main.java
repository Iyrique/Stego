package com.company.lab2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String message = "A";
        BufferedImage image = loadImage(new File("src/com/company/lab2/image.png"));

        BufferedImage embeddedImage = KJB.embedMessage(image, message);

        saveImage(embeddedImage, "src/com/company/lab2/embedded.png");
        embeddedImage = loadImage(new File("src/com/company/lab2/embedded.png"));
        System.out.println(KJB.extractMessage(embeddedImage));
    }

    public static void saveImage(BufferedImage image, String outputPath) {
        try {
            File output = new File(outputPath);
            ImageIO.write(image, "png", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage loadImage(File file) {
        BufferedImage img;
        try {
            System.out.println(file.getAbsoluteFile());
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            img = null;
        }
        return img;
    }
}
