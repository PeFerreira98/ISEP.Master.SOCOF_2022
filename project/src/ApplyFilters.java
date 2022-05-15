import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        sequentialHighlightFires();
        multiHighlightFires();
        poolHighlightFires();

        // sequentialImageCleaning();
        // multiImageCleaning();
        // poolImageCleaning();
    }

    static void sequentialHighlightFires() throws IOException {
        int filenumber = 3;
        float threshold = 1.35f;

        System.out.println("Starting Sequential Highlight Fires...");
        Long timeStart = System.currentTimeMillis();

        for (int i = 1; i <= filenumber; i++) {
            var inputFile = "HighlightFires/russia" + String.valueOf(new AtomicInteger(i)) + ".jpg";
            var outputFile = "seq1/russia" + String.valueOf(new AtomicInteger(i)) + "_highlight.jpg";
            var filters = new Filters(inputFile, threshold, outputFile);
            filters.HighLightFireFilter();
        }

        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nSequential Highlight Fires Finished...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void multiHighlightFires() throws IOException, InterruptedException {
        int filenumber = 3;
        float threshold = 1.35f;
        Thread[] threads = new Thread[filenumber];    

        System.out.println("Starting MultiThread Highlight Fires...");
        Long timeStart = System.currentTimeMillis();

        for (int i = 1; i <= filenumber; i++) {
            var inputFile = "HighlightFires/russia" + String.valueOf(new AtomicInteger(i)) + ".jpg";
            var outputFile = "thrd1/russia" + String.valueOf(new AtomicInteger(i)) + "_highlight.jpg";
            var filters = new Filters(inputFile, threshold, outputFile);

            threads[i-1] = new Thread(filters);
            threads[i-1].start();
        }

        for (int i = 0; i < filenumber; i++) {
            threads[i].join();
        }
        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nMultiThread Highlight Fires Finished...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void poolHighlightFires() throws IOException, InterruptedException {
        int threadNumber = 6;
        int filenumber = 3;
        float threshold = 1.35f;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        System.out.println("Starting PoolThread Highlight Fires...");
        Long timeStart = System.currentTimeMillis();

        for (int i = 1; i <= filenumber; i++) {
            var inputFile = "HighlightFires/russia" + String.valueOf(new AtomicInteger(i)) + ".jpg";
            var outputFile = "thrd1/russia" + String.valueOf(new AtomicInteger(i)) + "_highlight.jpg";

            CompletableFuture
                    .supplyAsync(() -> Utils.loadImage(inputFile), executor)
                    .thenApply(f -> Filters.HighLightFire(f, threshold))
                    .thenAccept(action -> Utils.writeImage(action, outputFile));
        }

        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
        
        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nPoolThread Highlight Fires Finished...");
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
