package pl.grm.geocompression;

public class Compress {
	private Data	dataIn;
	private Data	dataOut;
	
	public Compress(Data dataIn) {
		this.dataIn = dataIn;
	}
	
	public void compress(Data dataIn) {
		this.dataIn = dataIn;
		compress();
	}
	
	public void compress() {
		if (dataIn == null) { return; }
		
	}
	
	public Data getComprossedData() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
