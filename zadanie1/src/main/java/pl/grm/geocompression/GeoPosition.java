package pl.grm.geocompression;

public class GeoPosition {
	private double	x;
	private double	y;
	private long	lpm;
	
	public GeoPosition(long lpm) {
		this(0, 0);
		this.lpm = lpm;
	}
	
	public GeoPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public GeoPosition(long lpm, double x, double y) {
		this(x, y);
		this.lpm = lpm;
	}
	
	public GeoPosition(String sX, String sY) {
		this.x = Double.parseDouble(sX);
		this.y = Double.parseDouble(sY);
	}
	
	public double getX() {
		return this.x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public long getLpm() {
		return lpm;
	}
	
	public void setLpm(long lpm) {
		this.lpm = lpm;
	}
	
	@Override
	public String toString() {
		return "GeoPosition " + lpm + " [x=" + this.x + ", y=" + this.y + "]";
	}
	
	public String toSimplifiedString() {
		return "X=" + this.x + ", Y=" + this.y;
	}
	
	public String toMinimalString() {
		return String.format("%.6f", this.x) + "," + String.format("%.6f", this.y);
	}
	
	public String toMinimalLikeIntString() {
		String mS = toMinimalString();
		mS = mS.replaceAll("\\.0$", "");
		mS = mS.replaceAll("\\.0,", ",");
		return mS;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoPosition other = (GeoPosition) obj;
		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
}
