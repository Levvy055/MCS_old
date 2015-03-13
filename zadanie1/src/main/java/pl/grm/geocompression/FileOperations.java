package pl.grm.geocompression;

import java.io.*;
import java.net.*;
import java.util.*;

import pl.grm.misc.*;

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
	
	/**
	 * @param data
	 * @param fileName
	 * @param type
	 *            0 - gps positions, 1 - one time compression, 2 -better
	 *            compression
	 * @throws IOException
	 */
	public static void saveOutputFile(Data data, String fileName, int type) throws IOException {
		MLog.info("Saving");
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
			switch (type) {
				case 0 :
					List<GeoPosition> listG = data.getDataAsList();
					for (GeoPosition gp : listG) {
						fW.write(gp.toString() + "\r\n");
					}
					break;
				case 1 :
					List<String> listL = data.getDataLines();
					for (String line : listL) {
						fW.write(line);
					}
					break;
				case 2 :
					List<byte[]> listB = data.getFinalByteOutput();
					for (byte[] line : listB) {
						fW.write(new String(line));
					}
					break;
				default :
					break;
			}
		}
		fW.flush();
		fW.close();
		MLog.info("Output file Saved");
	}
}
