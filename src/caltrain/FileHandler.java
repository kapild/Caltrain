package caltrain;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class FileHandler {

	
	public static BufferedReader getReader(String file) throws FileNotFoundException{
		
			return new BufferedReader(new FileReader(new File(file)));
	}
}
