package pl.grm.geocompression;

import java.io.*;
import java.net.*;

public class FileOperations {
	
	public static File getFile(String fileName) throws FileNotFoundException {
		ClassLoader classLoader = FileOperations.class.getClassLoader();
		URL resFile = classLoader.getResource(fileName);
		File file = new File(resFile.getFile());
		if (file == null || !file.exists()) { throw new FileNotFoundException(
				"Nie znaleziono pliku o nazwie " + fileName); }
		return file;
	}
	
	public static void saveOutputFile(Data dataOut) {
		// TODO Auto-generated method stub
		
	}
}
