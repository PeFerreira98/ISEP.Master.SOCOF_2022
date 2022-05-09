import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.awt.Color;

public class ApplyFilters {

    private static final int NFILES = 5;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

        // sequential();

        // executers1();
        
        executers2();

    }

    static void sequential() throws InterruptedException, ExecutionException, IOException {
        long timeStart = System.currentTimeMillis();

        Filters filters = new Filters();
        for (int i = 1; i < NFILES + 1; i++) {
            Color image1[][] = Utils.loadImage(String.valueOf(i) + ".jpg");
            Color image2[][] = filters.GrayScaleFilter(image1);
            Color image3[][] = filters.BrighterFilter(image2, 128);
            Utils.writeImage(image3, "tmp/" + String.valueOf(i) + "_processed1.jpg");
        }

        long timeEnd = System.currentTimeMillis();
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    static void executers1() throws InterruptedException, ExecutionException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(6);

        Long timeStart = System.currentTimeMillis();

        for (int i = 1; i < NFILES + 1; i++) {
            Color image1[][] = Utils.loadImage(String.valueOf(i) + ".jpg");

            Callable<Color[][]> worker = new ProcessGrayFilter(image1);
            Future<Color[][]> submit = executor.submit(worker);

            Callable<Color[][]> worker2 = new ProcessBrighterFilter(submit.get(), 128);
            Future<Color[][]> submit2 = executor.submit(worker2);

            Utils.writeImage(submit2.get(), "tmp2/" + String.valueOf(i) + "_processed2.jpg");
        }

        Long timeEnd = System.currentTimeMillis();
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");

        executor.shutdown();
    }

    static void executers2() throws InterruptedException, ExecutionException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        
        Long timeStart = System.currentTimeMillis();

        for (int i = 1; i < NFILES + 1; i++) {

            // Color image1[][] = Utils.loadImage(String.valueOf(i) + ".jpg");

            AtomicInteger atomicInteger = new AtomicInteger(i);
            CompletableFuture future = CompletableFuture
                .supplyAsync(() -> Utils.loadImage(String.valueOf(atomicInteger) + ".jpg"), executor)
                .thenApply(f -> new Filters().GrayScaleFilter(f))
                .thenApply(fn -> new Filters().BrighterFilter(fn, 128))
                .thenAccept(action -> Utils.writeImage(action, "tmp3/" + String.valueOf(atomicInteger) + "_processed3.jpg"));

            //Utils.writeImage(future.get(), "tmp/" + String.valueOf(i) + "_processed3.jpg");
        }

        Long timeEnd = System.currentTimeMillis();
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");

        executor.shutdown();
    }

    
}
