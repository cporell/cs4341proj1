package cs4341proj1;

public class RunMinimax implements Runnable {

	// Runs the minimax during move evaluation.
	@Override
	public void run() {
		try {
			//Logger log = Logger.getInstance();
			GameBoard board = GameBoard.getInstance();
			int i = 1;

			// As long as the thread is active, continue to work.
			while(!Thread.currentThread().isInterrupted()){
				board.minimax(i);
				Logger.getInstance().print("Calculated " + i + "plys");
				i++;
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
