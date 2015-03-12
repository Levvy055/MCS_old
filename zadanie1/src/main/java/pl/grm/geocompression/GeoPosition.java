package pl.grm.geocompression;

public class GeoPosition {
	private float	x;
	private float	y;
	private long	lpm;
	
	public GeoPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public GeoPosition(long lpm, float x, float y) {
		this(x, y);
		this.lpm = lpm;
	}
	
	public boolean contains(float value) {
		if (this.x == value || this.y == value) { return true; }
		return false;
	}
	
	public byte getPosByValue(float v) {
		byte pos = 0;
		if (this.x == v && this.y == v) {
			pos = Data.ALL_I;
		}
		if (this.x == v) {
			pos = Data.X_I;
		} else if (this.y == v) {
			pos = Data.Y_I;
		}
		return pos;
	}
	
	public float getX() {
		return this.x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setY(float y) {
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
		return this.x + "," + this.y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.x);
		result = prime * result + Float.floatToIntBits(this.y);
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
		if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
}
