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
	 *            0 - gps positions(no compression), 1 - one stage only
	 *            compression, 2 -better
	 *            compression
	 * @param for0TypeExpLvl
	 *            0 - simplest -> 3 - full
	 * @throws IOException
	 */
	public static void saveOutputFile(Data data, String fileName, int type, int for0TypeExpLvl)
			throws IOException {
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
		if (data != null) {
			OutputStreamWriter fW;
			FileOutputStream fos;
			switch (type) {
				case 0 :
					List<GeoPosition> listG = data.getDataAsList();
					fW = new FileWriter(file);
					for (int i = 0; i < listG.size(); i++) {
						GeoPosition gp = listG.get(i);
						switch (for0TypeExpLvl) {
							case 0 :
								fW.write(gp.toMinimalLikeIntString().concat(
										(i == (listG.size() - 1) ? "" : "\r\n")));
								break;
							case 1 :
								fW.write(gp.toMinimalString().concat("\r\n"));
								break;
							case 2 :
								fW.write(gp.toSimplifiedString().concat("\r\n"));
								break;
							case 3 :
								fW.write(gp.toString().concat("\r\n"));
								break;
							default :
								break;
						}
					}
					fW.flush();
					fW.close();
					break;
				case 1 :
					fW = new FileWriter(file);
					List<String> listL = data.getStringList();
					for (String line : listL) {
						fW.write(line);
					}
					fW.flush();
					fW.close();
					break;
				case 2 :
					fos = new FileOutputStream(file);
					List<byte[]> listB = data.getByteList();
					for (byte[] line : listB) {
						fos.write(line);
					}
					fos.flush();
					fos.close();
					break;
				case 3 :
					fos = new FileOutputStream(file);
					byte[] bytes = data.getCByteList();
					fos.write(bytes);
					fos.flush();
					fos.close();
					break;
				default :
					break;
			}
		}
		MLog.info("Output File Saved");
	}
}
