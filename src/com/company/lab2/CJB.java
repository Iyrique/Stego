package com.company.lab2;

public class CJB {

    private static final double LAMDA = 0.1;
    private static final int SIGMA = 2;
    private static final RGBExtractor rgbExtractor = new RGBExtractor();



    private static int changeBlueValue(int bxy, double Lxy, char bit) {
        int result;
        if (bit == '1') {
            result = (int) (bxy - LAMDA * Lxy);
            if (result > 255) {
                result = 255;
            }
        } else {
            result = (int) (bxy + LAMDA * Lxy);
            if (result < 0) {
                result = 0;
            }
        }
        return result;
    }

    private static double[][] calculateBrightness(int[][] r, int[][] g, int[][] b, int width, int height) {
        double[][] L = new double[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                L[i][j] = (int) (0.299 * r[i][j] + 0.587 * g[i][j] + 0.114 * b[i][j]);
            }
        }
        return L;
    }
}
