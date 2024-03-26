package com.company.lab2;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String message = "Secret message!";
        BufferedImage image = loadImage(new File("src/com/company/lab2/image.png"));

    }

    private static BufferedImage loadImage(File file) {
        BufferedImage img ;
        try {
            System.out.println(file.getAbsoluteFile());
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            img = null;
        }
        return img ;
    }

    private void writeImage(File file, BufferedImage img){
        try{
            ImageIO.write(img, "bmp", file );
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
