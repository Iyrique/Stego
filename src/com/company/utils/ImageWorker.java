package com.company.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWorker {

    public static void saveImage(BufferedImage image, String outputPath) {
        try {
            File output = new File(outputPath);
            ImageIO.write(image, "png", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage loadImage(File file) {
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
