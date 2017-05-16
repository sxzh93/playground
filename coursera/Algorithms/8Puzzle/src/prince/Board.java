package prince;

import java.lang.*;
import java.util.*;

public class Board {
	private int dimension;
	private int rBlank;
	private int cBlank;
	//private int[][] blocks;
	private char[] blocks;

	public Board(int[][] blocks){
		// construct a board from an n-by-n array of blocks
		// (where blocks[i][j] = block in row i, column j)
		this.dimension = blocks.length;
		this.blocks = new char[this.dimension*this.dimension];

		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				this.blocks[i*this.dimension+j] = (char)blocks[i][j];
				if(this.blocks[i*this.dimension+j] == 0){
					this.rBlank = i;
					this.cBlank = j;
				}
			}
		}
	}

	public int dimension(){
		// board dimension n
		return this.dimension;
	}

	public int hamming(){
		// number of blocks out of place
		int mismatch = 0;
		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				if(this.blocks[i*this.dimension+j] == 0){
					continue;
				}
				if(this.blocks[i*this.dimension+j] != (i*this.dimension + j + 1)){
					++mismatch;
				}
			}
		}
		return mismatch;
	}

	public int manhattan(){
		// sum of Manhattan distances between blocks and goal
		int dist = 0;
		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				if(this.blocks[i*this.dimension+j] == 0){
					continue;
				}
				int row = (this.blocks[i*this.dimension+j] - 1) / this.dimension;
				int col = (this.blocks[i*this.dimension+j] - 1) % this.dimension;
				dist += Math.abs(row-i) + Math.abs(col-j);
			}
		}
		return dist;
	}

	public boolean isGoal(){
		// is this board the goal board?
		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				if(this.blocks[i*this.dimension+j] == 0){
					return (i == this.dimension-1) && (j == this.dimension-1);
				}
				if(this.blocks[i*this.dimension+j] != (i*this.dimension + j + 1)){
					return false;
				}
			}
		}
		return true;
	}

	public Board twin(){
		// a board that is obtained by exchanging any pair of blocks
		int[][] tblocks = new int[this.dimension][this.dimension];
		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				tblocks[i][j] = this.blocks[i*this.dimension+j];
			}
		}

		// exchange first and second
		int offset = 0;
		if(tblocks[0][0] == 0 || tblocks[0][1] == 0){
			++offset;
		}
		int tmp = tblocks[0+offset][0];
		tblocks[0+offset][0] = tblocks[0+offset][1];
		tblocks[0+offset][1] = tmp;

		return new Board(tblocks);
	}

	public boolean equals(Object y){
		// does this board equal y?
		if(y == this){
			return true;
		}
		if(y == null){
			return false;
		}
		if(y.getClass() != this.getClass()){
			return false;
		}
		Board that = (Board) y;
		if(that.dimension != this.dimension){
			return false;
		}
		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				if(this.blocks[i*this.dimension+j] != that.blocks[i*this.dimension+j]){
					return false;
				}
			}
		}
		return true;
	}

	public Iterable<Board> neighbors(){
		Stack<Board> stack = new Stack<Board>();
		int[][] dir = {{1,0},{-1,0},{0,-1},{0,1}};
		for(int i=0;i<4;++i){
			int newrow = rBlank + dir[i][0];
			int newcol = cBlank + dir[i][1];
			if(newrow>=0 && newrow<this.dimension && newcol>=0 && newcol<this.dimension){
				int[][] newblocks = new int[this.dimension][this.dimension];
				for(int j=0;j<this.dimension;++j){
					for(int k=0;k<this.dimension;++k){
						newblocks[j][k] = this.blocks[j*this.dimension+k];
					}
				}

				int tmp = newblocks[this.rBlank][this.cBlank];
				newblocks[this.rBlank][this.cBlank] = newblocks[newrow][newcol];
				newblocks[newrow][newcol] = tmp;

				stack.add(new Board(newblocks));
			}
		}
		return stack;
	}

	public String toString(){
		// string representation of this board
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.dimension+"\n");
		for(int i=0;i<this.dimension;++i){
			for(int j=0;j<this.dimension;++j){
				stringBuilder.append(String.format("%2d ", (int)this.blocks[i*this.dimension+j]));
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args){
		// unit tests (not graded)
	}
}
