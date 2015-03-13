package pl.grm.geocompression;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import pl.grm.misc.*;

public class Data {
	private HashMap<Long, GeoPosition>	geoPositions;
	private List<String>				dataInLines;
	private List<byte[]>				finalOutput;
	private long						finalBytesCount	= 0;
	public static final byte			X_I				= 1;
	public static final byte			Y_I				= 2;
	public static final byte			ALL_I			= 3;
	
	public Data() {
		this.dataInLines = new ArrayList<String>();
		this.geoPositions = new HashMap<Long, GeoPosition>();
		this.finalOutput = new ArrayList<>();
	}
	
	public void addPositionAfterLast(GeoPosition geoPosition) {
		geoPositions.put(getLast() + 1, geoPosition);
	}
	
	public void addString(String str) {
		dataInLines.add(str);
	}
	
	public void addBytes(byte[] bytes) {
		finalBytesCount += bytes.length;
		finalOutput.add(bytes);
	}
	
	public void clearLines() {
		dataInLines.clear();
	}
	
	public void clearFinal() {
		finalOutput = new ArrayList<byte[]>();
		finalBytesCount = 0;
	}
	
	public long getLast() {
		long vMax = 0;
		Iterator<Long> iterator = geoPositions.keySet().iterator();
		while (iterator.hasNext()) {
			Long v = iterator.next();
			vMax = vMax < v ? v : vMax;
		}
		return vMax;
	}
	
	public void removeLast() {
		dataInLines.remove(dataInLines.size() - 1);
	}
	
	public void loadTestDataToCompress(int testID) throws IOException {
		String fileName = "example_Data/Data" + testID + ".txt";
		File file = FileOperations.getFile(fileName);
		loadUncompressedDataFromFile(file);
	}
	
	public void loadUncompressedDataFromFile(String filename) throws IOException {
		File file = FileOperations.getFile(filename);
		loadUncompressedDataFromFile(file);
	}
	
	public void loadUncompressedDataFromFile(File file) throws IOException {
		if (file == null || !file.exists()) { throw new FileNotFoundException("Plik "
				+ file.getName() + " nie istnieje!"); }
		MLog.info("Loading input data");
		Stream<String> lines = Files.lines(file.toPath());
		MLog.info("Converting input data");
		Stream<String[]> splittedLines = lines.map(line -> line.split(","));
		Stream<GeoPosition> mappedLines = splittedLines.map(snippets -> new GeoPosition(Float
				.parseFloat(snippets[0]), Float.parseFloat(snippets[1])));
		List<GeoPosition> list = mappedLines.collect(Collectors.toList());
		MLog.info("Starting injection of input data with " + list.size() + " positions");
		long lpm = 1;
		for (GeoPosition geoPosition : list) {
			geoPosition.setLpm(lpm);
			this.geoPositions.put(lpm, geoPosition);
			System.out.println("Injected " + lpm + " of " + list.size());
			lpm++;
		}
		lines.close();
	}
	
	public void loadCompressedDataFromFile(String filename) {
		// TODO Auto-generated method stub
		
	}
	
	public HashMap<Long, GeoPosition> getDataAsMap() {
		return geoPositions;
	}
	
	public List<GeoPosition> getDataAsList() {
		List<GeoPosition> list = new ArrayList<GeoPosition>();
		Iterator<Entry<Long, GeoPosition>> iterator = geoPositions.entrySet().iterator();
		while (iterator.hasNext()) {
			GeoPosition position = iterator.next().getValue();
			list.add(position);
		}
		return list;
	}
	
	public List<String> getDataLines() {
		return dataInLines;
	}
	
	public List<byte[]> getFinalByteOutput() {
		return finalOutput;
	}
	
	public String getFinalByteOutputAsString() {
		String str = "";
		for (byte[] bs : finalOutput) {
			str += bs;
		}
		return str;
	}
	
	public long getFinalBytesCount() {
		return finalBytesCount;
	}
}
