package prince;
import java.lang.*;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int openSitesCnt;
	private int size;
	private boolean grid[][];
	private boolean isFullSite[][];
	private boolean isPercolates;
	private WeightedQuickUnionUF uf;
	static private int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	public Percolation(int n) {
		// create n-by-n grid, with all sites blocked
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		this.openSitesCnt = 0;
		this.size = n;
		this.grid = new boolean[n + 1][n + 1];
		this.isFullSite = new boolean[n + 1][n + 1];
		this.uf = new WeightedQuickUnionUF(n * n);
		this.isPercolates = false;
	}

	private boolean inRange(int row, int col) {
		return row >= 1 && row <= this.size && col >= 1 && col <= this.size;
	}
	
	private boolean isTop(int row){
		return row == 1;
	}
	
	private boolean isBottom(int col){
		return col == this.size;
	}

	private int getIndex(int row, int col) {
		return (row - 1) * this.size + col - 1;
	}
	
	private void resolveIndex(int idx, int[] pair){
		pair[0] = idx / this.size + 1;
		pair[1] = (idx % this.size) + 1;
	}
	
	private void markAsFull(int row, int col){
		this.isFullSite[row][col] = true;
		if(this.isBottom(col)){
			this.isPercolates = true;
		}
	}

	public void open(int row, int col){
		// open site (row, col) if it is not open already
		if(this.inRange(row, col)){
			if(!this.grid[row][col]){
				++this.openSitesCnt;
				this.grid[row][col] = true;
				if(this.isTop(row)){
					this.markAsFull(row, col);
				}
				int p = getIndex(row, col);
				for(int i=0;i<dir.length;++i){
					int curx = row + dir[i][0];
					int cury = col + dir[i][1];
					if(inRange(curx, cury) && this.isOpen(curx, cury)){
						int q = getIndex(curx, cury);
						if(!this.uf.connected(p, q)){
							this.uf.union(p, q);
							if(this.isTop(row) || this.isTop(curx)){
								int newroot = this.uf.find(p);
								int[] pair = {0, 0};
								this.resolveIndex(newroot, pair);
								this.markAsFull(pair[0], pair[1]);
							}
						}
					}
				}
			}
		}else{
			throw new IndexOutOfBoundsException();
		}
	}

	public boolean isOpen(int row, int col) {
		// is site (row, col) open?
		if (this.inRange(row, col)) {
			return this.grid[row][col];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public boolean isFull(int row, int col) {
		// is site (row, col) full?
		if (this.inRange(row, col)) {
			return this.isFullSite[row][col];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public int numberOfOpenSites() {
		// number of open sites
		return this.openSitesCnt;
	}

	public boolean percolates() {
		// does the system percolate?
		return this.isPercolates;
	}

	public static void main(String[] args) {
		// test client (optional)
	}
}