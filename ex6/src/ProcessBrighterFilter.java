import java.util.concurrent.Callable;

import java.awt.Color;

public class ProcessBrighterFilter implements Callable<Color[][]> {
    private Color[][] image;
    private int brightness;

    public ProcessBrighterFilter(Color[][] image, int brightness) {
        this.image = image;
        this.brightness = brightness;
    }

    @Override
    public Color[][] call() throws Exception {
        return applyFilter(this.image);
    }

    private Color[][] applyFilter(Color tmp[][]) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                Color pixel = tmp[i][j];
                tmp[i][j] = brighterUp(pixel);
            }
        }
        return tmp;
    }

    private Color brighterUp(Color pixel) {
        int red = pixel.getRed();
        int green = pixel.getGreen();
        int blue = pixel.getBlue();

        if (red + brightness > 255) {
            red = 255;
        } else {
            red += brightness;
        }
        if (green + brightness > 255) {
            green = 255;
        } else {
            green += brightness;
        }
        if (blue + brightness > 255) {
            blue = 255;
        } else {
            blue += brightness;
        }
        return new Color(red, green, blue);
    }
}
