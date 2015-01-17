package cs4341proj1;

public class Config {
	private int numRows;
	private int numCol;
	private int numWin;
	private int playernum;
	private int turnlen;
	private int firstplayer;
	
	Config(int numRows, int numCol, int numWin, int playernum, int turnlen, int firstplayer){
		this.numRows = numRows;
		this.numCol	= numCol;
		this.numWin = numWin;
		this.playernum = playernum;
		this.turnlen = turnlen;
		this.firstplayer = firstplayer;
	}

	/**
	 * @return the numRows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * @return the numCol
	 */
	public int getNumCol() {
		return numCol;
	}

	/**
	 * @return the numWin
	 */
	public int getNumWin() {
		return numWin;
	}

	/**
	 * @return the playernum
	 */
	public int getPlayernum() {
		return playernum;
	}

	/**
	 * @return the turnlen
	 */
	public int getTurnlen() {
		return turnlen;
	}

	/**
	 * @return the firstplayer
	 */
	public int getFirstplayer() {
		return firstplayer;
	}

}
