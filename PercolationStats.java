import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int experimentsCount;
    private static double[] fractions;

    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("Given N <= 0 || T <= 0");
        }
        experimentsCount = t;
        fractions = new double[experimentsCount];
        for (int expNum = 0; expNum < experimentsCount; expNum++) {
            Percolation pr = new Percolation(n);
            int openedSites = 0;
            while (!pr.percolates()) {
                int i = ThreadLocalRandom.current().nextInt(1, n + 1);
                int j = ThreadLocalRandom.current().nextInt(1, n + 1);
                if (!pr.isOpen(i, j)) {
                    pr.open(i, j);
                    openedSites++;
                }
            }
            double fraction = (double) openedSites / (n * n);
            fractions[expNum] = fraction;
        }

    }


    public static double mean(double[] arr) {
        return Arrays.stream(arr).average().orElse(Double.NaN);
    }


    public static double stddev(double[] arr) {
        double mean = mean(arr);
        double variance = Arrays.stream(arr)
                                .map(x -> (x - mean) * (x - mean))
                                .average()
                                .orElse(Double.NaN);
        return Math.sqrt(variance);
    }


    public double confidenceLo() {
        return mean(fractions) - ((CONFIDENCE_95 * stddev(fractions)) / Math.sqrt(
                experimentsCount));
    }


    public double confidenceHi() {
        return mean(fractions) + ((CONFIDENCE_95 * stddev(fractions)) / Math.sqrt(
                experimentsCount));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        long start = System.nanoTime();
        PercolationStats ps = new PercolationStats(n, t);
        double elapsed = (System.nanoTime() - start) / 1.0e9;
        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        System.out.println("mean                    = " + mean(fractions));
        System.out.println("stddev                  = " + stddev(fractions));
        System.out.println("95% confidence interval = " + confidence);
        System.out.println("Elapsed time            = " + elapsed + " seconds");
    }
}
