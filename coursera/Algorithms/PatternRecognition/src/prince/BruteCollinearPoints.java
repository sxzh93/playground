package prince;

import java.util.ArrayList;
import java.util.Arrays;

import collinear.LineSegment;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import prince.Point;

public class BruteCollinearPoints {
	private Point[] pointsArr;
	private int segCnt;
	
	public BruteCollinearPoints(Point[] points) throws IllegalArgumentException, NullPointerException{
		if(points == null){
			throw new NullPointerException();
		}
		this.pointsArr = points;
		this.segCnt = 0;
		Arrays.sort(this.pointsArr, Point.pointOrder());
		
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
		int size = pointsArr.length;
		ArrayList<LineSegment> reSegments = new ArrayList<LineSegment>();
		for(int p=0;p<=size-4;++p){
			for(int q=p+1;q<=size-3;++q){
				for(int r=q+1;r<=size-2;++r){
					for(int s=r+1;s<=size-1;++s){
						if(pointsArr[p].slopeTo(pointsArr[q]) == pointsArr[p].slopeTo(pointsArr[r]) &&
								pointsArr[p].slopeTo(pointsArr[r]) == pointsArr[p].slopeTo(pointsArr[s])){
							++this.segCnt;
							reSegments.add(new LineSegment(pointsArr[p],pointsArr[s]));
						}
					}
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
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}
