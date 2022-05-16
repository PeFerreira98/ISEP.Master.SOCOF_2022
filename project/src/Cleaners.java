import java.awt.Color;
import java.io.IOException;

public class Cleaners implements Runnable{
    String filename1;
    String filename2;
    String filename3;
    String outputFilename;

    Cleaners(String filename1, String filename2, String filename3, String outputFilename) {
        this.filename1 = filename1;
        this.filename2 = filename2;
        this.filename3 = filename3;
        this.outputFilename = outputFilename;
    }

    public void run() {
        try {
            CleanImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CleanImage() throws IOException {
        var image1 = Utils.loadImage(filename1);
        var image2 = Utils.loadImage(filename2);
        var image3 = Utils.loadImage(filename3);

        var tmp = CleanImage(image1, image2, image3);

        Utils.writeImage(tmp, outputFilename);
    }

    public static Color[][] CleanImage(Color[][] image1, Color[][] image2, Color[][] image3) {
        Color[][] tmp = new Color[image1.length][image1[0].length];

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                Color p1 = image1[i][j];
                Color p2 = image2[i][j];
                Color p3 = image3[i][j];
                tmp[i][j] = Utils.choosePixel(p1, p2, p3);
            }

            if (i % 100 == 0)
                System.out.print(".");
        }

        return tmp;
    }
}