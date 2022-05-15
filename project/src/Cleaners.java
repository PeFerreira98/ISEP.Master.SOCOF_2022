import java.awt.Color;
import java.io.IOException;

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

                tmp[i][j] = Utils.choosePixel(p1, p2, p3);
            }
        }

        return tmp;
    }


}