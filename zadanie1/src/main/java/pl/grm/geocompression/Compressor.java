package pl.grm.geocompression;

import java.util.*;

public class Compressor {
	private Data	dataIn;
	private Data	dataOut;
	
	public Compressor(Data dataIn) {
		this.dataIn = dataIn;
	}
	
	public void compress(Data dataIn) {
		this.dataIn = dataIn;
		compress();
	}
	
	public void compress() {
		if (dataIn == null) { return; }
		printInputData();
		final List<GeoPosition> geoPositions = dataIn.getData();
		int linesCount = geoPositions.size();
		List<GeoPosition> duplicates = Math.getDuplicate(geoPositions);
		print(duplicates);
	}
	
	private static void print(List list) {
		System.out.println("Output:");
		for (Object obj : list) {
			System.out.println(obj.toString());
		}
	}
	
	public void printInputData() {
		List<GeoPosition> data = dataIn.getData();
		System.out.println("Input:");
		print(data);
	}
	
	public Data getComprossedData() {
		return dataOut;
	}
}
