package pl.grm.geocompression;

import java.util.*;

public class Decompressor {
	private Data							dataIn;
	private Data							dataOut;
	private HashMap<Long, GeoPosition>		geoPositions;
	private HashMap<Float, ValuePositions>	valPositions;
	
	public Decompressor() {
		dataOut = new Data();
		valPositions = new HashMap<>();
	}
	
	public Decompressor(Data dataIn) {
		this();
		this.dataIn = dataIn;
	}
	
	public void decompress(Data dataIn) {
		this.dataIn = dataIn;
		decompress();
	}
	
	public void decompress() {
		if (dataIn == null) { return; }
		dataOut.addAllBytes(dataIn.getByteList());
	}
	
	public Data getDecomprossedData() {
		return dataOut;
	}
}
