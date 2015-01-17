package cs4341proj1;

import java.util.Arrays;

public class GameBoard {
	private int[][] rowsCols;
	
	GameBoard(int numRows, int numCols){
		rowsCols = new int[numRows][numCols];
		for (int i = 0; i < numRows; i++){
			Arrays.fill(rowsCols[i], 0);
		}
		
	}
	
	public void applyMove(int player, int col, int movetype){
		if (movetype == 0){//pop
			//rowsCols[rowsCols.length - 1][col] = 0;
			for(int i = rowsCols.length - 1; i > 0; i--){
				rowsCols[i][col] = rowsCols[i - 1][col];
			}
			rowsCols[0][col] = 0;
		} else {//drop
			rowsCols[nextOpenRow(col)][col] = player;
		}
	}
	
	private int nextOpenRow(int col){
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
}
