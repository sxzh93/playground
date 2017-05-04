package prince;

import java.util.ArrayList;
import java.util.Arrays;

import collinear.LineSegment;
import prince.Point;

public class BruteCollinearPoints {
	private Point[] pointsArr;
	private int segCnt;
	
	public BruteCollinearPoints(Point[] points){
		this.pointsArr = points;
		this.segCnt = 0;
		Arrays.sort(this.pointsArr, Point.pointOrder());
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
}
