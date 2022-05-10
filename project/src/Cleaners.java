import java.awt.Color;
import java.io.IOException;

import javax.rmi.CORBA.Util;

public class Cleaners {
    Color image1[][];
    Color image2[][];
    Color image3[][];

    Cleaners(){

    }

    Cleaners(String filename1, String filename2, String filename3) {
        this.LoadImages(filename1, filename2, filename3);
    }

    public void LoadImages(String filename1, String filename2, String filename3) {
        image1 = Utils.loadImage(filename1);
        image2 = Utils.loadImage(filename2);
        image3 = Utils.loadImage(filename3);
    }

    public void CleanImage(String outputFile) throws IOException {
        Color[][] tmp = Utils.copyImage(image1);

        tmp = cleanImage(tmp, image1, image2, image3);

        System.out.println("Starting file write...");
        Utils.writeImage(tmp, outputFile);
    }

    public Color[][] cleanImage() {
        return cleanImage(new Color[image1.length][image1[0].length], this.image1, this.image2, this.image3);
    }

    public static Color[][] cleanImage(Color[][] tmp, Color[][] image1, Color[][] image2, Color[][] image3) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                Color p1 = image1[i][j];
                Color p2 = image2[i][j];
                Color p3 = image3[i][j];

                tmp[i][j] = choosePixel(p1, p2, p3);
            }
        }

        return tmp;
    }

    private static Color choosePixel(Color s1, Color s2, Color s3){
        Color average = averagePixelColor(s1, s2, s3);

        int distance1 = calculateDistance(average, s1);
        int distance2 = calculateDistance(average, s2);
        int distance3 = calculateDistance(average, s3);

        if (distance1 < distance2 && distance1 < distance3) {
            return s1;
        } else if (distance2 < distance1 && distance2 < distance3) {
            return s2;
        } else {
            return s3;
        }
    }

    private static Color averagePixelColor(Color s1, Color s2, Color s3) {
        int r = (s1.getRed() + s2.getRed() + s3.getRed()) / 3;
        int g = (s1.getGreen() + s2.getGreen() + s3.getGreen()) / 3;
        int b = (s1.getBlue() + s2.getBlue() + s3.getBlue()) / 3;
        return new Color(r, g, b);
    }

    private static int calculateDistance(Color average, Color s1) {
        int r = average.getRed() - s1.getRed();
        int g = average.getGreen() - s1.getGreen();
        int b = average.getBlue() - s1.getBlue();
        return (int) Math.sqrt(r * r + g * g + b * b);
    }
}