package cs4341proj1;

public class Coords {
	private final int row;
	private final int column;
	
	public Coords(int row, int column){
		this.row = row;
		this.column = column;
	}
	
	public int getRow(){
		return this.row;
	}
	
	public int getColumn(){
		return this.column;
	}
	
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
