package cs4341proj1;

public class RunMinimax implements Runnable {

	@Override
	public void run() {
		try {
			Logger log = Logger.getInstance();
			GameBoard board = GameBoard.getInstance();

			log.print("About to minimax");
			board.minimax();
			//log.print(board.toString());
			while(!Thread.currentThread().isInterrupted()){
				//log.print("About to calculate Ply");
				boolean stuffchanged = board.calculatePly();
				//log.print(board.toString());
				//log.print("About to minimax");
				if (Thread.currentThread().isInterrupted()){
					break;
				}
				if(stuffchanged){
					board.minimax();
				}
				//log.print(board.toString());
			}

		} catch (Exception e) {
			Logger.getInstance().print("Unhandled exception in RunMinimax thread");
			Logger.getInstance().print(e.toString());
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement s : trace){
				Logger.getInstance().print(s.toString() + "\n\t\t");
			}
			
		}
	}
}
