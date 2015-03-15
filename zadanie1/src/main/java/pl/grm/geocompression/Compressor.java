package pl.grm.geocompression;

import java.io.*;
import java.util.*;

import pl.grm.misc.*;

public class Compressor {
	private Data							dataIn;
	private Data							dataOut;
	private TreeMap<Long, GeoPosition>		geoPositions;
	private TreeMap<Double, ValuePositions>	valPositions;
	
	public Compressor() {
		dataOut = new Data();
		valPositions = new TreeMap<>();
	}
	
	public Compressor(Data dataIn) {
		this();
		this.dataIn = dataIn;
		dataOut.getDataAsMap().putAll(dataIn.getDataAsMap());
	}
	
	public void compress(Data dataIn) {
		this.dataIn = dataIn;
		dataOut.getDataAsMap().putAll(dataIn.getDataAsMap());
		compress();
	}
	
	public void compress() {
		if (dataIn == null) { return; }
		MLog.info("Converting");
		convertToVP();
		MLog.info("Compression 1/3 stage");
		Iterator<Double> it = valPositions.keySet().iterator();
		while (it.hasNext()) {
			double v = it.next();
			ValuePositions vP = valPositions.get(v);
			int w = (int) Math.round(v);
			String valToStore = (w == v) ? String.valueOf(w) : String.valueOf(v);
			String str = valToStore + vP.toSimplifiedString();
			dataOut.addString(str);
			try {
				dataOut.addBytes(str.getBytes("UTF-8"));
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		MLog.info("Compression 2/3 stage");
		int finalBytesCount = (int) dataOut.getBytesListContentCount();
		byte[] bytes = new byte[finalBytesCount];
		List<byte[]> finalByteOutput = dataOut.getByteList();
		int i = 0;
		for (byte[] bs : finalByteOutput) {
			for (byte b : bs) {
				bytes[i] = b;
				i++;
			}
		}
		MLog.info("Current size: " + finalBytesCount);
		MLog.info("Compression 3/3 stage");
		byte[] output = compressBytes(bytes);
		dataOut.clearByteList();
		dataOut.addBytes(output);
		MLog.info("Current bytes amount: " + output.length);
		MLog.info("Compression completed");
	}
	
	public byte[] compressBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return bytes;
	}
	
	public void convertToVP() {
		this.geoPositions = dataOut.getDataAsMap();
		int positionsCount = geoPositions.size();
		try {
			this.dataOut.addString(positionsCount + "e");
			this.dataOut.addBytes((positionsCount + "e").getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<GeoPosition> listData = dataOut.getDataAsList();
		for (GeoPosition gP : listData) {
			long lpm = gP.getLpm();
			double x = gP.getX();
			double y = gP.getY();
			if (x == y) {
				addValue(x, lpm, Data.ALL_I);
			} else {
				addValue(x, lpm, Data.X_I);
				addValue(y, lpm, Data.Y_I);
			}
		}
	}
	
	private void addValue(double x, long index, byte position) {
		ValuePositions vP;
		if (valPositions.containsKey(x)) {
			vP = valPositions.get(x);
		} else {
			vP = new ValuePositions();
			valPositions.put(x, vP);
		}
		vP.put(index, position);
	}
	
	public Data getComprossedData() {
		return dataOut;
	}
}
