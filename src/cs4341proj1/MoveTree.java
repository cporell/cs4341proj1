package cs4341proj1;

import java.util.Arrays;

public class MoveTree extends GameBoard {
	
	private boolean isvalid;
	private int player;
	//private int moveValue = Integer.MIN_VALUE;
	private int col;
	private int moveType;
	private boolean terminal = false;
	
	MoveTree(GameBoard current, int player, int col, int movetype) {
		super(current.rowsCols.length, current.rowsCols[0].length);
		for (int i = 0; i < rowsCols.length; i++){
			this.rowsCols[i] = Arrays.copyOf(current.rowsCols[i], current.rowsCols[i].length);
		}
		//this.rowsCols = Arrays.copyOf(current.rowsCols, current.rowsCols.length);
		this.p1pop = current.p1pop;
		this.p2pop = current.p2pop;
		this.player = player;
		this.col = col;
		this.moveType = movetype;
		
		this.isvalid = this.isMoveValid(player, col, movetype);
		
		if (this.isvalid){
			this.applyMove((byte)player, col, moveType);
		}
		//Logger.getInstance().print("Valid: " + this.isvalid + " player" + player
		//		+ " col: " + col + " movetype: " + movetype);
		

		
	}
	

	public boolean getIsValid(){
		return this.isvalid;
	}
	
	public int getMoveType(){
		return this.moveType;
	}
	
	/*
	public void genPossibleMoves(){
		//Logger.getInstance().print("genPossibleMoves(): Valid: " + this.isvalid);
		if (this.isvalid){
			//Logger.getInstance().print("genPossibleMoves(): Valid: " + this.isvalid);
			if (this.player == 1){
				this.genPossibleMoves(2);
			} else {
				this.genPossibleMoves(1);
			}
			if(this.submoves == null){
				Logger.getInstance().print("gen possible moves Canceled");
			} else {
				this.prunedlength = this.submoves.length;
			}
		}
		//this.rowsCols = null;
	}
	/*
	
	/**
	 * This is the heuristic evaluation for a move.
	 * It assigns a points value based on the current state of the board.
	 * This points value will eventually be assigned to this MoveTree's moveValue.
	 */
	public int evalMove(){
		int N = Config.getInstance().getNumWin();	// Grab the 'N' we have to connect
		int player1Points = 0;		// The amount of points the player's pieces give
		int player2Points = 0;		// The amount of points the opponent's pieces give
		int score = 0;				// The final score to evaluate a move
		boolean winning = isNConnected(1, N) == N;
		boolean losing = isNConnected(2, N) == N;
		this.terminal = winning || losing;
		if(winning && losing){
			// If the player's move will be a draw, set points value to low priority.
			// Even though it is not technically a loss, it is not a win either,
			// so we want to give it just above our lowest value.
			//this.moveValue = (-(10 * N) + 1);	// Make the 'draw' state just above the 'lose' state,
												// so it doesn't pick losing over drawing
			return (-(10 * N) + 1);
		} else if (winning){
			// If this is a winning move, we want to set the points value to our highest value.
			//this.moveValue = 10 * N;
			return 10 * N;
		} else if (losing) {
			// This is a losing move, so return the least desirable score. 
			//this.moveValue = -(10 * N);
			return -(10 * N);
		} else {
			for(int i = 1; i < 3; i++) {	// Loop generates the move evaluation for player 1, then player 2
				for(int j = 0; j < N; j++){	// Inner loop keeps track of number of pieces connected.
					double result = 0;
					int prelimScore = this.isNConnected(i, j);
					// Determine score based on n-number of connected spots
					result = this.judgePrelimScore(prelimScore);
					if(i == 1) {	// player 1
						player1Points += result;
					}
					else {			// i == 2, player 2
						player2Points += result;
					}
				}
			}
		}
		
		if(this.player == 1){
			score = player1Points - player2Points;	
		}
		else {	// else player == 2
			score = player2Points - player1Points;
		}
		return score;
	}
	
	/**
	 * Judges the preliminary score given by isNConnected.
	 * Awards a certain amount of points based on the number given, and the number of pieces needed to win. 
	 */
	public double judgePrelimScore(int prelimScore) {
		double score = 0;
		double NtoWin = (double) Config.getInstance().getNumWin();
		if(prelimScore == NtoWin) {
			score = Math.pow(10, NtoWin);
		}
		else if(prelimScore == NtoWin - 1) {
			score = Math.pow(10, NtoWin-1);
		}
		else if(prelimScore == NtoWin - 2) {
			score = Math.pow(10, NtoWin-2);
		}
		else if(prelimScore <= NtoWin - 3) {
			score = Math.pow(10, NtoWin - prelimScore);
		}
		else {
			score = 0;
		}
		return score;
	}
	
	/**
	 * I'm modifying this to return the number connected. Any calls to this will have a
	 * check to see if it matches the "NtoWin" in Config. - Connor
	 * @param player The player whose pieces we are looking up.
	 * @return The number of pieces connected 
	 */
	private int isNConnected(int player, int numConnected){
		int N = Config.getInstance().getNumWin();
		int topleft = 0;
		int topright = 0;
		int left = 0;
		int right = 0;
		int bottomright = 0;
		int bottomleft = 0;
		int bottom = 0;
		int maxConnected = Integer.MIN_VALUE;
		for(int i = 0; i < rowsCols.length; i++){
			if (rowsCols[i][col] == player){
				for (int j = 1; i - j >= 0 && col - j >= 0; j++){//top left
					if (rowsCols[i - j][col - j] == player){
						topleft++;
					} else {
						break;
					}
				}
				for (int j = 1; i - j >= 0 && col + j < rowsCols[0].length; j++){//top right
					if (rowsCols[i - j][col + j] == player){
						topright++;
					} else {
						break;
					}
				}
				for (int j = 1; col - j >= 0; j++){//left
					if (rowsCols[i][col - j] == player){
						left++;
					} else {
						break;
					}
				}
				for (int j = 1; col + j < rowsCols[0].length; j++){//right
					if (rowsCols[i][col + j] == player){
						right++;
					} else {
						break;
					}
				}
				for (int j = 1; i + j < rowsCols.length && col - j >= 0; j++){//botomleft
					if (rowsCols[i + j][col - j] == player){
						bottomleft++;
					} else {
						break;
					}
				}
				for (int j = 1; i + j < rowsCols.length; j++){//bottom
					if (rowsCols[i + j][col] == player){
						bottom++;
					} else {
						break;
					}
				}
				for (int j = 1; i + j < rowsCols.length && col + j < rowsCols[0].length; j++){//bottomright
					if (rowsCols[i + j][col + j] == player){
						bottomright++;
					} else {
						break;
					}
				}
				if(topleft + bottomright + 1 >= numConnected
						|| topright + bottomleft + 1 >= numConnected
						|| left + right + 1 >= numConnected
						|| bottom + 1 >= numConnected){
					return numConnected;
				}
			}
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
	public int getMaxConnected(int... values) {
		int max = Integer.MIN_VALUE;
		for(int i : values) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}
	
/*
	public void calculatePly(int depth){
		if (depth == 0 && this.isvalid){
			this.genPossibleMoves();
		} else if(!this.isvalid) {
			//Logger.getInstance().print("Skipping invalid move");
		} else {
			for(int i = 0; i < this.prunedlength; i++){
				if (Thread.currentThread().isInterrupted()){
					return;
				}
				submoves[i].calculatePly(depth - 1);
			}
		}
	}
	*/
	
	/**
	 * The child version of minimax.
	 * Returns the minimum move value for its branch
	 * If this is a leaf move (has no child nodes), return the value for this leaf
	 * Else, returns the move with the lowest value among its children leaves 
	 */
	public int minimax(int depth, int alpha, int beta) {
		//int[] miniVal = new int[2];
		boolean allMovesInvalid = true;
		// Stores the value of the move with the lowest value
		//int currentVal = this.moveValue;
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
		if (depth == 0 || (this.isNConnected(1, N) == N || this.isNConnected(2, N) == N)){
			return this.evalMove();
		}
		
		// If we got here, then this leaf has children.
		// Search those children for the one with the lowest value, and return its column and move
		//for(MoveTree children: this.submoves) {
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				if (Thread.currentThread().isInterrupted()){
					return currentVal;
				}
				if (this.player == 1){
					currentMove = new MoveTree(this, 2, j, i);
				} else {
					currentMove = new MoveTree(this, 1, j, i);
				}
				
				if (currentMove.isvalid){
					allMovesInvalid = false;
					int childVal = currentMove.minimax(depth - 1, alpha, beta);
					if (this.player == 1){
						if(childVal < currentVal) {
							currentVal = childVal;
							if(childVal < beta){
								beta = childVal;
							}
						}
					} else {
						if(childVal > currentVal) {
							currentVal = childVal;
							if(childVal < alpha){
								alpha = childVal;
							}
						}
					}
					if(beta <= alpha){
						//prune(i);
						return currentVal;
					}

				}
				if (Thread.currentThread().isInterrupted()){
					return currentVal;
				}
			}
		}
		if (allMovesInvalid){
			return (-(10 * N) + 1);
		}
		return currentVal;
	}
	
}
