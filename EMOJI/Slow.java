package EMOJI;
public class Slow implements Thing{
	
	int x, y;
	
	public Slow( int x, int y, int value) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void move() {
		return;
	}
	
	public char draw() {
		return 'S';
	}
}