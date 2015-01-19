package cs4341proj1;

public class MoveTree extends GameBoard {
	
	private boolean isvalid;
	private int player;
	
	MoveTree(GameBoard current, int player, int col, int movetype) {
		super(current.rowsCols.length, current.rowsCols[0].length);
		this.rowsCols = current.rowsCols;
		this.p1pop = current.p1pop;
		this.p2pop = current.p2pop;
		this.player = player;
		
		this.isvalid = this.isMoveValid(player, col, movetype);
		
		if (this.isvalid){
			this.applyMove(player, col, movetype);
		}
		
	}
	
	public boolean getIsValid(){
		return this.isvalid;
	}
	
	
	public MoveTree[] genPossibleMoves(){
		if (this.player == 1){
			return super.genPossibleMoves(2);
		} else {
			return super.genPossibleMoves(1);
		}
	}

}
