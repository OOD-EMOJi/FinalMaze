package EMOJI;

import java.util.*;

public class RegionAnalyzer {
	Maze maze;
	List<Region> regions;
	
	public RegionAnalyzer(Maze maze) {
		this.maze = maze;
		this.regions = new ArrayList<Region>();
	}
	
	public void calculateRegions(int scoutLocationX, int scoutLocationY, int minx, int miny, int maxx, int maxy) {
		if(maxx - minx < 14 || maxy - miny < 14 ) return;
		
		//calculate center tile
		int midx = (maxx - minx) / 2 + minx;
		int midy = (maxy - miny) / 2 + miny;
		Region region = new Region(maze.tiles[midx][midy], 7, 7);
		regions.add(region);
		
		//recursion
		if(scoutLocationX < maxx && scoutLocationX > minx && scoutLocationY > miny && scoutLocationY < maxy) {
			calculateRegions(scoutLocationX, scoutLocationY, minx, miny, midx, midy);
			calculateRegions(scoutLocationX, scoutLocationY, midx, miny, maxx, midy);
			calculateRegions(scoutLocationX, scoutLocationY, minx, midy, midx, maxy);
			calculateRegions(scoutLocationX, scoutLocationY, midx, midy, maxx, maxy);
		}
	}
	
	public List<Region> getRegions() {
		return regions;
	}
	
	public List<Coin> findAllHiddenCoins() {
		return null;
	}
}