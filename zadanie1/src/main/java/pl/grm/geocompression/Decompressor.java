package pl.grm.geocompression;

import java.util.*;

import pl.grm.misc.*;

public class Decompressor {
	private Data							dataIn;
	private Data							dataOut;
	private HashMap<Long, GeoPosition>		geoPositions;
	private HashMap<Float, ValuePositions>	valPositions;
	
	public Decompressor() {
		dataOut = new Data();
		valPositions = new HashMap<>();
		geoPositions = new HashMap<>();
	}
	
	public Decompressor(Data dataIn) {
		this();
		this.dataIn = dataIn;
		dataOut.addAllBytes(dataIn.getByteList());
	}
	
	public void decompress(Data dataIn) {
		this.dataIn = dataIn;
		dataOut.addAllBytes(dataIn.getByteList());
		decompress();
	}
	
	public void decompress() {
		if (dataIn == null) { return; }
		int bytesCount = (int) dataIn.getBytesListContentCount();
		List<byte[]> byteList = dataIn.getByteList();
		byte[] data = new byte[bytesCount];
		int i = 0;
		for (byte[] bs : byteList) {
			for (byte b : bs) {
				data[i] = b;
				i++;
			}
		}
		String outputS = new String(data);
		this.valPositions.putAll(parseToValuePositions(outputS));
		this.geoPositions.putAll(parseToGeoPositions(valPositions));
		dataOut.addAllGeoPositions(geoPositions);
	}
	
	public Map<Float, ValuePositions> parseToValuePositions(String outputS) {
		Map<Float, ValuePositions> parsedPos = new HashMap<Float, ValuePositions>();
		int headerEndIndex = outputS.indexOf('e');
		String header = outputS.substring(0, headerEndIndex);
		int posMaxCount = Integer.parseInt(header);
		MLog.info("Positions in file: " + header);
		String workingString = outputS.substring(headerEndIndex + 1, outputS.length());
		System.out.println(workingString);
		int currentPosCount = 0;
		while (currentPosCount < posMaxCount * 2) {
			int iI = workingString.indexOf('i');
			int iP = workingString.indexOf('p');
			int iE = workingString.indexOf('e');
			if (iE == 0)
				iE = workingString.length();
			String vS = workingString.substring(0, iI);
			Float value = Float.parseFloat(vS);
			String iS = workingString.substring(iI + 1, iP);
			long index = Long.parseLong(iS);
			String pS = workingString.substring(iP + 1, iE);
			byte position = Byte.parseByte(pS);
			ValuePositions vP;
			if (parsedPos.containsKey(value)) {
				vP = parsedPos.get(value);
			} else {
				vP = new ValuePositions();
				parsedPos.put(value, vP);
			}
			vP.put(index, position);
			workingString = workingString.substring(iE + 1, workingString.length());
			currentPosCount++;
		}
		MLog.print("O ", parsedPos, false);
		return parsedPos;
	}
	
	public Map<Long, GeoPosition> parseToGeoPositions(HashMap<Float, ValuePositions> vPos) {
		Map<Long, GeoPosition> geoPos = new HashMap<Long, GeoPosition>();
		Iterator<Float> iterator = vPos.keySet().iterator();
		while (iterator.hasNext()) {
			Float value = iterator.next();
			ValuePositions vPositions = vPos.get(value);
			Iterator<Long> it = vPositions.keySet().iterator();
			while (it.hasNext()) {
				Long index = it.next();
				Byte pos = vPositions.get(index);
				GeoPosition gP;
				if (geoPos.containsKey(index)) {
					gP = geoPos.get(index);
				} else {
					gP = new GeoPosition(index);
				}
				switch (pos) {
					case Data.ALL_I :
						gP.setX(value);
						gP.setY(value);
						break;
					case Data.X_I :
						gP.setX(value);
						break;
					case Data.Y_I :
						gP.setY(value);
						break;
				}
				geoPos.put(index, gP);
			}
		}
		return geoPos;
	}
	
	public Data getDecomprossedData() {
		return dataOut;
	}
}
