package pl.grm.geocompression;

public class GeoPosition {
	private float	x;
	private float	y;
	
	public GeoPosition(float x, float y) {
		this.x = x;
		this.y = y;
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
	
	@Override
	public String toString() {
		return "GeoPosition [x=" + this.x + ", y=" + this.y + "]";
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
