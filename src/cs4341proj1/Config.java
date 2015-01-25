package cs4341proj1;


// Config class to store the initial configuration of the game board.
// It is set when the game is created. Future events can request the game
// information by calling this class' Getters.
public class Config {
	private int numRows;
	private int numCol;
	private int numWin;
	private int playernum;
	private int turnlen;
	private int firstplayer;
	
	// Config is singleton.
	private static Config instance = null;
	
	// Build the Config using the gameboard info
	private Config(int numRows, int numCol, int numWin, int playernum, int turnlen, int firstplayer){
		this.numRows = numRows;
		this.numCol	= numCol;
		this.numWin = numWin;
		this.playernum = playernum;
		this.turnlen = turnlen;
		this.firstplayer = firstplayer;
	}
	
	// Get the first instance of the Config, initializing it in the process
	public static Config getInstance(int numRows, int numCol, int numWin, int playernum, int turnlen, int firstplayer){
		instance = new Config(numRows, numCol, numWin, playernum, turnlen, firstplayer);
		return instance;
	}
	
	// Get the instance of the Config singleton.
	public static Config getInstance(){
		return instance;
	}

	// Getters for the Config singleton. Need to call getInstance and append these to the dot-train.
	
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
