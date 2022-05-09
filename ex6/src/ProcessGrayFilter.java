import java.util.concurrent.Callable;

import java.awt.Color;

public class ProcessGrayFilter implements Callable<Color[][]> {
    private Color[][] image;

    public ProcessGrayFilter(Color[][] image) {
        this.image = image;
    }

    @Override
    public Color[][] call() throws Exception {
        return applyFilter(this.image);
    }

    private Color[][] applyFilter(Color tmp[][]) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                Color pixel = tmp[i][j];
                tmp[i][j] = grayOut(pixel);
            }
        }
        return tmp;
    }

    private Color grayOut(Color pixel) {
        int red = pixel.getRed();
        int green = pixel.getGreen();
        int blue = pixel.getBlue();
        int gray = (red + green + blue) / 3;
        return new Color(gray, gray, gray);
    }
}
