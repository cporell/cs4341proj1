package cs4341proj1;

import java.util.*;


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
		log = new Logger(args[0]);
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
		board = new GameBoard(conf.getNumRows(), conf.getNumCol());
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
		String[] moveparts = move.split(" ");
		if (moveparts.length == 2){
			board.applyMove(2, Integer.parseInt(moveparts[0]), Integer.parseInt(moveparts[1]));
		} else {
			playing = false;
		}
	}

	private static void writeMove() {
		log.print(board.toString());
		Random rand = new Random();
		int col = rand.nextInt(conf.getNumCol());
		int movetype = rand.nextInt(2);
		log.print(col + " " + movetype);
		while(!board.isMoveValid(1, col, movetype)){
			col = rand.nextInt(conf.getNumCol());
			movetype = rand.nextInt(2);
			log.print(col + " " + movetype);
		}
		log.print(board.toString());
		board.applyMove(1, col, movetype);
		System.out.println(col + " " + movetype);
		log.print(col + " " + movetype);
		
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
