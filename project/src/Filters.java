
import java.awt.Color;
import java.io.IOException;

import javax.rmi.CORBA.Util;

/**
 * Creating image filters
 * 
 * @author Jorge Coelho
 * @contact jmn@isep.ipp.pt
 * @version 1.0
 * @since 2022-01-04
 */
public class Filters {

    String file;
    Color image[][];

    // Constructor with filename for source image
    Filters(String filename) {
        this.file = filename;
        image = Utils.loadImage(filename);
    }

    Filters(){
        this.file = "";
        image = null;
    }

    // Highlight Fires.
    public void HighLightFireFilter(String outputFile, float threshold) throws IOException {
        Color[][] tmp = Utils.copyImage(image);

        System.out.println("Pixel image size " + tmp.length + "," + tmp[0].length);

        this.HighLightFire(tmp, threshold);

        System.out.println("Starting file write...");
        Utils.writeImage(tmp, outputFile);
    }

    public Color[][] HighLightFire(Color[][] tmp, float threshold) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                Color pixel = tmp[i][j];
                tmp[i][j] = Utils.RedFilter(pixel, threshold);

            }

            if (i % 100 == 0)
                System.out.print(".");
        }
        return tmp;
    }
}
