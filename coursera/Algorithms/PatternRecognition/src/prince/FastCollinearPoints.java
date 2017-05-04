package prince;

import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

import collinear.LineSegment;
import edu.princeton.cs.algs4.*;
import prince.Point;

public class FastCollinearPoints {
	private int segCnt;
	private Point[] pointsArr;
	private Point[] sortArr;
	
	public FastCollinearPoints(Point[] points) throws IllegalArgumentException, NullPointerException{
		if(points == null){
			throw new NullPointerException();
		}
		
		this.pointsArr = points;
		Arrays.sort(this.pointsArr, Point.pointOrder());
		
		this.sortArr = new Point[this.pointsArr.length];
		for(int i=0;i<this.pointsArr.length;++i){
			this.sortArr[i] = this.pointsArr[i];
		}
		
		this.segCnt = 0;
		
		Point tmPoint = this.pointsArr[0];
		for(int i=1;i<this.pointsArr.length;++i){
			if(this.pointsArr[i].compareTo(tmPoint) == 0){
				throw new IllegalArgumentException();
			}
			tmPoint = this.pointsArr[i];
		}
	}
	
	public int numberOfSegments(){
		return this.segCnt;
	}
	
	public LineSegment[] segments(){
		ArrayList<LineSegment> reSegments = new ArrayList<LineSegment>();
		for(int i=0;i<this.pointsArr.length;++i){
			Arrays.sort(this.sortArr, this.pointsArr[i].slopeOrder());
			for(int j=0;j<this.sortArr.length;){
				Point start = this.sortArr[j];
				int k = j+1;
				while(this.pointsArr[i].slopeTo(start) == this.pointsArr[i].slopeTo(this.sortArr[k])){
					++k;
				}
				j = k;
				if(j - k + 1 < 4 || this.pointsArr[i].compareTo(start) >= 0){
					continue;
				}else{
					reSegments.add(new LineSegment(start, this.sortArr[k-1]));
					++this.segCnt;
				}
			}
		}
		return reSegments.toArray(new LineSegment[this.segCnt]);
	}
	
	public static void main(String[] args){
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for(int i=0;i<n;++i){
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}
		
		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}