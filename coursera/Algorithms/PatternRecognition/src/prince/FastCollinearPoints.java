package prince;

import java.util.Arrays;

import collinear.LineSegment;
import prince.Point;

public class FastCollinearPoints {
	private int segCnt;
	private Point[] pointsArr;
	private Point[] sortArr;
	
	public FastCollinearPoints(Point[] points){
		this.pointsArr = points;
		Arrays.sort(this.pointsArr, Point.pointOrder());
		this.sortArr = new Point[this.pointsArr.length];
		for(int i=0;i<this.pointsArr.length;++i){
			this.sortArr[i] = this.pointsArr[i];
		}
		this.segCnt = 0;
	}
	
	public int numberOfSegments(){
		return this.segCnt;
	}
	
	public LineSegment[] segments(){
		
	}
}