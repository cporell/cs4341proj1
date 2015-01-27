package cs4341proj1;
//CS 4341 Project 1
//Andrew Roskuski
//Connor Porell

// Simple class to hold a set of coords.
// Holds a row # and a column #.
public class Coords {
	private final int row;
	private final int column;
	
	// Make a new Coord
	public Coords(int row, int column){
		this.row = row;
		this.column = column;
	}
	
	// Getters for the Coord class
	public int getRow(){
		return this.row;
	}
	
	public int getColumn(){
		return this.column;
	}
	
	// Overrides Java's Object.equals so we can check if two coords are identical.
	@Override
	public boolean equals(Object c){
		if(c instanceof Coords){
			if(((Coords) c).row == this.row && ((Coords)c).column == this.column){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
