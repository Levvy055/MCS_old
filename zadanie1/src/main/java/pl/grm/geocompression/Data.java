package pl.grm.geocompression;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import pl.grm.misc.*;

public class Data {
	private TreeMap<Long, GeoPosition>	geoPositions;
	private List<String>				stringList;
	private List<byte[]>				byteList;
	private byte[]						compressedBytes;
	private long						finalBytesCountInList	= 0;
	public static final byte			X_I						= 1;
	public static final byte			Y_I						= 2;
	public static final byte			ALL_I					= 3;
	
	public Data() {
		this.stringList = new ArrayList<String>();
		this.geoPositions = new TreeMap<Long, GeoPosition>();
		this.byteList = new ArrayList<>();
	}
	
	public synchronized void addString(String str) {
		stringList.add(str);
	}
	
	public synchronized void addBytes(byte[] bytes) {
		finalBytesCountInList += bytes.length;
		byteList.add(bytes);
	}
	
	public void addAllBytes(List<byte[]> byteList) {
		for (byte[] bs : byteList) {
			addBytes(bs);
		}
	}
	
	public void addCompressedBytes(byte[] bytes) {
		compressedBytes = bytes;
	}
	
	public void clearStringList() {
		stringList.clear();
	}
	
	public void clearByteList() {
		byteList = new ArrayList<byte[]>();
		finalBytesCountInList = 0;
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
		Stream<GeoPosition> mappedLines;
		try {
			mappedLines = splittedLines.map(snippets -> new GeoPosition(snippets[0], snippets[1]));
		}
		catch (IllegalArgumentException e) {
			lines.close();
			throw new IOException("Incorrect input data!\n" + e.getMessage());
		}
		List<GeoPosition> list;
		try {
			list = mappedLines.collect(Collectors.toList());
		}
		catch (ArrayIndexOutOfBoundsException e) {
			lines.close();
			throw new IOException(
					"Incorrect input data!\nIs there empty line in file?\nDetails/Index: "
							+ e.getMessage());
		}
		MLog.info("Starting injection of input data with " + list.size() + " positions");
		long lpm = 1;
		for (GeoPosition geoPosition : list) {
			geoPosition.setLpm(lpm);
			this.geoPositions.put(lpm, geoPosition);
			lpm++;
		}
		lines.close();
	}
	
	public void loadCompressedDataFromFile(String filename) throws IOException {
		Path path = Paths.get(filename);
		byte[] data = Files.readAllBytes(path);
		addBytes(data);
	}
	
	public TreeMap<Long, GeoPosition> getDataAsMap() {
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
	
	public List<String> getStringList() {
		return stringList;
	}
	
	public List<byte[]> getByteList() {
		return byteList;
	}
	
	public String getByteListAsString() {
		String str = "";
		for (byte[] bs : byteList) {
			for (byte b : bs) {
				str += b + " ";
			}
		}
		return str;
	}
	
	public long getBytesListContentCount() {
		return finalBytesCountInList;
	}
	
	public void addAllGeoPositions(HashMap<Long, GeoPosition> gPMap) {
		this.geoPositions.putAll(gPMap);
	}
	
	public byte[] getCByteList() {
		return compressedBytes;
	}
}