package pl.grm.geocompression;

import java.util.*;

import com.google.common.collect.*;

public class Compressor {
	private Data	dataIn;
	private Data	dataOut;
	
	public Compressor() {
		dataOut = new Data();
	}
	
	public Compressor(Data dataIn) {
		this();
		this.dataIn = dataIn;
	}
	
	public void compress(Data dataIn) {
		this.dataIn = dataIn;
		compress();
	}
	
	public void compress() {
		if (dataIn == null) { return; }
		printInputData();
		final HashMap<Integer, GeoPosition> geoPositions = dataIn.getDataAsMap();
		int positionsCount = geoPositions.size();
		dataOut.addString("g" + positionsCount + "g");
		final List<GeoPosition> listData = dataIn.getDataAsList();
		List<GeoPosition> duplicates = Math.getDuplicates(listData);
		print("Duplikaty x&y:", duplicates);
		HashMultimap<Float, Integer> sDuplicates = HashMultimap.create();
		ArrayList<Float> tsD = new ArrayList<Float>();
		for (GeoPosition geoPosition : listData) {
			float x = geoPosition.getX();
			float y = geoPosition.getY();
			tsD.add(x);
			tsD.add(y);
		}
		if (Math.hasDuplicate(tsD)) {
			ArrayList<Float> tsDC = (ArrayList<Float>) tsD.clone();
			while (Math.hasDuplicate(tsDC)) {
				List dDC = Math.getDuplicates(tsDC);
				tsDC.removeAll(dDC);
			}
			
			print("Duplikat tsDC", tsDC);
		}
	}
	
	private static void print(String type, Collection list) {
		System.out.println("==============================================");
		System.out.println(type);
		for (Object obj : list) {
			System.out.println(obj.toString());
		}
	}
	
	public void printInputData() {
		List<GeoPosition> data = dataIn.getDataAsList();
		print("Input:", data);
	}
	
	public Data getComprossedData() {
		return dataOut;
	}
}
