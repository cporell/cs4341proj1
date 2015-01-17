package cs4341proj1;

import java.util.*;

public class Player {
	static Config conf;
	static Scanner in;// = new Scanner(System.in);
	static boolean first = false;
	static boolean firstplayed = false;
	static boolean playing = true;
	static GameBoard board;

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		//System.out.println("Test");
		in = new Scanner(System.in);
		sendName();
		readConfig();
		if (conf.getPlayernum() == 1){
			first = true;
		}
		board = new GameBoard(conf.getNumRows(), conf.getNumCol());
		System.err.println(board);
		
		while(playing){
			if(first && !firstplayed){
				writeMove();
				firstplayed = true;
			} else {
				readMove();
				if(playing){
					writeMove();
				}
			}
		}
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
		// TODO Auto-generated method stub
		
	}

	static public void sendName(){
		System.out.println("Zinnia");
	}
	
	public static void readConfig(){
		String configs = in.nextLine();
		String[] parts = configs.split(" ");
		conf = new Config(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 
				Integer.parseInt(parts[2]),Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
		
	}

}
