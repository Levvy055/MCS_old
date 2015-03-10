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
	
	public static void saveOutputFile(Data dataOut, String fileName) {
		File file = new File(fileName);
		int nmb = 1;
		if (file.exists()) {
			File tFile;
			do {
				tFile = new File(fileName + "." + nmb);
				nmb++;
			}
			while (tFile.exists());
			File file2 = new File(fileName + "." + nmb);
			file.renameTo(file2);
			file = new File(fileName);
		}
	}
}
