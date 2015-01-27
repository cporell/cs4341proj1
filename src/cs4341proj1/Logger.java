package cs4341proj1;
//CS 4341 Project 1
//Andrew Roskuski
//Connor Porell

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

// Logger class used to write certain events to a text file.
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
	
	// Start the logfile.
	public static void init(String logfile){
		instance = new Logger(logfile);
	}
	
	// Return the only instance of the logfile.
	public static Logger getInstance(){
		return instance;
	}
	
	// Write a statement to the logfile.
	public void print(String s){
		writer.println(s);
		writer.flush();
	}
	
	// When we're done with the logfile, close it.
	public void close(){
		writer.close();
	}
}
