import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Performs a series of computational experiments for calculating the threshold,
 * sample mean of percolation threshold, sample standard deviation of
 * percolation threshold low endpoint of 95% confidence interval high endpoint
 * of 95% confidence interval
 * 
 * @author egunay
 *
 */
public class PercolationStats {
    private int n;
    private int trials;

    private double[] x;
    private double threshold;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        this.trials = trials;
        this.x = new double[trials];
        
        double sumX = 0;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                if (percolation.isOpen(row, col))
                    continue;
                percolation.open(row, col);
            }

            x[i] = (double) percolation.numberOfOpenSites() / (n * n);
            sumX += x[i];
        }

        threshold = sumX / trials;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(x);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (n == 1)
            return Double.NaN;

        return StdStats.stddev(x);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return threshold - (1.96 * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return threshold + (1.96 * stddev() / Math.sqrt(trials));
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.printf("mean                    = %f %n", percolationStats.mean());
        System.out.printf("stddev                  = %f %n", percolationStats.stddev());
        System.out.printf("95%% confidence interval = %f, %f %n", percolationStats.confidenceLo(),
                percolationStats.confidenceHi());
    }
}
