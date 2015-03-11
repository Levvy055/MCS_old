package pl.grm.geocompression;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

public class Data {
	private HashMap<Integer, GeoPosition>	geoPositions;
	private ArrayList<String>				dataInLines;
	
	public Data() {
		this.dataInLines = new ArrayList<String>();
		this.geoPositions = new HashMap<Integer, GeoPosition>();
	}
	
	public void addPositionAfterLast(GeoPosition geoPosition) {
		geoPositions.put(getLast() + 1, geoPosition);
	}
	
	public int getLast() {
		int vMax = 0;
		Iterator<Integer> iterator = geoPositions.keySet().iterator();
		while (iterator.hasNext()) {
			Integer v = iterator.next();
			vMax = vMax < v ? v : vMax;
		}
		return vMax;
	}
	
	public void loadUncompressedDataFromFile(String filename) throws IOException {
		File file = FileOperations.getFile(filename);
		loadUncompressedDataFromFile(file);
	}
	
	public void loadTestDataToCompress(int testID) throws IOException {
		String fileName = "example_Data/Data" + testID + ".txt";
		File file = FileOperations.getFile(fileName);
		loadUncompressedDataFromFile(file);
	}
	
	public void loadUncompressedDataFromFile(File file) throws IOException {
		if (file == null || !file.exists()) { throw new FileNotFoundException("Plik "
				+ file.getName() + " nie istnieje!"); }
		Stream<String> lines = Files.lines(file.toPath());
		Stream<String[]> splittedLines = lines.map(line -> line.split(","));
		Stream<GeoPosition> mappedLines = splittedLines.map(snippets -> new GeoPosition(Float
				.parseFloat(snippets[0]), Float.parseFloat(snippets[1])));
		List<GeoPosition> list = mappedLines.collect(Collectors.toList());
		for (GeoPosition geoPosition : list) {
			int lpm = getLast() + 1;
			geoPosition.setLpm(lpm);
			this.geoPositions.put(lpm, geoPosition);
		}
		lines.close();
	}
	
	public void addString(String str) {
		dataInLines.add(str);
	}
	
	public void removeLast() {
		dataInLines.remove(dataInLines.size() - 1);
	}
	
	public HashMap<Integer, GeoPosition> getDataAsMap() {
		return geoPositions;
	}
	
	public List<GeoPosition> getDataAsList() {
		List<GeoPosition> list = new ArrayList<GeoPosition>();
		Iterator<Entry<Integer, GeoPosition>> iterator = geoPositions.entrySet().iterator();
		while (iterator.hasNext()) {
			GeoPosition position = iterator.next().getValue();
			list.add(position);
		}
		return list;
	}
	
	public List<String> getDataLines() {
		return dataInLines;
	}
	
	public void loadCompressedDataFromFile(String filename) {
		// TODO Auto-generated method stub
		
	}
}
