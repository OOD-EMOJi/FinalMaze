package EMOJI;
public class Dark implements Thing{
	
	int x, y;
	
	public Dark( int x, int y, int value) {
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
		return 'D';
	}
}