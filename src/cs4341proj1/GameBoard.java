package cs4341proj1;
//CS 4341 Project 1
//Andrew Roskuski
//Connor Porell

import java.util.Arrays;
import java.util.Random;

// GameBoard class, a singleton used to store information about the current state of the game.
// Accessing this class can tell a player what the current board looks like, the current best move,
// and whether or not a player has used their only "pop" move.
// NOTE: This is an internal representation of the game board. Blank spaces are represented by "0",
// and for computational purposes, we are always Player 1. This has no effect on what the Referee sees or does.
public class GameBoard {
	private static GameBoard instance = null;
	protected byte[][] rowsCols;
	protected boolean p1pop = false;
	protected boolean p2pop = false;
	//protected MoveTree[] submoves = null;
	//private int plyscalculated = 0;
	private int[] bestmove = new int[2];
	Random rand = new Random();

	// Initially, fill the board with zeros for our own internal representation
	// Use bytes for the spaces to save on memory.
	protected GameBoard(int numRows, int numCols){
		rowsCols = new byte[numRows][numCols];
		for (int i = 0; i < numRows; i++){
			Arrays.fill(rowsCols[i], (byte)0);
		}

	}

	// Get the instance of the gameboard.
	public static GameBoard getInstance(){
		if (instance == null){
			instance = new GameBoard(Config.getInstance().getNumRows(),
					Config.getInstance().getNumCol()); 
		}
		return instance;
	}

	// Apply a move to the gameboard. Takes in a byte for the player, and the proposed move type and column.
	public void applyMove(byte player, int col, int movetype){
		// movetype == 0, pop.
		// If we get here, set the corresponding player's pop to true to show that it has been used.
		if (movetype == 0){
			if(player == 1){
				p1pop = true;
			} else {
				p2pop = true;
			}
			// Drop all pieces in the column down by 1 space.
			for(int i = rowsCols.length - 1; i > 0; i--){
				rowsCols[i][col] = rowsCols[i - 1][col];
			}
			rowsCols[0][col] = 0;
		} else {
			// Else, we drop the piece down the desired column.
			rowsCols[nextOpenRow(col)][col] = player;
		}
		
	}

	// Validates a move. Takes in the player #, the column #, and the move type.
	// Returns whether the move is valid or not.
	public boolean isMoveValid(int player, int col, int movetype){
		// If we pop, check whether or not we've popped.
		// If we have already popped, then the move is invalid.
		if (movetype == 0){
			if(player == 1 && p1pop){
				return false;
			} 
			if (player == 2 && p2pop) {
				return false;
			}
			return rowsCols[rowsCols.length - 1][col] == player;

		} else {
			// Else, we drop. 
			// Check whether this column has an open row. If so, move is valid, else invalid.
			return nextOpenRow(col) >= 0;
		}
	}

	// Returns the # of the next open row in this column.
	// This method checks if the current row is free. If it isn't it returns the previous row #.
	protected int nextOpenRow(int col){
		for (int i = 0; i < rowsCols.length; i++){
			if(rowsCols[i][col] != 0){
				return i - 1;
			}
		}
		return rowsCols.length - 1;
	}

	// Overrides Java's toString. This custom toString creates a String version of the entire board.
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

	// Returns the current best move.
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
		MoveTree currentMove; // MoveTree is a child node to the gameboard.
		boolean amovewasvalid = false;

		// Search through the moveTrees and to find the highest value of all the minimaxes.
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				if (Thread.currentThread().isInterrupted()){
					return;
				}
				currentMove = null;
				currentMove = new MoveTree(this, 1, j, i); // Creates a new child node with the
															// current game state, player#, move type, and col
				
				if(this.isMoveValid(1, j, i)){
					//Logger.getInstance().print(currentMove.toString());
					// If the move is valid, proceed with minimax
					amovewasvalid = true;
					int tempminimax = currentMove.minimax(depth - 1, maxValue, Integer.MAX_VALUE, 0);
					// Timeout handler. If the time limit is reached before minimax finishes, then back out.
					if (Thread.currentThread().isInterrupted()){
						return;
					}
					Logger.getInstance().print(j + "," + i + " minimax = " + tempminimax);
					// If our move scores higher than the current max, set the new max to it.
					if(tempminimax > maxValue){
						maxValue = tempminimax;
						maxValueIndex[0] = j;
						maxValueIndex[1] = i;
					}
					// If our move scores equal to the current max, randomly select from the best moves
					if (tempminimax == maxValue){
						if(rand.nextInt(2) == 1){
							maxValueIndex[0] = j;
							maxValueIndex[1] = i;
						}
					}
					// If our move scores lowers than the current min, set the new min to it.
					if (tempminimax < minValue){
						minValue = tempminimax;
					}
				}
			}
		}
		// Write to the logger if the move was invalid.
		if(!amovewasvalid){
			Logger.getInstance().print("Move was invalid");
		}

		// Set the best move equal to the highest-scoring move at the end.
		this.bestmove = maxValueIndex;
	}
}
