package cs4341proj1;

public class MoveTree extends GameBoard {
	
	private boolean isvalid;
	private int player;
	private int moveValue = Integer.MIN_VALUE;
	private int col;
	private int moveType;
	
	MoveTree(GameBoard current, int player, int col, int movetype) {
		super(current.rowsCols.length, current.rowsCols[0].length);
		this.rowsCols = current.rowsCols;
		this.p1pop = current.p1pop;
		this.p2pop = current.p2pop;
		this.player = player;
		this.col = col;
		this.moveType = movetype;
		
		this.isvalid = this.isMoveValid(player, col, movetype);
		
		if (this.isvalid){
			this.applyMove(player, col, movetype);
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
		if (this.player == 1){
			this.genPossibleMoves(2);
		} else {
			this.genPossibleMoves(1);
		}
	}
	
	public void evalMove(){
		if (isNConnected(1)){
			moveValue = Integer.MAX_VALUE;
		}
		if (isNConnected(2)){
			moveValue = Integer.MIN_VALUE;
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
		if (depth == 0){
			this.genPossibleMoves(1);
		} else {
			for(int i = 0; i < submoves.length; i++){
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
	@Override
	public int minimax() {
		//int[] miniVal = new int[2];
		
		// Stores the value of the move with the lowest value
		int lowestVal = this.moveValue;	
		
		//miniVal[0] = this.col;			// Store the column the action will take place in
		//miniVal[1] = this.moveType;		// Store the move type that will be used
		
		if(this.submoves == null) {
			return lowestVal;
		}
		
		// If we got here, then this leaf has children.
		// Search those children for the one with the lowest value, and return its column and move
		for(MoveTree children: this.submoves) {
			if(children.moveValue < lowestVal) {
				lowestVal = children.moveValue;
			}
		}
		
		return lowestVal;
	}
}
