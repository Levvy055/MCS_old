package pl.grm.geocompression;

import java.util.*;

import pl.grm.misc.*;

public class Compressor {
	private Data							dataIn;
	private Data							dataOut;
	private HashMap<Integer, GeoPosition>	geoPositions;
	private HashMap<Float, ValuePositions>	valPositions;
	
	public Compressor() {
		dataOut = new Data();
		valPositions = new HashMap<>();
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
		Logger.print("Input:", dataIn.getDataAsList(), false);
		@SuppressWarnings("unchecked")
		HashMap<Integer, GeoPosition> geoPositionsT = (HashMap<Integer, GeoPosition>) dataIn
				.getDataAsMap().clone();
		geoPositions = geoPositionsT;
		int positionsCount = geoPositions.size();
		dataOut.addString(positionsCount + "g");
		List<GeoPosition> listData = dataIn.getDataAsList();
		System.out.println("Compressing ");
		for (GeoPosition gP : listData) {
			long lpm = gP.getLpm();
			float x = gP.getX();
			float y = gP.getY();
			if (x == y) {
				addValue(x, lpm, Data.ALL_I);
			} else {
				addValue(x, lpm, Data.X_I);
				addValue(y, lpm, Data.Y_I);
			}
			System.out.print(".");
		}
		System.out.println("Saving ");
		Iterator<Float> it = valPositions.keySet().iterator();
		while (it.hasNext()) {
			Float v = it.next();
			ValuePositions vP = valPositions.get(v);
			dataOut.addString(v + vP.toSimplifiedString());
			System.out.print(".");
		}
		Logger.print("values in map: ", valPositions, true);
		Logger.print("Output: ", dataOut.getDataLines(), false);
	}
	
	private void addValue(float value, long index, byte position) {
		ValuePositions vP;
		if (valPositions.containsKey(value)) {
			vP = valPositions.get(value);
		} else {
			vP = new ValuePositions();
			valPositions.put(value, vP);
		}
		vP.put(index, position);
	}
	
	public Data getComprossedData() {
		return dataOut;
	}
}
