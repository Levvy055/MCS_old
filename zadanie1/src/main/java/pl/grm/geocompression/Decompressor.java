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
		MLog.info("Decompressing 1/3");
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
		MLog.info("Decompressing 2/3");
		this.valPositions.putAll(parseToValuePositions(outputS));
		MLog.info("Decompressing 3/3");
		this.geoPositions.putAll(parseToGeoPositions(valPositions));
		dataOut.addAllGeoPositions(geoPositions);
	}
	
	public Map<Float, ValuePositions> parseToValuePositions(String outputS) {
		Map<Float, ValuePositions> parsedPos = new HashMap<Float, ValuePositions>();
		int headerEndIndex = outputS.indexOf('e');
		String header = outputS.substring(0, headerEndIndex);
		int posMaxCount = Integer.parseInt(header);
		MLog.info("Positions in file: " + header);
		int iE = outputS.indexOf('e', headerEndIndex + 1);
		int liEpO = headerEndIndex + 1;
		String workingString;
		while (iE != -1) {
			workingString = outputS.substring(liEpO, iE);
			System.out.println(workingString);
			int iI = workingString.indexOf('i');
			int iP = workingString.indexOf('p');
			int liPpT = 0;
			while (iP < iE && iP != -1) {
				String vS = workingString.substring(liPpT, iI);
				Float value = Float.parseFloat(vS);
				String iS = workingString.substring(iI + 1, iP);
				long index = Long.parseLong(iS);
				String pS = workingString.substring(iP + 1, iP + 2);
				byte position = Byte.parseByte(pS);
				ValuePositions vP;
				if (parsedPos.containsKey(value)) {
					vP = parsedPos.get(value);
				} else {
					vP = new ValuePositions();
					parsedPos.put(value, vP);
				}
				vP.put(index, position);
				liPpT = iP + 1;
				iP = workingString.indexOf('p', iP + 1);
				iI = workingString.indexOf('i', iI + 1);
			}
			liEpO = iE + 1;
			iE = outputS.indexOf('e', iE + 1);
		}
		MLog.print("O ", parsedPos, true);
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
