package pl.grm.geocompression;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import pl.grm.misc.*;

public class Compressor {
	private Data							dataIn;
	private Data							dataOut;
	private TreeMap<Long, GeoPosition>		geoPositions;
	private TreeMap<Double, ValuePositions>	valPositions;
	private int								taskCount;
	
	public Compressor() {
		dataOut = new Data();
		valPositions = new TreeMap<>();
	}
	
	public Compressor(Data dataIn) {
		this();
		this.dataIn = dataIn;
		dataOut.getDataAsMap().putAll(dataIn.getDataAsMap());
	}
	
	public void compress() {
		if (dataIn == null) { return; }
		MLog.info("Converting");
		convertToVP();
		MLog.info("Compression 1/3 stage");
		GPSToSimpleString();
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
		dataOut.clearByteList();
		dataOut.addBytes(bytes);
		byte[] output = compressBytes(bytes);
		dataOut.addCompressedBytes(output);
		MLog.info("Current bytes amount: " + output.length);
		MLog.info("Compression completed");
	}
	
	private void convertToVP() {
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
	
	private void GPSToSimpleString() {
		ExecutorService taskExecutor = Executors.newFixedThreadPool(5);
		Iterator<Double> it = valPositions.keySet().iterator();
		taskCount = 0;
		while (it.hasNext()) {
			Double v = it.next();
			taskCount++;
			int cT = taskCount;
			taskExecutor.execute(() -> {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				addSimpleString(v, cT);
			});
		}
		taskExecutor.shutdown();
		MLog.info("Waiting for  completion of " + taskCount + " tasks...");
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void addSimpleString(double v, int tC) {
		ValuePositions vP = valPositions.get(v);
		int w = (int) Math.round(v);
		String valToStore = (w == v) ? String.valueOf(w) : String.valueOf(v);
		String str = valToStore + vP.toSimplifiedString();
		try {
			this.dataOut.addString(str);
			this.dataOut.addBytes(str.getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MLog.info("End of task " + tC + "/" + taskCount);
	}
	
	private static byte[] compressBytes(byte[] bytesIn) {
		double bInLen = bytesIn.length;
		byte[] bytesOut = new byte[(int) Math.ceil(bInLen / 2)];
		int j = 0;
		int i = 0;
		while (i < bInLen - 2) {
			bytesOut[j] = connect(shorten(bytesIn[i]), shorten(bytesIn[i + 1]));
			j++;
			i += 2;
		}
		if (bInLen % 2 == 1)
			bytesOut[j] = connect(shorten(bytesIn[bytesIn.length - 1]), 0);
		return bytesOut;
	}
	
	private static byte connect(int s1, int s2) {
		return (byte) (s1 | (s2 << 4));
	}
	
	private static int shorten(byte b) {
		byte result = 0;
		if (b > 47 && b < 58) {
			result = (byte) (b - 48);
		} else {
			switch (b) {
				case 'e' :
					result = 10;
					break;
				case 'i' :
					result = 11;
					break;
				case 'p' :
					result = 12;
					break;
				case '-' :
					result = 13;
					break;
				case '.' :
					result = 14;
				default :
					break;
			}
		}
		return result;
	}
	
	public Data getComprossedData() {
		return dataOut;
	}
}
