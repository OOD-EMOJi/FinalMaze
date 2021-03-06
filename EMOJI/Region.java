package EMOJI;

import java.util.*;


public class Region implements Comparable<Region> {
	Tile center;
	int sizex, sizey;
	List<Coin> hiddenCoins;
	
	public Region(Tile center, int sizex, int sizey) {
		this.center = center;
		this.sizex = sizex;
		this.sizey = sizey;
		this.hiddenCoins = new ArrayList<Coin>();
	}
	
	public void countCoins(Maze maze, List<Coin> allhiddenCoins) {
		for(Coin hc : allhiddenCoins) {
			if( hc.getX() < center.getX() + sizex && 
				hc.getX() > center.getX() - sizex &&
				hc.getY() < center.getY() + sizey && 
				hc.getY() > center.getY() - sizey) {
					this.hiddenCoins.add(hc);
			}
		}
	}
	
	public List<Coin> getHiddenCoins() {
		return hiddenCoins;
	}
	
	public int compareTo(Region that) {
		return this.getHiddenCoins().size() - that.getHiddenCoins().size();
	}
	
	public String toString() {
		return "Region ( " + center.getX() + "," + center.getY() + " )[ " + (center.getX() - sizex) + " - " + (center.getX() + sizex) + " , " + (center.getY() - sizey) + " - " + (center.getY() + sizey) + "]";
	}
}