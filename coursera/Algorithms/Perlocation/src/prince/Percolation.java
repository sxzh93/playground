package prince;
import java.lang.*;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int openSitesCnt;
	private int size;
	private boolean grid[][];
	private WeightedQuickUnionUF topUF, globalUF;
	private int virtualTopIdx, virtualBottomIdx;
	static private int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	public Percolation(int n) {
		// create n-by-n grid, with all sites blocked
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		this.openSitesCnt = 0;
		this.size = n;
		this.grid = new boolean[n + 1][n + 1];
		this.topUF = new WeightedQuickUnionUF(n * n + 1);
		this.globalUF = new WeightedQuickUnionUF(n * n + 2);
		this.virtualTopIdx = n*n;
		this.virtualBottomIdx = n*n+1;
	}

	private boolean inRange(int row, int col) {
		return row >= 1 && row <= this.size && col >= 1 && col <= this.size;
	}
	
	private boolean isTop(int row){
		return row == 1;
	}
	
	private boolean isBottom(int row){
		return row == this.size;
	}

	private int getIndex(int row, int col) {
		return (row - 1) * this.size + col - 1;
	}

	public void open(int row, int col){
		// open site (row, col) if it is not open already
		if(this.inRange(row, col)){
			if(!this.grid[row][col]){
				++this.openSitesCnt;
				this.grid[row][col] = true;
				int p = getIndex(row, col);
				if(this.isTop(row)){
					this.topUF.union(p, this.virtualTopIdx);
					this.globalUF.union(p, this.virtualTopIdx);
				}
				if(this.isBottom(row)){
					this.globalUF.union(p, this.virtualBottomIdx);
				}
				for(int i=0;i<dir.length;++i){
					int curx = row + dir[i][0];
					int cury = col + dir[i][1];
					if(this.inRange(curx, cury) && this.isOpen(curx, cury)){
						int q = this.getIndex(curx, cury);
						if(!this.topUF.connected(p, q)){
							this.topUF.union(p, q);
							this.globalUF.union(p, q);
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
			return this.topUF.connected(this.getIndex(row, col), this.virtualTopIdx);
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
		return this.globalUF.connected(this.virtualTopIdx, this.virtualBottomIdx);
	}

	public static void main(String[] args) {
		// test client (optional)
	}
}