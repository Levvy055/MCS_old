package pl.grm;

import java.io.*;

import pl.grm.geocompression.*;

public class Main {
	private static Data	dataIn	= new Data();
	private static Data	dataOut	= null;
	
	public static void main(String[] args) {
		try {
			if (args.length == 2 || args.length == 3) {
				switch (args[0]) {
					case "compress" :
						compression(args);
						break;
					case "decompress" :
						decompression(args);
						break;
					default :
						throw new ArgumentException("compress or decompress not specified!");
				}
			} else {
				System.out.println("Zla ilosc parametrow!");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void compression(String[] args) throws IOException, ArgumentException {
		Compressor compressor;
		Data dataOut;
		if (args.length == 2) {
			String filename = args[1];
			dataIn.loadDataFromFile(filename);
		} else if (args.length == 3) {
			int testID;
			if (args[1].equals("-test")) {
				if ((testID = Integer.parseInt(args[2])) > 0 && testID < 5) {
					dataIn.loadTestDataToCompress(testID);
				} else {
					throw new ArgumentException("Brak testu dla wskazanego ID");
				}
			} else {
				throw new ArgumentException("Bledna ilosc argumentow!");
			}
		}
		compressor = new Compressor(dataIn);
		compressor.compress();
		dataOut = compressor.getComprossedData();
		FileOperations.saveOutputFile(dataOut, "dane_compress.txt");
	}
	
	private static void decompression(String[] args) throws IOException, ArgumentException {
		Decompressor Decompressor;
		Data dataOut;
		if (args.length == 2) {
			String filename = args[1];
			dataIn.loadDataFromFile(filename);
		} else if (args.length == 3) {
			int testID;
			if (args[1].equals("-test")) {
				if ((testID = Integer.parseInt(args[2])) > 0 && testID < 5) {
					dataIn.loadTestDataToCompress(testID);
				} else {
					throw new ArgumentException("Brak testu dla wskazanego ID");
				}
			} else {
				throw new ArgumentException("Bledna ilosc argumentow!");
			}
		}
		Decompressor = new Decompressor(dataIn);
		Decompressor.decompress();
		dataOut = Decompressor.getDecomprossedData();
		FileOperations.saveOutputFile(dataOut, "dane_decompress.txt");
	}
}
