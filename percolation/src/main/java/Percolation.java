import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * model of a percolation system including WeightedQuickUnionUF from algs4.jar
 * 
 * @author egunay
 *
 */
public class Percolation {
    
    private static final byte CLOSED = Byte.valueOf("0001", 2);
    private static final byte OPEN = Byte.valueOf("0010", 2);
    private static final byte CONNECTED_TOP = Byte.valueOf("0100", 2);
    private static final byte CONNECTED_BOTTOM = Byte.valueOf("1000", 2);
    

    private WeightedQuickUnionUF uf;

    private int n;
    private byte[] siteStates;
    private int openSiteCount;
    private boolean percolated = false;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        int length = n * n;
        this.n = n;

        uf = new WeightedQuickUnionUF(length);

        siteStates = new byte[length];
        for (int i = 0; i < length; i++)
            siteStates[i] = CLOSED;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int p = getIndex(row, col);

        if (!isOpen(row, col)) {
            siteStates[p] = (byte) (siteStates[p] | OPEN);
            openSiteCount++;
        }

        int root = uf.find(p);
        if (row == 1)
            siteStates[root] = (byte) (siteStates[root] | CONNECTED_TOP);
        if (row == n)
            siteStates[root] = (byte) (siteStates[root] | CONNECTED_BOTTOM);
        
        // check the neighbours and make union with open neighbours
        checkNeighbour(row, col, row, col+1);
        checkNeighbour(row, col, row, col-1);
        checkNeighbour(row, col, row+1, col);
        checkNeighbour(row, col, row-1, col);
        
        root = uf.find(p);
        if ((siteStates[root] & CONNECTED_TOP) == CONNECTED_TOP && (siteStates[root] & CONNECTED_BOTTOM) == CONNECTED_BOTTOM)
            percolated = true;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return (siteStates[getIndex(row, col)] & OPEN) == OPEN;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        int p = getIndex(row, col);
        int root = uf.find(p);
        
        return (siteStates[root] & CONNECTED_TOP) == CONNECTED_TOP;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolated;
    }

    /**
     * connect the opening site to the open neighbors.
     * backwash is prevented by connecting the bottom line sites to virtual bottom site only if the opening site percolates. 
     * 
     * @param row
     * @param col
     * @param neighbourRow
     * @param neighbourCol
     */
    private void checkNeighbour(int row, int col, int neighbourRow, int neighbourCol) {
        if (neighbourRow <= 0 || neighbourRow > n || neighbourCol <= 0 || neighbourCol > n)
            return;
        
        int p = getIndex(row, col);
        int q = getIndex(neighbourRow, neighbourCol);
        
        if (isOpen(neighbourRow, neighbourCol)) {
            int rootP = uf.find(p);
            int rootQ = uf.find(q);
            uf.union(p, q);
            
            siteStates[rootP] = (byte) (siteStates[rootP] | siteStates[rootQ]);
            siteStates[rootQ] = (byte) (siteStates[rootP] | siteStates[rootQ]);
        } 
    }

    /**
     * find the index of the site by row - col
     * 
     * @param row
     * @param col
     * @return
     */
    private int getIndex(int row, int col) {
        row = validateAndAdjustIndex(row);
        col = validateAndAdjustIndex(col);

        return row * n + col;
    }

    private int validateAndAdjustIndex(int p) {
        if (p < 1 || p > n)
            throw new IndexOutOfBoundsException();

        return p - 1;
    }

    // test client (optional)
    public static void main(String[] args) {

        Percolation percolation = new Percolation(5);

        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(2, 1);
        System.out.println(percolation.numberOfOpenSites());
        
        percolation.open(2, 2);
        percolation.open(2, 5);
        percolation.open(3, 2);
        percolation.open(4, 2);
        System.out.println(percolation.percolates());
        percolation.open(5, 2);

        System.out.println(percolation.percolates());
    }
}
