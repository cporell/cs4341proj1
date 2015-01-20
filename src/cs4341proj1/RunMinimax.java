package cs4341proj1;

public class RunMinimax implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Logger log = Logger.getInstance();
		GameBoard board = GameBoard.getInstance();
		
		log.print("About to minimax");
		board.minimax();
		log.print(board.toString());
		while(!Thread.currentThread().isInterrupted()){
			log.print("About to calculate Ply");
			board.calculatePly();
			log.print(board.toString());
			log.print("About to minimax");
			if (Thread.currentThread().isInterrupted()){
				break;
			}
			board.minimax();
			log.print(board.toString());
		}
	}

}
