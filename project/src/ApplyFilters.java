import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.awt.Color;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        // sequentialHighlightFires();

        // parallelHighlightFires();

        sequentialImageCleaning();

        parallelImageCleaning();
    }

    static void sequentialHighlightFires() throws IOException {
        // Scanner input = new Scanner(System.in);
        // String filePath = "";
        // System.out.println("Insert the name of the file path you would like to
        // use:");
        // filePath = input.nextLine();
        // System.out.println("Insert the red value threshold:");
        // float threshold = input.nextFloat();
        // input.close();

        int filenumber = 3;
        float threshold = 1.35f;

        System.out.println("Starting Sequential Highlight Fires...");
        Long timeStart = System.currentTimeMillis();

        for (int i = 2; i <= filenumber; i++) {
            Filters filters = new Filters("HighlightFires/russia" + String.valueOf(new AtomicInteger(i)) + ".jpg");
            filters.HighLightFireFilter("seq1/russia" + String.valueOf(new AtomicInteger(i)) + ".jpg", threshold);
        }

        Long timeEnd = System.currentTimeMillis();
        System.out.println("Sequential Highlight Fires Finished...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void parallelHighlightFires() throws IOException {
        int threadNumber = 6;
        int filenumber = 3;
        float threshold = 1.35f;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        System.out.println("Starting Parallel Highlight Fires...");
        Long timeStart = System.currentTimeMillis();

        for (int i = 1; i <= filenumber; i++) {
            AtomicInteger atomicInteger = new AtomicInteger(i);

            CompletableFuture
                    .supplyAsync(
                            () -> Utils.loadImage("HighlightFires/russia" + String.valueOf(atomicInteger) + ".jpg"),
                            executor)
                    .thenApply(f -> new Filters().HighLightFire(f, threshold))
                    .thenAccept(action -> Utils.writeImage(action,
                            "para1/russia" + String.valueOf(atomicInteger) + ".jpg"));
        }

        executor.shutdown();

        Long timeEnd = System.currentTimeMillis();
        System.out.println("Parallel Highlight Fires Finished...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void sequentialImageCleaning() throws IOException {
        String filename1 = "CleanImage/clean1.jpg";
        String filename2 = "CleanImage/clean2.jpg";
        String filename3 = "CleanImage/clean3.jpg";
        String outputFilename = "seq2/cleanedImage.jpg";
        Cleaners cleaners = new Cleaners(filename1, filename2, filename3);

        System.out.println("Starting Sequential Image Cleaning...");
        Long timeStart = System.currentTimeMillis();

        cleaners.CleanImage(outputFilename);

        Long timeEnd = System.currentTimeMillis();
        System.out.println("Sequential Image Clean Finished...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void parallelImageCleaning() throws IOException, InterruptedException, ExecutionException {
        String filename1 = "CleanImage/clean1.jpg";
        String filename2 = "CleanImage/clean2.jpg";
        String filename3 = "CleanImage/clean3.jpg";
        String outputFilename = "para2/cleanedImage.jpg";

        int threadNumber = 6;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        System.out.println("Starting Parallel Image Cleaning...");
        Long timeStart = System.currentTimeMillis();

        CompletableFuture
                .supplyAsync(() -> new Cleaners(filename1, filename2, filename3), executor)
                .thenApply(f -> f.cleanImage())
                .thenAccept(action -> Utils.writeImage(action, outputFilename))
                .get();

        executor.shutdown();

        Long timeEnd = System.currentTimeMillis();
        System.out.println("Parallel Image Clean Finished...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }
}
