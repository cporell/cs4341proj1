package cs4341proj1;

import java.util.Arrays;

public class GameBoard {
	private static GameBoard instance = null;
	protected byte[][] rowsCols;
	protected boolean p1pop = false;
	protected boolean p2pop = false;
	//protected MoveTree[] submoves = null;
	//private int plyscalculated = 0;
	private int[] bestmove = new int[2];

	protected GameBoard(int numRows, int numCols){
		rowsCols = new byte[numRows][numCols];
		for (int i = 0; i < numRows; i++){
			Arrays.fill(rowsCols[i], (byte)0);
		}

	}

	public static GameBoard getInstance(){
		if (instance == null){
			instance = new GameBoard(Config.getInstance().getNumRows(),
					Config.getInstance().getNumCol()); 
		}
		return instance;
	}

	public void applyMove(byte player, int col, int movetype){
		if (movetype == 0){//pop
			if(player == 1){
				p1pop = true;
			} else {
				p2pop = true;
			}
			//rowsCols[rowsCols.length - 1][col] = 0;
			for(int i = rowsCols.length - 1; i > 0; i--){
				rowsCols[i][col] = rowsCols[i - 1][col];
			}
			rowsCols[0][col] = 0;
		} else {//drop
			rowsCols[nextOpenRow(col)][col] = player;
		}
		//Logger.getInstance().print("Move applied");
		/*
		if (this.submoves != null){
			//This next line replaces the gameboard's submove tree with that of the 
			//  hypothetical move that matches the one that was just applied
			//submoves is always ordered so that it contains a pop move (movetype == 0)
			//  for each column in order then a drop move (movetype == 1) for each column in
			//  order. That is why the math to determine the index works
			Logger.getInstance().print("About to move tree");
			if (this.submoves[col + (rowsCols[0].length * movetype)] != null){
				this.submoves = this.submoves[col + (rowsCols[0].length * movetype)].submoves;
				Logger.getInstance().print("About to adjust calculated ply number");
				this.plyscalculated--;
				if (this.submoves == null){
					Logger.getInstance().print("submoves was null, resetting things");
					this.plyscalculated = 0;
				}
			} else {
				this.submoves = null;
				Logger.getInstance().print("submoves was null, resetting things");
				this.plyscalculated = 0;
			}

			Logger.getInstance().print("Tree moved");
		}
		 */

	}

	public boolean isMoveValid(int player, int col, int movetype){
		if (movetype == 0){//pop
			if(player == 1 && p1pop){
				return false;
			} 
			if (player == 2 && p2pop) {
				return false;
			}
			return rowsCols[rowsCols.length - 1][col] == player;

		} else {//drop
			return nextOpenRow(col) >= 0;
		}
	}

	protected int nextOpenRow(int col){
		for (int i = 0; i < rowsCols.length; i++){
			if(rowsCols[i][col] != 0){
				return i - 1;
			}
		}
		return rowsCols.length - 1;
	}

	@Override
	public String toString(){
		String result = "";
		for (int i = 0; i < rowsCols.length; i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				result += rowsCols[i][j] + "";
			}
			result += "\n";
		}
		return result;
	}
/*
	public void genPossibleMoves(int player){
		//Logger.getInstance().print("Called From" + (this instanceof MoveTree));
		MoveTree[] result = new MoveTree[rowsCols[0].length * 2];
		int n = 0;
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				if (Thread.currentThread().isInterrupted()){
					return;
				}
				//Logger.getInstance().print("Making a movetree");
				result[n] = new MoveTree(this, player, j, i);
				n++;
				if (Thread.currentThread().isInterrupted()){
					return;
				}
			}
		}
		this.submoves = result;
	}


	public boolean calculatePly(){
		if (this.plyscalculated >= 5){
			return false;
		}
		if (this.plyscalculated == 0){
			this.genPossibleMoves(1);
		} else {
			for(int i = 0; i < submoves.length; i++){
				if (Thread.currentThread().isInterrupted()){
					return false;
				}
				if(submoves[i] != null){
					if(submoves[i].getIsValid()){
						submoves[i].calculatePly(this.plyscalculated - 1);
					}
				}
				if (Thread.currentThread().isInterrupted()){
					return false;
				}
			}

		}

		plyscalculated++;

		for(int i = 0; i < submoves.length; i++){
			if (this.submoves[i] != null){
				submoves[i].nullifyRowsCols(this.plyscalculated - 2);
			}

		}

		Logger.getInstance().print(plyscalculated + " plys calculated");
		return true;
	}
*/
	public int[] getBestMove(){
		return this.bestmove;
	}

	/**
	 * Function to search for the best possible move via minimax
	 * Gathers all the minimax values of its movetrees, then chooses the maximum value and returns it.
	 * Returns an array holding two ints: the column to move in, and the move type
	 */
	public void minimax(int depth) {
		int maxValue = Integer.MIN_VALUE;
		int minValue = Integer.MAX_VALUE;
		int[] maxValueIndex = new int[2];
		MoveTree currentMove;
		boolean amovewasvalid = false;
		
		// Search through the moveTrees and to find the highest value of all the minimaxes.
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				if (Thread.currentThread().isInterrupted()){
					return;
				}
				currentMove = null;
				currentMove = new MoveTree(this, 1, j, i);
				if(this.isMoveValid(1, j, i)){
					amovewasvalid = true;
					int tempminimax = currentMove.minimax(depth - 1, maxValue, minValue);
					if (Thread.currentThread().isInterrupted()){
						return;
					}
					if(tempminimax > maxValue){
						maxValue = tempminimax;
						maxValueIndex[0] = j;
						maxValueIndex[1] = i;
					}
					if (tempminimax < minValue){
						minValue = tempminimax;
					}
				}
			}
		}
		if(!amovewasvalid){
			Logger.getInstance().print("WTF");
		}
		if(p1pop){
			Logger.getInstance().print("p1 has popped");
		}

		this.bestmove = maxValueIndex;
	}
}
