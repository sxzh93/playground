import java.lang.*;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation{
	private int openSitesCnt;
	private int size;
	private boolean grid[][];
	
	public Percolation(int n){
		// create n-by-n grid, with all sites blocked
		if(n<=0){
			throw new IllegalArgumentException();
		}
		this.openSitesCnt = 0;
		this.size = n;
		this.grid = new boolean[n+1][n+1];
	}
	
	private boolean inRange(int row, int col){
		return row>=1 && row<=size && col>=1 && col<=size;
	}
	
	public void open(int row, int col){
		// open site (row, col) if it is not open already
		if(this.inRange(row, col)){
			if(!this.grid[row][col]){
				++this.openSitesCnt;
			}
			this.grid[row][col] = true;
		}else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public boolean isOpen(int row, int col){
		// is site (row, col) open?
		if(this.inRange(row, col)){
			return this.grid[row][col];
		}else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public boolean isFull(int row, int col){
		// is site (row, col) full?
		if(this.inRange(row, col)){
			
		}else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public int numberOfOpenSites(){
		// number of open sites
		return this.openSitesCnt;
	}
	
	public boolean percolates(){
		// does the system percolate?
		for(int i=1;i<=size;++i){
			if(isFull(size, i)){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args){
		// test client (optional)
	}
}
