package pl.grm.geocompression;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Data {
	private List<GeoPosition>	geoPositionsList;
	
	public void loadDataFromFile(String filename) throws IOException {
		File file = FileOperations.getFile(filename);
		loadDataFromFile(file);
	}
	
	public void loadTestData(int testID) throws IOException {
		String fileName = "example_Data/Data" + testID + ".txt";
		File file = FileOperations.getFile(fileName);
		loadDataFromFile(file);
	}
	
	public void loadDataFromFile(File file) throws IOException {
		if (file == null || !file.exists()) { throw new FileNotFoundException("Plik "
				+ file.getName() + " nie istnieje!"); }
		Stream<String> lines = Files.lines(file.toPath());
		Stream<String[]> splittedLines = lines.map(line -> line.split(","));
		Stream<GeoPosition> mappedLines = splittedLines.map(snippets -> new GeoPosition(Float
				.parseFloat(snippets[0]), Float.parseFloat(snippets[1])));
		this.geoPositionsList = mappedLines.collect(Collectors.toList());
		lines.close();
	}
	
	public List<GeoPosition> getData() {
		return geoPositionsList;
	}
}
