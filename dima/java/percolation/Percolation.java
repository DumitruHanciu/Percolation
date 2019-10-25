package dima.java.percolation;

/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Date: 15.12.2018
 *  Description: Percolation
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int openedSites;
    private byte[] info; // closed 0 .. open 1 .. full 2 .. bottom 3 .. full and bottom 4
    private int grid;
    private boolean hasPercolated;
    private WeightedQuickUnionUF wqufObj;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {

        if (n < 1) throw new IllegalArgumentException();

        grid = n;
        info = new byte[grid * grid];
        openedSites = 0;
        hasPercolated = false;
        wqufObj = new WeightedQuickUnionUF(grid * grid);
    }

    // helper function used to convert row / col indicies to 1D index value
    private int getRootIndex(int row, int col) {

        int index = coordToIndex(row, col);
        int rootIndex = wqufObj.find(index);

        return rootIndex;
    }

    // helper function for conversion row / col to 1d index value
    private int coordToIndex(int rowIndex, int colIndex) {

        return (rowIndex - 1) * grid + colIndex - 1;
    }

    // helper function used to check if site is at bottom row
    private boolean isBottom(int row, int col) {

        int rootIndex = getRootIndex(row, col);

        return info[rootIndex] == 3 || info[rootIndex] == 4;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {

        if (row <= 0 || row > grid || col <= 0 || col > grid)
            throw new IllegalArgumentException();

        int index = coordToIndex(row, col);

        return info[index] != 0;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {

        if (row <= 0 || row > grid || col <= 0 || col > grid)
            throw new IllegalArgumentException();

        int rootIndex = getRootIndex(row, col);

        return info[rootIndex] == 2 || info[rootIndex] == 4;
    }

    // number of open sites
    public int numberOfOpenSites() {

        return openedSites;
    }

    // does the system percolate?
    public boolean percolates() {

        return hasPercolated;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {

        if (row <= 0 || row > grid || col <= 0 || col > grid)
            throw new IllegalArgumentException();

        boolean full = false, bottom = false;
        int index = coordToIndex(row, col);

        if (!isOpen(row, col)) {
            info[index] = 1;
            if (row == 1) full = true;
            if (row == grid) bottom = true;

            // following ifs will check connection to neighboring sites which could be full / bottom
            // case point index is not in last row
            if (row < grid && isOpen(row + 1, col) && !wqufObj.connected(index, index + grid)) {

                if (isFull(row + 1, col) || isFull(row, col)) full = true;
                if (isBottom(row + 1, col) || isBottom(row, col)) bottom = true;

                wqufObj.union(index, index + grid);
            }


            // case point index is not in first row
            if (row > 1 && isOpen(row - 1, col) && !wqufObj.connected(index, index - grid)) {

                if (isFull(row - 1, col) || isFull(row, col)) full = true;
                if (isBottom(row - 1, col) || isBottom(row, col)) bottom = true;

                wqufObj.union(index, index - grid);
            }

            // case point index is not in last column
            if (col < grid && isOpen(row, col + 1) && !wqufObj.connected(index, index + 1)) {

                if (isFull(row, col + 1) || isFull(row, col)) full = true;
                if (isBottom(row, col + 1) || isBottom(row, col)) bottom = true;

                wqufObj.union(index, index + 1);
            }

            // case point index is not in first column
            if (col > 1 && isOpen(row, col - 1) && !wqufObj.connected(index, index - 1)) {

                if (isFull(row, col - 1) || isFull(row, col)) full = true;
                if (isBottom(row, col - 1) || isBottom(row, col)) bottom = true;

                wqufObj.union(index, index - 1);
            }

            // check if grid percolated and set root to full or bottom
            int rootIndex = wqufObj.find(index);
            if (full && bottom) {
                info[rootIndex] = 4;
                hasPercolated = true;
            }
            else if (full)
                info[rootIndex] = 2;
            else if (bottom)
                info[rootIndex] = 3;
            openedSites += 1;
        }
    }

    // test client
    public static void main(String[] args) {

    }
}
