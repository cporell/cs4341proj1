package cs4341proj1;

import java.util.Arrays;

public class MoveTree extends GameBoard {
	
	private boolean isvalid;
	private int player;
	private int moveValue = Integer.MIN_VALUE;
	private int col;
	private int moveType;
	private boolean terminal = false;
	private int prunedlength;
	
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
		//Logger.getInstance().print("Valid: " + this.isvalid + " player" + player
		//		+ " col: " + col + " movetype: " + movetype);
		
		this.init();
		
	}
	
	private void init(){
		if (this.isvalid && !this.terminal){
			this.applyMove((byte)player, col, moveType);
			this.evalMove();
		}
	}
	
	public boolean getIsValid(){
		return this.isvalid;
	}
	
	public int getMoveType(){
		return this.moveType;
	}
	
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
	
	public void evalMove(){
		boolean winning = isNConnected(1);
		boolean losing = isNConnected(2);
		this.terminal = winning || losing;
		if(winning && losing){
			this.moveValue = -100;
		} else if (winning){
			this.moveValue = 100;
		} else if (losing) {
			this.moveValue = -100;
		} else {
			this.moveValue = 0;
		}

		
	}
	
	private boolean isNConnected(int player){
		int N = Config.getInstance().getNumWin();
		int topleft = 0;
		int topright = 0;
		int left = 0;
		int right = 0;
		int bottomright = 0;
		int bottomleft = 0;
		int bottom = 0;
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
				if(topleft + bottomright + 1 >= N
						|| topright + bottomleft + 1 >= N
						|| left + right + 1 >= N
						|| bottom + 1 >= N){
					return true;
				}
			}
		}
		return false;
	}
	

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
	
	/**
	 * The child version of minimax.
	 * Returns the minimum move value for its branch
	 * If this is a leaf move (has no child nodes), return the value for this leaf
	 * Else, returns the move with the lowest value among its children leaves 
	 */
	public int minimax(int alpha, int beta) {
		//int[] miniVal = new int[2];
		
		// Stores the value of the move with the lowest value
		int currentVal = this.moveValue;	
		
		//miniVal[0] = this.col;			// Store the column the action will take place in
		//miniVal[1] = this.moveType;		// Store the move type that will be used
		
		if(this.submoves == null || this.terminal) {
			return currentVal;
		}
		
		// If we got here, then this leaf has children.
		// Search those children for the one with the lowest value, and return its column and move
		//for(MoveTree children: this.submoves) {
		for(int i = 0; i < this.prunedlength; i++){	
			if (Thread.currentThread().isInterrupted()){
				return currentVal;
			}
			if (this.submoves[i].isvalid){
				int childVal = this.submoves[i].minimax(alpha, beta);
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
					prune(i);
					return currentVal;
				}
				
			}
			if (Thread.currentThread().isInterrupted()){
				return currentVal;
			}
		}
		
		return currentVal;
	}
	
	public void nullifyRowsCols(int depth){
		if (depth == 0){
			this.rowsCols = null;
		} else if (this.submoves != null){
			for(int i = 0; i < this.prunedlength; i++){
				submoves[i].nullifyRowsCols(depth - 1);
			}
		}
	}
	
	private void prune(int index){
		for (int i = index + 1; i < this.submoves.length; i++){
			this.submoves[i] = null;
		}
		this.prunedlength = index + 1;
	}
}
