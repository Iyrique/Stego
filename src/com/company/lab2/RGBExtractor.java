package com.company.lab2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RGBExtractor {

    private int[][] r;
    private int[][] g ;
    private int[][] b ;

    public void extractRGB(BufferedImage image) {
        int width = image.getWidth() ;
        int height = image.getHeight();
        r  = new int[height][width];
        g  = new int[height][width];
        b  = new int[height][width];
        Color pixel = null;
        for(int i = 0 ; i < height; ++i) {
            for(int j = 0 ; j < width ; ++j) {
                pixel = new Color(image.getRGB(i, j));
                r[i][j] = pixel.getRed();
                g[i][j] = pixel.getGreen();
                b[i][j] = pixel.getBlue();
            }
        }
    }

    public void changeBlue(BufferedImage image, int[][] blue){
        int width = image.getWidth() ;
        int height = image.getHeight() ;
        Color pixel ;
        for(int i = 2 ; i < width ;++i) {
            for (int j = 2 ; j < height ;++j) {
                pixel = new Color(image.getRGB(i, j));
                Color c = new Color(pixel.getRed(), pixel.getGreen(), blue[i][j]);
                image.setRGB(i, j, c.getRGB());
            }
        }
    }

    public int[][] getR() {
        return r;
    }

    public int[][] getG() {
        return g;
    }

    public int[][] getB() {
        return b;
    }
}
