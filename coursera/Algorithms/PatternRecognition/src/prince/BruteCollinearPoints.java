package prince;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import collinear.LineSegment;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import prince.Point;

public class BruteCollinearPoints {
	private Point[] pointsArr;
	private int segCnt;
	private ArrayList<LineSegment> lineSegmentsList = new ArrayList<LineSegment>();
	
	private static class ByPoint implements Comparator<Point>{
    	public int compare(Point a, Point b){
    		return a.compareTo(b);
    	}
    }

	private static Comparator<Point> pointOrder() {
    	return new ByPoint();
	}
	
	public BruteCollinearPoints(Point[] points) throws IllegalArgumentException, NullPointerException{
		if(points == null){
			throw new NullPointerException();
		}
		this.pointsArr = new Point[points.length];
		for(int i=0;i<this.pointsArr.length;++i){
			this.pointsArr[i] = points[i];
		}
		this.segCnt = 0;
		Arrays.sort(this.pointsArr, BruteCollinearPoints.pointOrder());

		for(int i=0;i<this.pointsArr.length-1;++i){
			if(this.pointsArr[i].compareTo(this.pointsArr[i+1]) == 0){
				throw new IllegalArgumentException();
			}
		}
		
		int size = pointsArr.length;
		for(int p=0;p<=size-4;++p){
			for(int q=p+1;q<=size-3;++q){
				for(int r=q+1;r<=size-2;++r){
					for(int s=r+1;s<=size-1;++s){
						if(pointsArr[p].slopeTo(pointsArr[q]) == pointsArr[p].slopeTo(pointsArr[r]) &&
								pointsArr[p].slopeTo(pointsArr[r]) == pointsArr[p].slopeTo(pointsArr[s])){
							++this.segCnt;
							this.lineSegmentsList.add(new LineSegment(pointsArr[p],pointsArr[s]));
						}
					}
				}
			}
		}
	}
	
	public int numberOfSegments(){
		return this.segCnt;
	}
	
	public LineSegment[] segments(){
		LineSegment[] ret = new LineSegment[this.segCnt];
		for(int i=0;i<this.segCnt;++i){
			ret[i] = this.lineSegmentsList.get(i);
		}
		return ret;
	}
	
	public static void main(String[] args){
		// read the n points from a file
		String fname = "src/collinear/input8.txt";
		In in = new In(fname);
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
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}
