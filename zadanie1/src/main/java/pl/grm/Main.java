package pl.grm;

import java.io.*;
import java.util.*;

import pl.grm.geocompression.*;

public class Main {
	
	public static void main(String[] args) {
		if (args.length > 0) {
			Compress compress = null;
			Data dataIn = new Data();
			Data dataOut = null;
			if (args.length == 1 || (args.length == 2 && args[0].equals("-test"))) {
				try {
					if (args.length == 1) {
						String filename = args[0];
						dataIn.loadDataFromFile(filename);
					} else {
						try {
							int testID;
							if ((testID = Integer.parseInt(args[1])) > 0 && testID < 5) {
								dataIn.loadTestData(testID);
								List<GeoPosition> data = dataIn.getData();
								for (GeoPosition geoPosition : data) {
									System.out.println(geoPosition.toString());
								}
							} else {
								System.out.println("Brak testu dla wskazanego ID");
							}
						}
						catch (NumberFormatException e) {
							e.printStackTrace();
						}
						catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					compress = new Compress(dataIn);
					compress.compress();
					dataOut = compress.getComprossedData();
					FileOperations.saveOutputFile(dataOut);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			} else {
				System.out.println("Podales za duzo parametrow!");
			}
		} else {
			System.out.println("Nie podales nazwy pliku!");
		}
	}
}
