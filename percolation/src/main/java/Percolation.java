import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * 
 * @author egunay
 *
 */
public class Percolation {
	
	private WeightedQuickUnionUF weightedQuickUnionUF;
	private int n;
	private boolean[] siteStates;
	private int openSiteCount;
	
	private int topLineIndex;
	private int bottomLineIndex;
	
	
	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		
		int N = n * n;
		this.n = n;
		this.topLineIndex = N;
		this.bottomLineIndex = N + 1;
		weightedQuickUnionUF = new WeightedQuickUnionUF(N + 2);
		siteStates = new boolean[N];
		
		// link the top line to virtual node
		for (int i = 0; i < n; i++) 
			weightedQuickUnionUF.union(topLineIndex, i);
		
		// link the bottom line to virtual node
		for (int i = N - n -1; i < N; i++)
			weightedQuickUnionUF.union(bottomLineIndex, i);
	}

	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		int p = getIndex(row, col);

		siteStates[p] = true;
		openSiteCount++;
		
		// check the neighbours and make union with open neighbours
		int[] neighbourSites = new int[] {p % n == 0 ? -1 : p-1, p%n == n-1 ? -1 : p+1, p-n, p+n};
		for (int i = 0; i < neighbourSites.length; i++) {
			int q = neighbourSites[i];
			if (q < 0 || q >= n*n)
				continue;
			
			if (siteStates[q]) {
				weightedQuickUnionUF.union(p, q);
			}
		}
		
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		return siteStates[getIndex(row, col)];
	}

	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		int p = getIndex(row, col);
		return siteStates[p] && weightedQuickUnionUF.connected(topLineIndex, p);
	}

	// number of open sites
	public int numberOfOpenSites() {
		return openSiteCount;
	}

	// does the system percolate?
	public boolean percolates() {
		return weightedQuickUnionUF.connected(topLineIndex, bottomLineIndex);
	}

	private int getIndex(int row, int col) {
		row = validateAndAdjustIndex(row);
		col = validateAndAdjustIndex(col);

		return row * n + col;
	}
	
	private int validateAndAdjustIndex(int p) {
		if (p < 1 || p > n)
			throw new IndexOutOfBoundsException();
		
		return p-1;
	}
	
	// test client (optional)
	public static void main(String[] args) {

		Percolation percolation = new Percolation(5);
		
		percolation.open(1, 1);
		percolation.open(2, 1);
		percolation.open(2, 2);
		percolation.open(2, 5);
		percolation.open(3, 2);
		percolation.open(4, 2);
		System.out.println(percolation.percolates());
		percolation.open(5, 2);
		
		System.out.println(percolation.percolates());
	}
}
