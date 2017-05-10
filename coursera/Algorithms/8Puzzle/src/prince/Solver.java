package prince;

import java.util.Comparator;
import java.util.Stack;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
	private class searchNode{
		public Board board;
		public int movesCnt;
		public searchNode previous;

		public searchNode(Board b, int m, searchNode p){
			this.board = b;
			this.movesCnt = m;
			this.previous = p;
		}
	}

	private static class SearchNodeComparator implements Comparator<searchNode>{
		public int compare(searchNode a, searchNode b){
			int pvala = a.board.manhattan() + a.movesCnt;
			int pvalb = b.board.manhattan() + b.movesCnt;
			if(pvala == pvalb){
				return 0;
			}
			return pvala < pvalb ? -1 : 1;
		}
	}

	private boolean solvable;
	private Board initialBoard;
	private Board twinBoard;
	private MinPQ<searchNode> AStarPQ;
	private MinPQ<searchNode> AStarPQTwin;
	private int movesCnt;
	private ArrayList<Board> soList;

	private void AStarSearch(){
		AStarPQ.insert(new searchNode(this.initialBoard, 0, null));
		AStarPQTwin.insert(new searchNode(this.twinBoard, 0, null));
		searchNode ans = null;
		while(true){
			searchNode cur = AStarPQ.delMin();
			if(cur.board.isGoal()){
				ans = cur;
				this.movesCnt = cur.movesCnt;
				this.solvable = true;
				break;
			}
			for (Board board : cur.board.neighbors()) {
				if(cur.previous == null || !board.equals(cur.previous.board)){
					AStarPQ.insert(new searchNode(board, cur.movesCnt+1, cur));
				}
			}

			searchNode twinCur = AStarPQTwin.delMin();
			if(twinCur.board.isGoal()){
				this.solvable = false;
				break;
			}
			for (Board board : twinCur.board.neighbors()) {
				if(cur.previous == null || !board.equals(twinCur.previous.board)){
					AStarPQTwin.insert(new searchNode(board, twinCur.movesCnt+1, twinCur));
				}
			}
		}
		if(this.solvable){
			Stack<Board> stack = new Stack<Board>();
			searchNode tmp = ans;
			while(!tmp.board.equals(this.initialBoard)){
				stack.push(tmp.board);
				tmp = tmp.previous;
			}
			stack.push(this.initialBoard);
			while(!stack.empty()){
				this.soList.add(stack.peek());
				stack.pop();
			}
		}
	}

	public Solver(Board initial){
		// find a solution to the initial board (using the A* algorithm)
		this.solvable = true;
		this.initialBoard = initial;
		this.twinBoard = this.initialBoard.twin();
		this.movesCnt = 0;
		this.soList = new ArrayList<Board>();

		AStarPQ = new MinPQ<searchNode>(new SearchNodeComparator());
		AStarPQTwin = new MinPQ<searchNode>(new SearchNodeComparator());

		this.AStarSearch();
	}

	public boolean isSolvable(){
		// is the initial board solvable?
		return this.solvable;
	}

	public int moves(){
		// min number of moves to solve initial board; -1 if unsolvable
		if(!this.solvable){
			return -1;
		}
		return this.movesCnt;
	}

	public Iterable<Board> solution(){
		// sequence of boards in a shortest solution; null if unsolvable
		if(!this.solvable){
			return null;
		}
		return this.soList;
	}

	public static void main(String[] args){
		// create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
}
