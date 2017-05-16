package prince;

import java.lang.NullPointerException;
import java.util.ArrayList;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

public class PointSET {
	private SET<Point2D> pointSet;
	
	public PointSET(){
		// construct an empty set of points
		pointSet = new SET<Point2D>();
	}
	
	public boolean isEmpty(){
		// is the set empty? 
		return pointSet.isEmpty();
	}
	
	public int size(){
		// number of points in the set
		return pointSet.size();
	}
	
	public void insert(Point2D p){
		// add the point to the set (if it is not already in the set)
		if(p == null){
			throw new NullPointerException();
		}
		if(!pointSet.contains(p)){
			pointSet.add(p);
		}
	}
	
	public boolean contains(Point2D p){
		// does the set contain point p?
		if(p == null){
			throw new NullPointerException();
		}
		return pointSet.contains(p);
	}
	
	public void draw(){
		// draw all points to standard draw
		for(Point2D point2d : this.pointSet){
			point2d.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect){
		// all points that are inside the rectangle
		if(rect == null){
			throw new NullPointerException();
		}
		ArrayList<Point2D> ret = new ArrayList<Point2D>();
		for(Point2D point2d : this.pointSet){
			if(rect.contains(point2d)){
				ret.add(point2d);
			}
		}
		return ret;
	}
	
	public Point2D nearest(Point2D p){
		// a nearest neighbor in the set to point p; null if the set is empty 
		if(p == null){
			throw new NullPointerException();
		}
		
		Point2D ret = null;
		double minDist = Double.MAX_VALUE;
		
		for(Point2D point2d: this.pointSet){
			if(p.distanceSquaredTo(point2d) < minDist){
				minDist = p.distanceSquaredTo(point2d);
				ret = point2d;
			}
		}
		
		return ret;
	}
	
	public static void main(String[] args){
		// unit testing of the methods (optional)
		
	}
}
