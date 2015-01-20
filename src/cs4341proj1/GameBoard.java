package cs4341proj1;

import java.util.Arrays;

public class GameBoard {
	private static GameBoard instance = null;
	protected int[][] rowsCols;
	protected boolean p1pop = false;
	protected boolean p2pop = false;
	protected MoveTree[] submoves = null;
	private int plyscalculated = 0;
	
	protected GameBoard(int numRows, int numCols){
		rowsCols = new int[numRows][numCols];
		for (int i = 0; i < numRows; i++){
			Arrays.fill(rowsCols[i], 0);
		}
		
	}
	
	public static GameBoard getInstance(){
		if (instance == null){
			instance = new GameBoard(Config.getInstance().getNumRows(),
					Config.getInstance().getNumCol()); 
		}
		return instance;
	}
	
	public void applyMove(int player, int col, int movetype){
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
			if(nextOpenRow(col) < 0){
				return false;
			} else {
				return true;
			}
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
	
	public void genPossibleMoves(int player){
		MoveTree[] result = new MoveTree[rowsCols[0].length * 2];
		int n = 0;
		for (int i = 0; i < 2 ;i++){
			for (int j = 0; j < rowsCols[0].length; j++){
				result[n] = new MoveTree(this, player, j, i);
				n++;
			}
		}
		this.submoves = result;
	}
	

	public void calculatePly(){
		if (this.plyscalculated == 0){
			this.genPossibleMoves(1);
		} else {
			for(int i = 0; i < submoves.length; i++){
				submoves[i].calculatePly(this.plyscalculated - 1);
			}
		}
		plyscalculated++;
	}
	
	/**
	 * Function to search for the best possible move via minimax
	 * Gathers all the minimax values of its movetrees, then chooses the maximum value and returns it.
	 * Returns an array holding two ints: the column to move in, and the move type
	 */
	public int minimax() {
		int maxValue = 0;
		
		// Search through the moveTrees and to find the highest value of all the minimaxes.
		for(MoveTree children : this.submoves) {
			if(children.minimax() > maxValue){
				maxValue = children.minimax();
			}
		}
		
		return maxValue;
	}
}
