import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        // sequentialHighlightFires();
        // multiHighlightFires();
        // poolHighlightFires();

        sequentialImageCleaning();
        multiImageCleaning();
        poolImageCleaning();
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
        System.out.println("\nFinished Sequential Highlight Fires...");
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

            threads[i - 1] = new Thread(filters);
            threads[i - 1].start();
        }

        for (int i = 0; i < filenumber; i++) {
            threads[i].join();
        }
        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nFinished MultiThread Highlight Fires...");
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
        System.out.println("\nFinished PoolThread Highlight Fires...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void sequentialImageCleaning() throws IOException {
        String filename1 = "CleanImage/clean1.jpg";
        String filename2 = "CleanImage/clean2.jpg";
        String filename3 = "CleanImage/clean3.jpg";
        String outputFilename = "seq2/cleanedImage.jpg";

        System.out.println("Starting Sequential Image Cleaning...");
        Long timeStart = System.currentTimeMillis();

        Cleaners cleaners = new Cleaners(filename1, filename2, filename3, outputFilename);
        cleaners.CleanImage();

        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nFinished Sequential Image Clean...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void multiImageCleaning() throws IOException, InterruptedException {
        String filename1 = "CleanImage/clean1.jpg";
        String filename2 = "CleanImage/clean2.jpg";
        String filename3 = "CleanImage/clean3.jpg";
        String outputFilename = "thrd2/cleanedImage.jpg";

        System.out.println("Starting MultiThread Image Cleaning...");
        Long timeStart = System.currentTimeMillis();

        Cleaners cleaners = new Cleaners(filename1, filename2, filename3, outputFilename);
        var thread = new Thread(cleaners);
        thread.start();

        thread.join();

        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nFinished MultiThread Image Cleaning...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void poolImageCleaning() throws IOException, InterruptedException {
        String filename1 = "CleanImage/clean1.jpg";
        String filename2 = "CleanImage/clean2.jpg";
        String filename3 = "CleanImage/clean3.jpg";
        String outputFilename = "para2/cleanedImage.jpg";
        List<String> filenames = List.of(filename1, filename2, filename3);

        int threadNumber = 6;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        System.out.println("Starting PoolThread Image Cleaning...");
        Long timeStart = System.currentTimeMillis();

        // Async load images (Task[] in C#)
        var futures = filenames.stream()
            .map(f -> CompletableFuture.supplyAsync(() -> Utils.loadImage(f), executor))
            .collect(Collectors.toList()); 

        // Wait for all images to be loaded (Task[].WaitAll() in C#)
        var allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        
        // non block join then process images (await Task then apply action in C#)
        allFutures
            .thenApply(v -> {
            return futures.stream()
                .map(f -> f.join())
                .collect(Collectors.toList());
            })
            .thenApply(c -> Cleaners.CleanImage(c.get(0), c.get(1), c.get(2)))
            .thenAccept(v -> Utils.writeImage(v, outputFilename));

        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);

        Long timeEnd = System.currentTimeMillis();
        System.out.println("\nFinished PoolThread Image Clean...");
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }
}
