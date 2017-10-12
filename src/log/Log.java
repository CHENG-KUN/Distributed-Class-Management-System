package log;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public static void write(String message, String path) {
		
		try {
			
			FileWriter out = new FileWriter(path, true);
			
			Date now = new Date(); 
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			String time = dateFormat.format(now);
			
			out.append(time + " : " + message + "\r\n");
			out.close();
			
		} catch (IOException e) {
			System.out.println("Log file path is not exist.");
		}
		
	}
	
}
