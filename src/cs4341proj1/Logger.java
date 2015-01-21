package cs4341proj1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Logger {
	PrintWriter writer;
	private static Logger instance;
	
	private Logger(String logfile){
		
		try {
			this.writer = new PrintWriter(logfile, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}
	
	public static void init(String logfile){
		instance = new Logger(logfile);
	}
	
	public static Logger getInstance(){
		return instance;
	}
	
	public void print(String s){
		writer.println(s);
		writer.flush();
	}
	
	public void close(){
		writer.close();
	}
}
