package pl.grm.geocompression;

import java.io.*;
import java.net.*;
import java.util.*;

public class FileOperations {
	
	public static File getFile(String fileName) throws FileNotFoundException {
		ClassLoader classLoader = FileOperations.class.getClassLoader();
		URL resFile = classLoader.getResource(fileName);
		File file;
		if (resFile != null) {
			file = new File(resFile.getFile());
		} else {
			file = new File(fileName);
		}
		if (file == null || !file.exists()) { throw new FileNotFoundException(
				"Nie znaleziono pliku o nazwie " + fileName); }
		return file;
	}
	
	public static void saveOutputFile(Data data, String fileName, boolean saveGeoPosition)
			throws IOException {
		File file = new File(fileName + ".txt");
		int nmb = 1;
		if (file.exists()) {
			File tFile;
			do {
				tFile = new File(fileName + "." + nmb + ".txt");
				nmb++;
			}
			while (tFile.exists() && nmb < 1);
			file.renameTo(tFile);
			file = new File(fileName + ".txt");
		}
		FileWriter fW = new FileWriter(file);
		if (data != null) {
			if (saveGeoPosition) {
				List<GeoPosition> list = data.getDataAsList();
				for (GeoPosition gp : list) {
					fW.write(gp.toString());
				}
			} else {
				List<String> list = data.getDataLines();
				for (String line : list) {
					fW.write(line);
				}
			}
		}
		fW.flush();
		fW.close();
	}
}
