package prince;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
	private class KdTreeNode{
		double x;
		double y;
		boolean horizontal;
		KdTreeNode leftChild;
		KdTreeNode rightChild;
		public KdTreeNode(double x, double y, boolean hvflag, KdTreeNode lc, KdTreeNode rc) {
			this.x = x;
			this.y = y;
			this.horizontal = hvflag;
			this.leftChild = lc;
			this.rightChild = rc;
		}
	}
	
	public KdTree(){
		
		// construct an empty KdTree
	}
	
	public boolean isEmpty(){
		// is the KdTree empty?
	}
	
	public int size(){
		// number of points in the KdTree
	}
	
	public void insert(Point2D p){
		// add the point to the KdTree (if it is not already in the set)
	}
	
	public boolean contains(Point2D p){
		// does the KdTree contain point p?
	}
	
	public void draw(){
		// draw all the points to standard draw
	}
	
	public Iterable<Point2D> range(RectHV rect){
		// all points that are inside the rectangle 
	}
	
	public Point2D nearest(Point2D p){
		// a nearest neighbor in the KdTree to point p; null if the KdTree is empty
	}
	
	public static void main(String[] args){
		// unit testing of the methods (optional) 
	}
}
