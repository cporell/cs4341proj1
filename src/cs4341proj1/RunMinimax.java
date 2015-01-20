package cs4341proj1;

public class RunMinimax implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		GameBoard board = GameBoard.getInstance();
		board.minimax();
		for(int i = 0; i < 5; i++){
			board.calculatePly();
			board.minimax();
		}
	}

}
