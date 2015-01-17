package cs4341proj1;

import java.util.*;

public class Player {
	static Config conf;
	static Scanner in;// = new Scanner(System.in);
	static boolean first = false;
	static boolean firstplayed = false;
	static boolean playing = true;

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		//System.out.println("Test");
		in = new Scanner(System.in);
		sendName();
		readConfig();
		if (conf.getPlayernum() == 1){
			first = true;
		}
		
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
		// TODO Auto-generated method stub
		
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
