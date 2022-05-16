import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Utils {

  Utils() {
  }

  /**
   * Loads image from filename into a Color (pixels decribed with rgb values)
   * matrix.
   * 
   * @param filename the name of the imge in the filesystem.
   * @return Color matrix.
   */
  public static Color[][] loadImage(String filename) {
    BufferedImage buffImg = loadImageFile(filename);
    Color[][] colorImg = convertTo2DFromBuffered(buffImg);
    return colorImg;
  }

  /**
   * Converts image from a Color matrix to a .jpg file.
   * 
   * @param image    the matrix of Color objects.
   * @param filename to the image.
   */
  public static void writeImage(Color[][] image, String filename) {
    File outputfile = new File(filename);
    BufferedImage bufferedImage = Utils.matrixToBuffered(image);
    try {
      ImageIO.write(bufferedImage, "jpg", outputfile);
    } catch (IOException e) {
      System.out.println("Could not write image " + filename + " !");
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Loads in a BufferedImage from the specified path to be processed.
   * 
   * @param filename The path to the file to read.
   * @return a BufferedImage if able to be read, NULL otherwise.
   */
  private static BufferedImage loadImageFile(String filename) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File(filename));
    } catch (IOException e) {
      System.out.println("Could not load image " + filename + " !");
      e.printStackTrace();
      System.exit(1);
    }
    return img;
  }

  /**
   * Copy a Color matrix to another Color matrix.
   * Useful if one does not want to modify the original image.
   * 
   * @param image the source matrix
   * @return a copy of the image
   */
  public static Color[][] copyImage(Color[][] image) {
    Color[][] copy = new Color[image.length][image[0].length];
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[i].length; j++) {
        copy[i][j] = image[i][j];
      }
    }
    return copy;
  }

  /**
   * Converts a matrix of Colors into a BufferedImage to
   * write on the filesystem.
   * 
   * @param image the matrix of Colors
   * @return the image ready for writing to filesystem
   */
  private static BufferedImage matrixToBuffered(Color[][] image) {
    int width = image.length;
    int height = image[0].length;
    BufferedImage bImg = new BufferedImage(width, height, 1);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        bImg.setRGB(x, y, image[x][y].getRGB());
      }
    }
    return bImg;
  }

  /**
   * Converts a file loaded into a BufferedImage to a
   * matrix of Colors
   * 
   * @param image the BufferedImage to convert
   * @return the matrix of Colors
   */
  private static Color[][] convertTo2DFromBuffered(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    Color[][] result = new Color[width][height];

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        // Get the integer RGB, and separate it into individual components.
        // (BufferedImage saves RGB as a single integer value).
        int pixel = image.getRGB(x, y);
        // int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        result[x][y] = new Color(red, green, blue);
      }
    }
    return result;
  }

  public static Color redFilter(Color pixel, float threshold) {
    // fetches values of each pixel
    int r = pixel.getRed();
    int g = pixel.getGreen();
    int b = pixel.getBlue();
    // takes average of color values
    int avg = (r + g + b) / 3;
    if (r > avg * threshold && g < 100 && b < 200)
      // outputs red pixel
      return new Color(255, 0, 0);
    else
      return new Color(avg, avg, avg);
  }

  public static Color choosePixel(Color s1, Color s2, Color s3) {
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