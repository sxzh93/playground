package prince;
import java.lang.*;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import prince.Percolation;

public class PercolationStats {
	private int trials;
	private int n;
	private double[] cnt;
	
	private double MonteCarlo(){
		Percolation percolation = new Percolation(this.n);
		double openSitesCnt = 0;
		while(!percolation.percolates()){
			int x = StdRandom.uniform(1, this.n+1);
			int y = StdRandom.uniform(1, this.n+1);
			if(!percolation.isOpen(x, y)){
				percolation.open(x, y);
				openSitesCnt += 1;
			}
		}
		return openSitesCnt/(this.n*this.n);
	}
	
	public PercolationStats(int n, int trials){
		// perform trials independent experiments on an n-by-n grid
		this.n = n;
		this.trials = trials;
		if(n<=0 || trials<=0){
			throw new IllegalArgumentException();
		}
		cnt = new double[trials];
		for(int i=0;i<trials;++i){
			cnt[i] = MonteCarlo();
		}
	}
	
	public double mean(){
		// sample mean of percolation threshold
		return StdStats.mean(cnt);
	}
	
	public double stddev(){
		// sample standard deviation of percolation threshold
		if(this.trials == 1){
			return Double.NaN;
		}
		
		return StdStats.stddev(cnt);
	}
	
	public double confidenceLo(){
		// low  endpoint of 95% confidence interval
		return this.mean() - 1.96 * this.stddev() / Math.sqrt(trials); 
	}
	
	public double confidenceHi(){
		// high endpoint of 95% confidence interval
		return this.mean() + 1.96 * this.stddev() / Math.sqrt(trials);
	}
	
	public static void main(String[] args){
		// test client
//		int n = StdIn.readInt();
//		int trials = StdIn.readInt();
		int n = 20, trials = 20;
		
		PercolationStats pStats = new PercolationStats(n, trials);
		StdOut.printf("mean                    = %f\n", pStats.mean());
		StdOut.printf("stddev                  = %f\n", pStats.stddev());
		StdOut.printf("95%% confidence interval = [%f, %f]\n", pStats.confidenceLo(), pStats.confidenceHi());
	}
}
