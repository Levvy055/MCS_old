package pl.grm.geocompression;

import java.util.*;

import pl.grm.misc.*;

public class Decompressor {
	private Data							dataIn;
	private Data							dataOut;
	private HashMap<Long, GeoPosition>		geoPositions;
	private HashMap<Double, ValuePositions>	valPositions;
	private int								geoPositionsAmount;
	
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
	
	public void decompress(Data dataIn) throws InputDataCorruptedException {
		this.dataIn = dataIn;
		dataOut.addAllBytes(dataIn.getByteList());
		decompress();
	}
	
	public void decompress() throws InputDataCorruptedException {
		if (dataIn == null) { return; }
		MLog.info("Decompressing 1/3");
		int bytesCount = (int) dataIn.getBytesListContentCount();
		List<byte[]> byteList = dataIn.getByteList();
		MLog.info("Loaded " + bytesCount + " bytes in " + byteList.size() + " elements of list.");
		byte[] data = new byte[bytesCount];
		int i = 0;
		for (byte[] bs : byteList) {
			for (byte b : bs) {
				data[i] = b;
				i++;
			}
		}
		byte[] dataUC = divideBytes(data);
		String outputS = new String(dataUC);
		MLog.info("Decompressing 2/3");
		this.valPositions.putAll(parseToValuePositions(outputS));
		MLog.info("Decompressing 3/3");
		this.geoPositions.putAll(parseToGeoPositions(valPositions));
		if (geoPositions.size() != geoPositionsAmount) { throw new InputDataCorruptedException(
				"Brakuje wszystkich pozycji.\nIndeks wskazuje na: " + geoPositionsAmount
						+ " \nWykryto " + geoPositions.size()); }
		dataOut.addAllGeoPositions(geoPositions);
		MLog.info("Decompression completed");
		dataOut.addString(geoPositionsAmount + "e");
		Iterator<Double> it = valPositions.keySet().iterator();
		while (it.hasNext()) {
			double v = it.next();
			ValuePositions vP = valPositions.get(v);
			int w = (int) Math.round(v);
			String valToStore = (w == v) ? String.valueOf(w) : String.valueOf(v);
			String str = valToStore + vP.toSimplifiedString();
			dataOut.addString(str);
		}
	}
	
	public byte[] divideBytes(byte[] data) {
		byte[] result = new byte[data.length * 2];
		int n = 0;
		for (int i = 0; i < result.length; i += 2, n++) {
			result[i] = widen(data[n], 1);
			result[i + 1] = widen(data[n], 2);
		}
		return result;
	}
	
	private byte widen(byte b, int i) {
		int r;
		if (i == 1) {
			r = b & 0xF;
		} else {
			r = (b & 0xF0) >>> 4;
		}
		if (r > -1 && r < 10) {
			r += 48;
		} else {
			switch (r) {
				case 10 :
					r = 'e';
					break;
				case 11 :
					r = 'i';
					break;
				case 12 :
					r = 'p';
					break;
				case 13 :
					r = '-';
					break;
				case 14 :
					r = '.';
					break;
				default :
					break;
			}
		}
		return (byte) r;
	}
	
	public Map<Double, ValuePositions> parseToValuePositions(String outputS)
			throws InputDataCorruptedException {
		Map<Double, ValuePositions> parsedPos = new TreeMap<Double, ValuePositions>();
		int headerEndIndex = outputS.indexOf('e');
		String header = outputS.substring(0, headerEndIndex);
		geoPositionsAmount = Integer.parseInt(header);
		MLog.info("Positions in file: " + header);
		int iE = outputS.indexOf('e', headerEndIndex + 1);
		int liEpO = headerEndIndex + 1;
		String workingString;
		while (iE != -1) {
			workingString = outputS.substring(liEpO, iE);
			List<Integer> iI = new ArrayList<Integer>();
			for (int iIT = workingString.indexOf('i'); iIT >= 0; iIT = workingString.indexOf('i',
					iIT + 1)) {
				iI.add(iIT);
			}
			List<Integer> iP = new ArrayList<Integer>();
			for (int iPT = workingString.indexOf('p'); iPT >= 0; iPT = workingString.indexOf('p',
					iPT + 1)) {
				iP.add(iPT);
			}
			String vS = workingString.substring(0, iI.get(0));
			double value = Double.parseDouble(vS);
			if (iP.size() != iI.size()) { throw new InputDataCorruptedException(""); }
			for (int i = 0; i < iI.size(); i++) {
				ValuePositions vP;
				int iIT = iI.get(i);
				int iPT = iP.get(i);
				String iS = workingString.substring(iIT + 1, iPT);
				String pS = workingString.substring(iPT + 1, iPT + 2);
				long index = Long.parseLong(iS);
				byte position = Byte.parseByte(pS);
				if (parsedPos.containsKey(value)) {
					vP = parsedPos.get(value);
				} else {
					vP = new ValuePositions();
					parsedPos.put(value, vP);
				}
				vP.put(index, position);
			}
			liEpO = iE + 1;
			iE = outputS.indexOf('e', iE + 1);
		}
		return parsedPos;
	}
	
	public Map<Long, GeoPosition> parseToGeoPositions(HashMap<Double, ValuePositions> vPos) {
		Map<Long, GeoPosition> geoPos = new HashMap<Long, GeoPosition>();
		Iterator<Double> iterator = vPos.keySet().iterator();
		while (iterator.hasNext()) {
			double value = iterator.next();
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
					default :
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
