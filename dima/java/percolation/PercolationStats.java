package dima.java.percolation;


/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats 4 100
 *  Dependencies: edu.princeton.cs.algs4.StdRandom
 *
 *  Class of statistical evaluation of the Percolation Class.
 *
 *  % java PercolationStats 4 100
 *  n and tries               = 4 / 100
 *  mean                      = 0.60125
 *  stddev                    = 0.11087490676205752
 *  95% confidence interval =[0.5795185182746366, 0.6229814817253633]
 *
 ******************************************************************************/


import edu.princeton.cs.algs4.StdRandom;

/**
 * The {@code PercolationStats} class provides possibility to evaluate the statistical behaviour of
 * the Percolation class for a set of trials. Test client reads two integer numbers from standard
 * input, which are the grid size of the Percolation object and the number of trials to be executed.
 * It randomly opens sites until the system percolates and adds the results of opened sites to the x
 * array. After all trials have been successfully executed the mean, standard deviation and the 95%
 * confidence interval for x are calculated with the test client main function.
 *
 * @author Dumitru Hanciu
 */
public class PercolationStats {

    private int tries;
    private int gridWidth;

    private double[] x;

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    /**
     * Constructor for the PercolatesStats class. During construction it executes {@code trial}
     * number of tests and finally make the statistical calculations.
     *
     * @param n      size of the grid
     * @param trials number of tests to be executed for the statistical evaluation
     * @throws IllegalArgumentException if {@code n, trials} is {@code (n < 1) || (trials < 1)}
     */
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n < 1 || trials < 1)
            throw new IllegalArgumentException("n and trials should be bigger than zero.");

        tries = trials;
        gridWidth = n;

        x = new double[tries];

        for (int i = 0; i < tries; i++) {
            x[i] = ratioOpened();
        }

        mean = mean();
        stddev = stddev();
        confidenceLo = confidenceLo();
        confidenceHi = confidenceHi();
    }

    /**
     * Returns the ratio between the number of opened and total sites after the system has
     * percolated.
     *
     * @return a double representing percolation ratio
     */
    private double ratioOpened() {

        double ratioOpened;
        Percolation percolationObj = new Percolation(gridWidth);

        while (!percolationObj.percolates()) {
            int rand = StdRandom.uniform(gridWidth * gridWidth);
            percolationObj.open(rand / gridWidth + 1, rand % gridWidth + 1);
        }

        ratioOpened = (double) percolationObj.numberOfOpenSites() / (gridWidth * gridWidth);

        return ratioOpened;
    }

    /**
     * Calculates manually the mean value of x.
     *
     * @return mean value of x.
     */
    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum / x.length;
    }

    /**
     * Calculates manually the standard deviation value of x.
     *
     * @return stddev value of x.
     */
    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] - mean, 2);
        }
        return Math.sqrt(sum / (x.length - 1));
    }

    /**
     * Calculates the low confidence level value of x.
     *
     * @return confidenceLo value of x.
     */
    // sample low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(tries);
    }

    /**
     * Calculates the high confidence level value of x.
     *
     * @return confidenceHi value of x.
     */
    // sample high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(tries);
    }

    /**
     * Unit tests the methods in this class outputing resulting values to standard output.
     *
     * @param args the command-line arguments
     */
    // test client (described below)
    public static void main(String[] args) {
        int n, trial;

        n = Integer.parseInt(args[0]);
        trial = Integer.parseInt(args[1]);

        PercolationStats percObj = new PercolationStats(n, trial);

        System.out.println("n and tries               = " + n + " / " + trial);
        System.out.println("mean                      = " + percObj.mean);
        System.out.println("stddev                    = " + percObj.stddev);
        System.out.println("95% confidence interval =[" + percObj.confidenceLo + ", "
                                   + percObj.confidenceHi + "]");
    }
}

