package cs4341proj1;

//import java.util.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// Player class holds the player info and plays the game.
// It stores the game configuration, a Scanner, various game info, and the AI's name.
public class Player {
	static Config conf;
	static Scanner in;// = new Scanner(System.in);
	static boolean first = false;
	static boolean firstplayed = false;
	static boolean playing = true;
	static GameBoard board;
	static Logger log;
	static String name = "Zinnia";

	// Runs the player. At the start, sends all the necessary info to the Referee and the logger.
	public static void main(String[] args) {
		if (args.length > 0){
			name = args[0];
		}
		Logger.init(name + "_log.txt");
		log = Logger.getInstance();
		log.print("started!!!!!");
		in = new Scanner(System.in);
		log.print("About to send Name");
		sendName();
		log.print("About to read config");
		readConfig();
		if (conf.getPlayernum() == conf.getFirstplayer()){
			first = true;
		}
		
		log.print("generating game board");
		board = GameBoard.getInstance();
		
		// The game loop. Handles behavior until a game over.
		while(playing){
			try{
				// If we go first, play towards the middle of the board.
				if(first && !firstplayed){
					log.print("playing first");
					firstplayed = true;
					//writeMove();
					int[] bestmove = {conf.getNumCol() / 2 , 1};
					board.applyMove((byte)1, bestmove[0], bestmove[1]);
					log.print("best move = " + bestmove[0] + " " + bestmove[1]);
					System.out.println(bestmove[0] + " " + bestmove[1]);
					//System.err.println(board);
				} else {
					// Else, if we don't go first, play to adapt to the opponent's first move.
					log.print("reading move");
					readMove();
					//System.err.println(board);
					if(playing){
						log.print("writing move");
						writeMove();
						//System.err.println(board);
					} else {
						log.print("End of game, skipping writemove");
					}
				}
			} catch (Exception e){
				// Oops! If we got here, something went wrong.
				log.print("Unhandled Exception in main thread");
				log.print(e.toString());
				StackTraceElement[] trace = e.getStackTrace();
				for (StackTraceElement s : trace){
					log.print(s.toString() + "\n\t\t");
				}
				playing = false;
			}
		}
		log.print("About to quit");
		log.close();
		System.exit(0);
	}
	
	// Reads in a move. If it is of the proper format, write it to the gameboard.
	private static void readMove() {
		while(!in.hasNextLine()){
			
		}
		String move = in.nextLine();
		log.print("read " + move);
		String[] moveparts = move.split(" ");
		if (moveparts.length == 2){
			board.applyMove((byte)2, Integer.parseInt(moveparts[0]), Integer.parseInt(moveparts[1]));
			log.print("Opponent's move applied");
		} else {
			log.print("Set playing to false");
			playing = false;
		}
		
	}

	// Writes a move to the gameboard.
	// This part also handles the thread. The thread is used for timing purposes- if we have a timeout,
	// we can end the thread easily and base our move on what's been done so far.
	private static void writeMove() {
		FutureTask<Void> f = new FutureTask<Void>(new RunMinimax(), null);
		ExecutorService exec = Executors.newFixedThreadPool(1);
		exec.execute(f);
		
		try {
			f.get((conf.getTurnlen() * 1000) - 500, TimeUnit.MILLISECONDS);
			//f.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			log.print("Cancelling thread");
			f.cancel(true);
			//e.printStackTrace();
		}
		exec.shutdown();

		int[] bestmove = board.getBestMove();
		log.print("best move = " + bestmove[0] + " " + bestmove[1]);
		board.applyMove((byte)1, bestmove[0], bestmove[1]);
		System.out.println(bestmove[0] + " " + bestmove[1]);
		
	}

	// Send the player name to the Ref.
	static public void sendName(){
		System.out.println(name);
	}
	
	// Reads in the configs from the Ref, and stores it in the Config object.
	public static void readConfig(){
		int playernum;
		while(!in.hasNextLine()){
			
		}
		String names = in.nextLine();
		String[] parts = names.split(" ");
		if(parts[1].equals(name)){
			playernum = 0;
		} else {
			playernum = 1;
		}
		while(!in.hasNextLine()){
			
		}
		String configs = in.nextLine();
		parts = configs.split(" ");
		conf = Config.getInstance(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 
				Integer.parseInt(parts[2]), playernum, Integer.parseInt(parts[4]), 
				Integer.parseInt(parts[3]) - 1);
	}

}
