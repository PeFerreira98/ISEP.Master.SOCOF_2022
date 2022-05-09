
import java.awt.Color;

public class Filters {

    // Constructor with filename for source image
    Filters() {

    }

    // Grayscale filter works bya averaging every pixel red, green and blue values.
    public Color[][] GrayScaleFilter(Color[][] tmp) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // fetches values of each pixel
                Color pixel = tmp[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                // takes average of color values
                int grayNum = (r + g + b) / 3;
                // outputs average into picuture to make grayscale
                tmp[i][j] = new Color(grayNum, grayNum, grayNum);

            }
        }
        return tmp;
    }

    // Swirl filter makes a distorion on the image by applying a rotation
    // to the pixels.
    public Color[][] SwirlFilter(Color[][] tmp) {
        Color[][] tmp2 = Utils.copyImage(tmp);
        int height = tmp[0].length;
        int width = tmp.length;

        double x0 = 0.5 * (width - 1);
        double y0 = 0.5 * (height - 1);

        // swirl
        for (int sx = 0; sx < width; sx++) {
            for (int sy = 0; sy < height; sy++) {
                double dx = sx - x0;
                double dy = sy - y0;
                double r = Math.sqrt(dx * dx + dy * dy);
                double angle = Math.PI / 256 * r;
                int tx = (int) (+dx * Math.cos(angle) - dy * Math.sin(angle) + x0);
                int ty = (int) (+dx * Math.sin(angle) + dy * Math.cos(angle) + y0);

                // plot pixel (sx, sy) the same color as (tx, ty) if it's in bounds
                if (tx >= 0 && tx < width && ty >= 0 && ty < height)
                    tmp[sx][sy] = new Color(tmp2[tx][ty].getRed(), tmp2[tx][ty].getGreen(), tmp2[tx][ty].getBlue());
            }
        }

        return tmp;
    }

    public Color[][] BrighterFilter(Color[][] tmp, int value) {

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // fetches values of each pixel
                Color pixel = tmp[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                // takes average of color values
                int bright = value;
                if (r + bright > 255)
                    r = 255;
                else
                    r = r + bright;
                if (g + bright > 255)
                    g = 255;
                else
                    g = g + bright;
                if (b + bright > 255)
                    b = 255;
                else
                    b = b + bright;
                tmp[i][j] = new Color(r, g, b);

            }
        }
        return tmp;
    }

}
