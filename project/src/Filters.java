import java.io.*;
import java.awt.Color;

/**
 * Creating image filters
 * 
 * @author Jorge Coelho
 * @contact jmn@isep.ipp.pt
 * @version 1.0
 * @since 2022-01-04
 */
public class Filters implements Runnable{

    String filename;
    float threshold;
    String outputFilename;

    Filters(String filename, float threshold, String outputFilename) {
        this.filename = filename;
        this.threshold = threshold;
        this.outputFilename = outputFilename;
    }

    public void run() {
        try {
            HighLightFireFilter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Highlight Fires.
    public void HighLightFireFilter() throws IOException {
        var image = Utils.loadImage(filename);

        HighLightFire(image, threshold);

        Utils.writeImage(image, outputFilename);
    }

    public static Color[][] HighLightFire(Color[][] tmp, float threshold) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                tmp[i][j] = Utils.RedFilter(tmp[i][j], threshold);
            }

            if (i % 100 == 0)
                System.out.print(".");
        }
        return tmp;
    }
}
