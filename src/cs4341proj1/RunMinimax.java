package cs4341proj1;

public class RunMinimax implements Runnable {

	@Override
	public void run() {
		try {
			//Logger log = Logger.getInstance();
			GameBoard board = GameBoard.getInstance();
			int i = 1;

			while(!Thread.currentThread().isInterrupted()){
				
				//log.print(board.toString());
				//log.print("About to minimax");
				board.minimax(i);
				Logger.getInstance().print("Calculated " + i + "plys");
				//System.gc();
				//log.print(board.toString());
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
