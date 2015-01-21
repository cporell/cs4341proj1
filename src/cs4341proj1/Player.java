package cs4341proj1;

import java.util.*;
import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Player {
	static Config conf;
	static Scanner in;// = new Scanner(System.in);
	static boolean first = false;
	static boolean firstplayed = false;
	static boolean playing = true;
	static GameBoard board;
	static Logger log;
	static String name = "Zinnia";
	//private static final Logger log = Logger.getLogger( ClassName.class.getName() );

	public static void main(String[] args) {
		if (args.length > 1){
			name = args[1];
		}
		Logger.init(args[0]);
		log = Logger.getInstance();
		log.print("started!!!!!");
		// TODO Auto-generated method stub
		//System.out.println("Test");
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
		//System.err.println(board);
		
		while(playing){
			if(first && !firstplayed){
				log.print("playing first");
				firstplayed = true;
				writeMove();
				//System.err.println(board);
			} else {
				log.print("reading move");
				readMove();
				//System.err.println(board);
				if(playing){
					log.print("writing move");
					writeMove();
					//System.err.println(board);
				}
			}
		}
		log.close();
	}
	
	private static void readMove() {
		String move = in.nextLine();
		log.print("read " + move);
		String[] moveparts = move.split(" ");
		if (moveparts.length == 2){
			board.applyMove((byte)2, Integer.parseInt(moveparts[0]), Integer.parseInt(moveparts[1]));
		} else {
			playing = false;
		}
		log.print("Opponent's move applied");
	}

	private static void writeMove() {
		//log.print(board.toString());
		FutureTask<Void> f = new FutureTask<Void>(new RunMinimax(), null);
		ExecutorService exec = Executors.newFixedThreadPool(1);
		exec.execute(f);
		
		try {
			f.get(conf.getTurnlen() - 2, TimeUnit.SECONDS);
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
		/*
		Random rand = new Random();
		int col = rand.nextInt(conf.getNumCol());
		int movetype = rand.nextInt(2);
		log.print(col + " " + movetype);
		while(!board.isMoveValid(1, col, movetype)){
			col = rand.nextInt(conf.getNumCol());
			movetype = rand.nextInt(2);
			log.print(col + " " + movetype);
		}
		*/
		//log.print(board.toString());
		int[] bestmove = board.getBestMove();
		log.print("best move = " + bestmove[0] + " " + bestmove[1]);
		board.applyMove((byte)1, bestmove[0], bestmove[1]);
		System.out.println(bestmove[0] + " " + bestmove[1]);
		log.print("best move printed");
		//log.print(bestmove[0] + " " + bestmove[1]);
		
	}

	static public void sendName(){
		System.out.println(name);
	}
	
	public static void readConfig(){
		int playernum;
		//log.print("about to read from stdin");
		String names = in.nextLine();
		//log.print(configs);
		//log.print("about to split config string");
		String[] parts = names.split(" ");
		if(parts[1].equals(name)){
			playernum = 0;
		} else {
			playernum = 1;
		}
		//for (int i = 0; i < parts.length; i++){
			//log.print(parts[i]);
		//}
		//log.print("about to construct Config");
		
		String configs = in.nextLine();
		parts = configs.split(" ");
		conf = Config.getInstance(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 
				Integer.parseInt(parts[2]), playernum, Integer.parseInt(parts[4]), 
				Integer.parseInt(parts[3]) - 1);
		//log.print("readConfig done");
	}

}
