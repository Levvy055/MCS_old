package pl.grm;

import java.io.*;

import pl.grm.geocompression.*;
import pl.grm.misc.*;

public class Main {
	
	public static void main(String[] args) {
		boolean logON = false;
		boolean test = false;
		boolean compress = false;
		boolean decompress = false;
		int iKC = 0;
		int testID = 0;
		String filename = "";
		try {
			if (args.length == 2 || args.length == 3 || args.length == 4) {
				for (int i = 0; i < args.length; i++) {
					String string = args[i];
					switch (string) {
						case "-l" :
							logON = true;
							break;
						case "-test" :
							test = true;
							testID = Integer.parseInt(args[i + 1]);
							break;
						case "compress" :
							compress = true;
							iKC = i;
							break;
						case "decompress" :
							decompress = true;
							iKC = i;
							break;
						default :
							break;
					}
				}
				if (!test) {
					if (args.length >= iKC + 1) {
						filename = args[iKC + 1];
					} else {
						throw new ArgumentException("file not specified!");
					}
				}
				MLog.ON = logON;
				if (compress) {
					compression(test, testID, filename);
				} else if (decompress) {
					decompression(filename);
				} else {
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
	
	private static void compression(boolean test, int testID, String filename) throws IOException,
			ArgumentException {
		Compressor compressor;
		Data dataIn = new Data();
		Data dataOut;
		if (test) {
			if (testID > 0 && testID < 3) {
				dataIn.loadTestDataToCompress(testID);
			} else {
				throw new ArgumentException("Brak testu dla wskazanego ID");
			}
		} else {
			dataIn.loadUncompressedDataFromFile(filename);
		}
		compressor = new Compressor(dataIn);
		compressor.compress();
		dataOut = compressor.getComprossedData();
		FileOperations.saveOutputFile(dataOut, "dane_compress", 3, 0);
	}
	
	private static void decompression(String filename) throws IOException {
		Decompressor Decompressor;
		Data dataIn = new Data();
		Data dataOut = null;
		dataIn.loadCompressedDataFromFile(filename);
		Decompressor = new Decompressor(dataIn);
		try {
			Decompressor.decompress();
			dataOut = Decompressor.getDecomprossedData();
		}
		catch (Exception e) {
			MLog.info(e.getMessage());
			e.printStackTrace();
		}
		FileOperations.saveOutputFile(dataOut, "dane_decompress", 0, 0);
	}
}
