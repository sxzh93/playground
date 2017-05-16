package prince;

import java.awt.Point;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private class KdTreeNode{
		public Point2D point2d;
		public RectHV rect;
		public KdTreeNode leftChild;
		public KdTreeNode rightChild;
		public boolean horizontal;
		
		public KdTreeNode(double x, double y, RectHV rect, KdTreeNode lc, KdTreeNode rc, boolean hvflag) {
			this.point2d = new Point2D(x, y);
			this.rect = rect;
			this.leftChild = lc;
			this.rightChild = rc;
			this.horizontal = hvflag;
		}
		
		private int comparePoint2D(Point2D p){
			double nPrimary, nSecondary, pPrimary, pSecondary;
			nPrimary = this.horizontal ? point2d.x() : point2d.y();
			nSecondary = this.horizontal ? point2d.y() : point2d.x();
			pPrimary = this.horizontal ? p.x() : p.y();
			pSecondary = this.horizontal ? p.y() : p.x();
			if(nPrimary == pPrimary){
				if(nSecondary == pSecondary){
					return 0;
				}
				return nSecondary > pSecondary ? 1 : -1;
			}else{
				return nPrimary > pPrimary ? 1 : -1;
			}
		}
	}
	
	private KdTreeNode root;
	private int size;
	private double minDist;
	private Point2D near;
	
	public KdTree(){
		root = null;
		minDist = Double.MAX_VALUE;
		near = null;
		size = 0;
		// construct an empty KdTree
	}
	
	public boolean isEmpty(){
		// is the KdTree empty?
		return this.size == 0;
	}
	
	public int size(){
		// number of points in the KdTree
		return this.size;
	}
	
	public void insert(Point2D p){
		// add the point to the KdTree (if it is not already in the set)
		KdTreeNode parent = this.root;
		KdTreeNode cur = parent;
		boolean left = false;
		if(parent == null){
			this.root = new KdTreeNode(p.x(), p.y(), new RectHV(0, 0, 1, 1), null, null, true);
			++this.size;
			return;
		}
		while(cur != null){
			if(cur.comparePoint2D(p)>0){
				parent = cur;
				cur = cur.leftChild;
				left = true;
			}else if(cur.comparePoint2D(p)<0){
				parent = cur;
				cur = cur.rightChild;
				left = false;
			}else{
				return ;
			}
		}
		if(left){
			if(parent.horizontal){
				parent.leftChild = new KdTreeNode(p.x(), p.y(), 
						new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point2d.x(), parent.rect.ymax()), 
						null, null, !parent.horizontal);
			}else{
				parent.leftChild = new KdTreeNode(p.x(), p.y(), 
						new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point2d.y()), 
						null, null, !parent.horizontal);
			}
		}else{
			if(parent.horizontal){
				parent.rightChild = new KdTreeNode(p.x(), p.y(), 
						new RectHV(parent.point2d.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax()), 
						null, null, !parent.horizontal);
			}else{
				parent.rightChild = new KdTreeNode(p.x(), p.y(), 
						new RectHV(parent.rect.xmin(), parent.point2d.y(), parent.rect.xmax(), parent.rect.ymax()), 
						null, null, !parent.horizontal);
			}
		}
		++this.size;
	}
	
	public boolean contains(Point2D p){
		// does the KdTree contain point p?
		KdTreeNode cur = this.root;
		while(cur != null){
			if(cur.comparePoint2D(p)>0){
				cur = cur.leftChild;
			}else if(cur.comparePoint2D(p)<0){
				cur = cur.rightChild;
			}else{
				return true;
			}
		}
		return false;
	}
	
	private void drawPoints(KdTreeNode rt){
		if(rt == null){
			return ;
		}
		rt.point2d.draw();
		drawPoints(rt.leftChild);
		drawPoints(rt.rightChild);
	}
	
	private void drawLines(KdTreeNode rt, boolean drawHorizontal){
		if(rt == null){
			return ;
		}
		
		if(rt.horizontal && !drawHorizontal){
			StdDraw.line(rt.point2d.x(), rt.rect.ymin(), rt.point2d.x(), rt.rect.ymax());
		}else if(!rt.horizontal && drawHorizontal){
			StdDraw.line(rt.rect.xmin(), rt.point2d.y(), rt.rect.xmax(), rt.point2d.y());
		}
		
		drawLines(rt.leftChild, drawHorizontal);
		drawLines(rt.rightChild, drawHorizontal);
	}
	
	public void draw(){
		// draw all the points to standard draw in black and subdivisions in red(for vertical splits) and blue(for horizontal splits)
		// draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        this.drawPoints(this.root);
        
        // draw the rectangle
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        this.root.rect.draw();

        // draw subdivision lines
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        this.drawLines(this.root, false);
        
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        this.drawLines(this.root, true);
        
        StdDraw.show();
        StdDraw.pause(40);
	}
	
	private void doRange(KdTreeNode rt, ArrayList<Point2D> pList, RectHV rect){
		if(rt == null || !rect.intersects(rt.rect)){
			return ;
		}
		
		if(rect.contains(rt.point2d)){
			pList.add(rt.point2d);
		}
		doRange(rt.leftChild, pList, rect);
		doRange(rt.rightChild, pList, rect);
	}
	
	public Iterable<Point2D> range(RectHV rect){
		// all points that are inside the rectangle 
		ArrayList<Point2D> pList = new ArrayList<Point2D>();
		this.doRange(this.root, pList, rect);
		
		return pList;
	}
	
	private Point2D doNearest(KdTreeNode rt, Point2D p){
		Point2D ret = null;
		if(rt != null && rt.rect.distanceSquaredTo(p)<this.minDist){
			if(rt.point2d.distanceSquaredTo(p) < this.minDist){
				this.minDist = rt.point2d.distanceSquaredTo(p);
				ret = rt.point2d;
			}
			Point2D tmp1, tmp2;
			
			boolean leftFirst = (rt.horizontal && rt.point2d.x() > p.x()) || (!rt.horizontal && rt.point2d.y() > p.y());
			
			if(leftFirst){
				tmp1 = doNearest(rt.leftChild, p);
				tmp2 = doNearest(rt.rightChild, p);
			}else{
				tmp1 = doNearest(rt.rightChild, p);
				tmp2 = doNearest(rt.leftChild, p);
			}
			if(tmp2 != null){
				return tmp2;
			}else if(tmp1 != null){
				return tmp1;
			}
		}
		return ret;
	}
	
	public Point2D nearest(Point2D p){
		// a nearest neighbor in the KdTree to point p; null if the KdTree is empty
		this.minDist = Double.MAX_VALUE;
		return this.doNearest(this.root, p);
	}
	
	public static void main(String[] args){
		// unit testing of the methods (optional) 
	}
}
