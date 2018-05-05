package taxi_gui;


import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Log {

	private DateTimeFormatter dtf;
	private PrintWriter pwriter;
	private LocalDateTime currentdatetime;
	
	public Log(){
		initlog();
	}
	
	public void initlog() {
		dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		currentdatetime = LocalDateTime.now();
		
		try {
			pwriter = new PrintWriter(new FileOutputStream( new File("log.txt"), true));
		} 
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		
		String str_currentdatetime = dtf.format(currentdatetime);	
		pwriter.println();
		pwriter.printf("### Logger Initialized at %s ###",str_currentdatetime);
		pwriter.println();
	}	
	
	public void writelog(String str){
		System.out.println(str);
		pwriter.println( str );
	}
	
	public void close() {
		pwriter.close();
	}
	
	public void flush(){
		pwriter.flush();
	}
	
}
