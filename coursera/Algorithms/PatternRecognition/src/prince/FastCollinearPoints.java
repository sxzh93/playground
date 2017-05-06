package prince;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import collinear.LineSegment;
import edu.princeton.cs.algs4.*;
import prince.Point;

public class FastCollinearPoints {
	private int segCnt;
	private Point[] pointsArr;
	private Point[] sortArr;
	private ArrayList<LineSegment> lineSegmentsList = new ArrayList<LineSegment>();
	
	private static class ByPoint implements Comparator<Point>{
    	public int compare(Point a, Point b){
    		return a.compareTo(b);
    	}
    }
	
	private static Comparator<Point> pointOrder() {
    	return new ByPoint();
    }
	
	public FastCollinearPoints(Point[] points) throws IllegalArgumentException, NullPointerException{
		if(points == null){
			throw new NullPointerException();
		}
		
		this.pointsArr = new Point[points.length];
		for(int i=0;i<points.length;++i){
			this.pointsArr[i] = points[i];
		}
		Arrays.sort(this.pointsArr, FastCollinearPoints.pointOrder());
		
		this.sortArr = new Point[this.pointsArr.length];
		for(int i=0;i<this.pointsArr.length;++i){
			this.sortArr[i] = this.pointsArr[i];
		}
		
		this.segCnt = 0;
		
		for(int i=0;i<this.pointsArr.length-1;++i){
			if(this.pointsArr[i].compareTo(this.pointsArr[i+1]) == 0){
				throw new IllegalArgumentException();
			}
		}
		
		for(int i=0;i<this.pointsArr.length;++i){
			Arrays.sort(this.sortArr, this.pointsArr[i].slopeOrder());
			for(int j=0;j<this.sortArr.length;){
				Point minPoint = this.sortArr[j];
				Point maxPoint = this.sortArr[j];
				double curSlope = this.pointsArr[i].slopeTo(this.sortArr[j]);
				int k = j+1;
				while(k < this.sortArr.length && this.pointsArr[i].slopeTo(this.sortArr[k]) == curSlope){
					if(minPoint.compareTo(this.sortArr[k])>0){
						minPoint = this.sortArr[k];
					}
					if(maxPoint.compareTo(this.sortArr[k])<0){
						maxPoint = this.sortArr[k];
					}
					++k;
				}
				
				if(k - j + 1 >= 4 && this.pointsArr[i].compareTo(minPoint) < 0){
					this.lineSegmentsList.add(new LineSegment(this.pointsArr[i], maxPoint));
					++this.segCnt;
				}
				j = k;
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
		String fname = "src/collinear/rs1423.txt";
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
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}