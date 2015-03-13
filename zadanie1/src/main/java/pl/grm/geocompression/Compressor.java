package pl.grm.geocompression;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import pl.grm.misc.*;

public class Compressor {
	private Data							dataIn;
	private Data							dataOut;
	private HashMap<Long, GeoPosition>		geoPositions;
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
		dataOut.getDataAsMap().putAll(dataIn.getDataAsMap());
		MLog.info("Compression started");
		this.geoPositions = dataIn.getDataAsMap();
		int positionsCount = geoPositions.size();
		this.dataOut.addString(positionsCount + "e");
		List<GeoPosition> listData = dataIn.getDataAsList();
		MLog.info("Compressing");
		MLog.info("Compression 1/3 stage");
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
		}
		Iterator<Float> it = valPositions.keySet().iterator();
		while (it.hasNext()) {
			Float v = it.next();
			ValuePositions vP = valPositions.get(v);
			String str = v + vP.toSimplifiedString();
			dataOut.addString(str);
			dataOut.addBytes(str.getBytes());
		}
		MLog.info("Compression 2/3 stage");
		int finalBytesCount = (int) dataOut.getFinalBytesCount();
		byte[] bytes = new byte[finalBytesCount];
		List<byte[]> finalByteOutput = dataOut.getFinalByteOutput();
		int i = 0;
		for (byte[] bs : finalByteOutput) {
			for (byte b : bs) {
				bytes[i] = b;
				i++;
			}
		}
		MLog.info("Current size: " + finalBytesCount);
		MLog.info("Compression 3/3 stage");
		try {
			Deflater defl = new Deflater();
			defl.setInput(bytes);
			defl.finish();
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
			dataOut.clearFinal();
			while (!defl.finished()) {
				int count = defl.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
			byte[] output = outputStream.toByteArray();
			defl.end();
			dataOut.addBytes(output);
			MLog.info("Current size: " + output.length);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
