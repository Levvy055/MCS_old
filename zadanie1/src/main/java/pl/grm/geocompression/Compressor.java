package pl.grm.geocompression;

import java.util.*;

public class Compressor {
	private Data							dataIn;
	private Data							dataOut;
	private HashMap<Integer, GeoPosition>	geoPositions;
	private HashMap<Float, ValuePosition>	duplicatingValues;
	
	public Compressor() {
		dataOut = new Data();
		duplicatingValues = new HashMap<Float, ValuePosition>();
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
		print("Input:", dataIn.getDataAsList(), false);
		@SuppressWarnings("unchecked")
		HashMap<Integer, GeoPosition> geoPositionsT = (HashMap<Integer, GeoPosition>) dataIn
				.getDataAsMap().clone();
		geoPositions = geoPositionsT;
		int positionsCount = geoPositions.size();
		dataOut.addString(positionsCount + "g");
		List<GeoPosition> listData = dataIn.getDataAsList();
		Set<GeoPosition> duplicates = Math.getUniqueValues(Math.getDuplicates(listData));
		print("Duplikujace sie pozycje:", duplicates, false);
		addDuplicatingGP(duplicates);
		ArrayList<Float> tsD = new ArrayList<Float>();
		for (GeoPosition geoPosition : listData) {
			float x = geoPosition.getX();
			float y = geoPosition.getY();
			tsD.add(x);
			tsD.add(y);
		}
		if (Math.hasDuplicate(tsD)) {
			Set<Float> tsDC = Math.getUniqueValues(tsD);
			print("Duplikujace sie wartosci:", tsDC, true);
			addDuplicatingValues(tsDC);
			print("DPGP IDs: ", duplicatingValues, false);
		}
		print("Output: ", dataOut.getDataLines(), false);
	}
	
	private void addDuplicatingGP(Set<GeoPosition> duplicates) {
		ValuePosition xMap;
		ValuePosition yMap;
		for (GeoPosition geoPosition : duplicates) {
			long lpm = geoPosition.getLpm();
			float vX = geoPosition.getX();
			float vY = geoPosition.getY();
			if (duplicatingValues.containsKey(vX)) {
				xMap = duplicatingValues.get(vX);
			} else {
				xMap = new ValuePosition();
			}
			if (vY == vX) {
				xMap.put(lpm, Data.ALL_I);
				duplicatingValues.put(vX, xMap);
			} else {
				xMap.put(lpm, Data.X_I);
				duplicatingValues.put(vX, xMap);
				if (duplicatingValues.containsKey(vY)) {
					yMap = duplicatingValues.get(vY);
				} else {
					yMap = new ValuePosition();
				}
				yMap.put(lpm, Data.Y_I);
				duplicatingValues.put(vY, yMap);
			}
		}
	}
	
	private void addDuplicatingValues(Set<Float> values) {
		ValuePosition vMap;
		for (GeoPosition geoPosition : geoPositions.values()) {
			for (Float v : values) {
				if (geoPosition.contains(v)) {
					byte pos = geoPosition.getPosByValue(v);
					if (duplicatingValues.containsKey(v)) {
						vMap = duplicatingValues.get(v);
					} else {
						vMap = new ValuePosition();
					}
					vMap.put(geoPosition.getLpm(), pos);
					duplicatingValues.put(v, vMap);
				}
			}
		}
	}
	
	public void print(String type, HashMap<Float, ValuePosition> map, boolean sorted) {
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<Float> it = map.keySet().iterator(); it.hasNext();) {
			Float v = it.next();
			ValuePosition vP = map.get(v);
			list.add("");
			list.add("Value: " + v);
			list.addAll(vP.toStringFullList());
			
		}
		print(type, list, sorted);
	}
	
	public void print(String type, Set set, boolean sorted) {
		List list = new ArrayList(set);
		print(type, list, sorted);
	}
	
	public static void print(String type, List list, boolean sorted) {
		System.out.println("==============================================");
		System.out.println(type);
		if (sorted) {
			Collections.sort(list);
		}
		for (Object obj : list) {
			System.out.println(obj.toString());
		}
	}
	
	public Data getComprossedData() {
		return dataOut;
	}
}
