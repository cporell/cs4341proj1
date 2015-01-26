package cs4341proj1;

import java.util.ArrayList;
import java.util.Arrays;

// MoveTree class is a child of the GameBoard class. 
// It represents the child nodes of the game state tree.
public class MoveTree extends GameBoard {
	
	private boolean isvalid;
	private int player;
	private int col;
	private int moveType;
	
	// MoveTree constructor takes some elements from the constructor of its parent,
	// and stores a copy of the current game state, and the proposed move.
	MoveTree(GameBoard current, int player, int col, int movetype) {
		super(current.rowsCols.length, current.rowsCols[0].length);
		for (int i = 0; i < rowsCols.length; i++){
			this.rowsCols[i] = Arrays.copyOf(current.rowsCols[i], current.rowsCols[i].length);
		}
		this.p1pop = current.p1pop;
		this.p2pop = current.p2pop;
		this.player = player;
		this.col = col;
		this.moveType = movetype;
		
		this.isvalid = this.isMoveValid(player, col, movetype);
		
		if (this.isvalid){
			this.applyMove((byte)player, col, moveType);
		}
		
	}
	
	// Getters for the MoveTree.
	public boolean getIsValid(){
		return this.isvalid;
	}
	
	public int getMoveType(){
		return this.moveType;
	}
		
	/**
	 * This is the heuristic evaluation for a move.
	 * It assigns a points value based on the current state of the board.
	 * This points value will eventually be assigned to this MoveTree's moveValue.
	 */
	private int evalMove(int currentDepth){
		int N = Config.getInstance().getNumWin();	// Grab the 'N' we have to connect
		//int player1Points = 0;		// The amount of points the player's pieces give
		//int player2Points = 0;		// The amount of points the opponent's pieces give
		//int score = 0;				// The final score to evaluate a move
		// Checks to see whether either player will win if this move is played.
		boolean winning = isNConnected((byte)1, N) == N;
		boolean losing = isNConnected((byte)2, N) == N;
		if(winning && losing){
			// If the player's move will be a draw, set points value to low priority.
			// Even though it is not technically a loss, it is not a win either,
			// so we want to give it just above our lowest value.
			// Make the 'draw' state just above the 'lose' state,
			// so it doesn't pick losing over drawing
			//Logger.getInstance().print("tie");
			return Integer.MIN_VALUE + 2 + currentDepth;
			
		} else if (winning){
			// If this is a winning move, we want to set the points value to our highest value.
			//Logger.getInstance().print("winning");
			return Integer.MAX_VALUE - 1 - currentDepth;
		} else if (losing) {
			// This is a losing move, so return the least desirable score. 
			//Logger.getInstance().print("loss");
			return Integer.MIN_VALUE + 1 + currentDepth;
		} else {
			// If the proposed move doesn't reach a terminal state, search the board to score this move.
			return scanBoard();
		}
		
	}

	// Scans the board based on a proposed move.
	// This method checks to look for chains of players' pieces.
	// Player 1 is rewarded for its own chains, while it is penalized for leaving Player 2's chains open.
	private int scanBoard(){
		int score = 0;
		// Grab a series of coords for piece chains.
		// Stores the coords of pieces we've seen in a "visited" arrayList, so we know not to count it twice.
		ArrayList<Coords> visitedVertical = new ArrayList<Coords>();
		ArrayList<Coords> visitedHorizontal = new ArrayList<Coords>();
		ArrayList<Coords> visitedDownDiag = new ArrayList<Coords>();
		ArrayList<Coords> visitedUpDiag = new ArrayList<Coords>();
		
		// After we have scanned the board, we analyze the coords gathered above for chains.
		// We get points for having our own chains, while we lose points for leaving open chains for
		// the opponent.
		for (int i = 0; i < rowsCols.length; i++){
				for (int j = 0; j < rowsCols[0].length; j++){
					if(rowsCols[i][j] == (byte)1){
						score += analyzeVertical(i, j, visitedVertical);
						score += analyzeHorizontal(i, j, visitedHorizontal);
						score += analyzeDownDiag(i,j, visitedDownDiag);
						score += analyzeUpDiag(i,j, visitedUpDiag);
					} else if (rowsCols[i][j] == (byte)2){
						score -= analyzeVertical(i, j, visitedVertical);
						score -= analyzeHorizontal(i, j, visitedHorizontal);
						score -= analyzeDownDiag(i,j, visitedDownDiag);
						score -= analyzeUpDiag(i,j, visitedUpDiag);
					}
				}
		}
		
		// At the end, return the score
		return score;
	}
	
	// Analyze a vertical chain of pieces.
	// Given a coord, we look up and down from that piece.
	// If we find pieces of a similar player, we add to the chain.
	private int analyzeVertical(int row, int col, ArrayList<Coords> visited){
		int connected = 1; // Start with initial chain of 1 for the starting piece.
		
		// If we've already visited this spot, back out.
		if (visited.contains(new Coords(row, col))){
			return 0;
		}
		// Add this spot to our "visited" data.
		visited.add(new Coords(row, col));
		// Look at the rows above first.
		for(int i = row - 1; i > -1; i--){
			if(rowsCols[i][col] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(i, col));
			} else {
				break;
			}
		}
		// Look at the rows below
		for(int i = row + 1; i < rowsCols.length; i++){
			if(rowsCols[i][col] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(i, col));
			} else {
				break;
			}
		}
		// If the row at the top of the chain is free, return the amount connected.
		// Else, the chain is blocked, so no score.
		if(nextOpenRow(col) == row - 1){
			return connected;
		} else {
			return 0;
		}
	}
	
	// Analyze a horizontal chain of pieces.
	// Given a coord, we look right and left from that piece.
	// If we find pieces of a similar player, we add to the chain.
	private int analyzeHorizontal(int row, int col, ArrayList<Coords> visited){
		int connected = 1;
		boolean openleft = false;
		boolean openright = false;
		// If we've already visited this spot, back out.
		if (visited.contains(new Coords(row, col))){
			return 0;
		}
		// Add this spot to our "visited" list.
		visited.add(new Coords(row, col));
		
		// Look at the pieces to the left, keeping track of whether a piece can be dropped to the end
		for(int i = col - 1; i > -1; i--){
			if(rowsCols[row][i] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(row, i));
			} else if(rowsCols[row][i] == (byte)0){
				openright = true;
				break;
			} else {
				break;
			}
		}
		
		// Look at the pieces to the right, keeping track of whether a piece can be dropped to the end
		for(int i = col + 1; i < rowsCols[0].length; i++){
			if(rowsCols[row][i] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(row, i));
			} else if(rowsCols[row][i] == (byte)0){
				openleft = true;
				break;
			} else {
				break;
			}
		}
		
		// If the chain is open, return the number of pieces connected, else it is blocked, so no score.
		if(openleft || openright){
			if (openleft && openright){
				return connected * 2;
			} else {
				return connected;
			}
		} else {
			return 0;
		}
	}
	
	// Analyze a chain of pieces going up diagonally (from left to right).
	// Given a coord, we look around that piece.
	// If we find pieces of a similar player, we add to the chain.
	private int analyzeUpDiag(int row, int col, ArrayList<Coords> visited){
		// Base chain size of 1 for starting piece
		int connected = 1;
		boolean openleft = false;
		boolean openright = false;
		// If we've already visited this spot, back out.
		if (visited.contains(new Coords(row, col))){
			return 0;
		}
		// Else add it to visited.
		visited.add(new Coords(row, col));
		// Search up and to the right for pieces connected diagonally.
		for(int i = 1; row + i < rowsCols.length && col -i > - 1 ; i++){
			if(rowsCols[row + i][col - i] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(row + i, col - i));
			} else if(rowsCols[row + i][col - i] == (byte)0){
				openleft = true;
				break;
			} else {
				break;
			}
		}
		// Search down and to the left for pieces connected diagonally.
		for(int i = 1; row - i > -1 && col + i < rowsCols[0].length; i++){
			if(rowsCols[row - i][col + i] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(row - i, col + i));
			} else if(rowsCols[row - i][col + i] == (byte)0){
				openright = true;
				break;
			} else {
				break;
			}
		}
		// If a piece can be dropped into place to continue the chain, return the number connected.
		// Else it is blocked, so no score.
		if(openleft || openright){
			if (openleft && openright){
				return connected * 2;
			} else {
				return connected;
			}
		} else {
			return 0;
		}
	}
	
	// Analyze a chain of pieces going down diagonally (from left to right).
	// Given a coord, we look around that piece.
	// If we find pieces of a similar player, we add to the chain.
	private int analyzeDownDiag(int row, int col, ArrayList<Coords> visited){
		// Initial chain size of 1. You know the drill by now :)
		int connected = 1;
		boolean openleft = false;
		boolean openright = false;
		// If the given coord is in our "visited" list, skip it.
		if (visited.contains(new Coords(row, col))){
			return 0;
		}
		// Else add it to our list and continue
		visited.add(new Coords(row, col));
		
		// Look down and to the right for connected pieces
		for(int i = 1; row - i > -1 && col -i > - 1 ; i++){
			if(rowsCols[row - i][col - i] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(row - i, col - i));
			} else if(rowsCols[row - i][col - i] == (byte)0){
				openleft = true;
				break;
			} else {
				break;
			}
		}
		// Search up and to the left for connected pieces.
		for(int i = 1; row + i < rowsCols.length && col + i < rowsCols[0].length; i++){
			if(rowsCols[row + i][col + i] == rowsCols[row][col]){
				connected++;
				visited.add(new Coords(row + i, col + i));
			} else if(rowsCols[row + i][col + i] == (byte)0){
				openright = true;
				break;
			} else {
				break;
			}
		}
		// If the chain is open, return the number of pieces connected. Else, score = 0.
		if(openleft || openright){
			if (openleft && openright){
				return connected * 2;
			} else {
				return connected;
			}
		} else {
			return 0;
		}
	}
	
	// Passes a column as a default argument to isNConnected.
	private int isNConnected(byte player, int numConnected){
		return isNConnected(player, numConnected, this.col);
	}
	
	/**
	 * This method checks a column to look for the number of connected pieces.
	 * @param player The player whose pieces we are looking up.
	 * @return The number of pieces connected 
	 */
	private int isNConnected(byte player, int numConnected, int col){
		//int N = Config.getInstance().getNumWin();

		int maxConnected = Integer.MIN_VALUE;
		//Goes down the whole column, looking for the given player's pieces.
		for(int i = 0; i < rowsCols.length; i++){
			int topleft = 0;
			int topright = 0;
			int left = 0;
			int right = 0;
			int bottomright = 0;
			int bottomleft = 0;
			int bottom = 0;
			// If we have a match, search around that piece.
			if (rowsCols[i][col] == player){
				// Searches the top left of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; i - j >= 0 && col - j >= 0; j++){//top left
					if (rowsCols[i - j][col - j] == player){
						topleft++;
					} else {
						break;
					}
				}
				// Searches the top right of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; i - j >= 0 && col + j < rowsCols[0].length; j++){//top right
					if (rowsCols[i - j][col + j] == player){
						topright++;
					} else {
						break;
					}
				}
				// Searches the left of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; col - j >= 0; j++){//left
					if (rowsCols[i][col - j] == player){
						left++;
					} else {
						break;
					}
				}
				// Searches the right of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; col + j < rowsCols[0].length; j++){//right
					if (rowsCols[i][col + j] == player){
						right++;
					} else {
						break;
					}
				}
				// Searches the bottom left of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; i + j < rowsCols.length && col - j >= 0; j++){//botomleft
					if (rowsCols[i + j][col - j] == player){
						bottomleft++;
					} else {
						break;
					}
				}
				// Searches the bottom of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; i + j < rowsCols.length; j++){//bottom
					if (rowsCols[i + j][col] == player){
						bottom++;
					} else {
						break;
					}
				}
				// Searches the bottom right of that piece for more pieces of that player.
				// Stops when there is no match.
				for (int j = 1; i + j < rowsCols.length && col + j < rowsCols[0].length; j++){//bottomright
					if (rowsCols[i + j][col + j] == player){
						bottomright++;
					} else {
						break;
					}
				}
				// If any of the above variables result in a chain at or exceeding the number required to win,
				// return that number.
				if(topleft + bottomright + 1 >= numConnected
						|| topright + bottomleft + 1 >= numConnected
						|| left + right + 1 >= numConnected
						|| bottom + 1 >= numConnected){
					return numConnected;
				}
			}
			// Else, return the maximum of all of them.
			maxConnected = Math.max(maxConnected, getMaxConnected(topleft + bottomright + 1, topright + bottomleft + 1,
					left + right + 1, bottom + 1));
		}
		
		return maxConnected;
	}
	
	/**
	 * Gets a maximum value of the various directions from getNConnected
	 * @param A series of values
	 * @return The maximum number of those values
	 */
	private int getMaxConnected(int... values) {
		int max = Integer.MIN_VALUE;
		for(int i : values) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}
	
	/**
	 * The child version of minimax.
	 * Returns the minimum move value for its branch
	 * This implementation of minimax has alpha-beta pruning built in.
	 * If this is a leaf move (has no child nodes), return the value for this leaf
	 * Else, returns the move with the lowest value among its children leaves 
	 */
	public int minimax(int depth, int alpha, int beta, int currentdepth) {
		boolean allMovesInvalid = true;
		// Stores the value of the move with the lowest value
		int currentVal;
		if (this.player == 1){
			currentVal = Integer.MAX_VALUE;
		} else {
			currentVal = Integer.MIN_VALUE;
		}
		
		MoveTree currentMove;
		
		//miniVal[0] = this.col;			// Store the column the action will take place in
		//miniVal[1] = this.moveType;		// Store the move type that will be used
		int N = Config.getInstance().getNumWin();
		if (depth == 0 || (this.isNConnected((byte)1, N) == N || this.isNConnected((byte)2, N) == N)){
			return this.evalMove(currentdepth);
		}
		
		// If we got here, then this leaf has children.
		// Search those children for the one with the lowest value, and return its column and move
		//for(MoveTree children: this.submoves) {
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				if (Thread.currentThread().isInterrupted()){
					return currentVal;
				}
				currentMove = null;
				if (this.player == 1){
					currentMove = new MoveTree(this, 2, j, i);
				} else {
					currentMove = new MoveTree(this, 1, j, i);
				}
				
				if (currentMove.isvalid){
					//Logger.getInstance().print(currentMove.toString());
					allMovesInvalid = false;
					int childVal = currentMove.minimax(depth - 1, alpha, beta, currentdepth + 1);
					//Logger.getInstance().print("prelim minimax " + j + "," + i + ":" + childVal);
					if (this.player == 1){
						currentVal = Math.min(childVal, currentVal);
						beta = Math.min(beta, currentVal);
					} else {
						currentVal = Math.max(childVal, currentVal);
						alpha = Math.max(alpha, currentVal);
					}

					if(beta < alpha){
						//prune(i);
						return currentVal;
					}

				}
				// If the time limit is reached, return the value we have.
				if (Thread.currentThread().isInterrupted()){
					return currentVal;
				}
			}
		}
		// If there are no valid moves, then score this move with the lowest value.
		if (allMovesInvalid){
			return Integer.MIN_VALUE + 1;
		}
		return currentVal;
	}
	
}
