package EMOJI;

import java.util.*;

public class RegionAnalyzer {
	Maze maze;
	List<Region> regions;
	List<Coin> allHiddenCoins;
	BreadthFirstSearchPathFinder pathFinder;
	
	public RegionAnalyzer(Maze maze) {
		this.maze = maze;
		this.regions = new ArrayList<Region>();
		this.allHiddenCoins = new ArrayList<Coin>();
		this.pathFinder = new BreadthFirstSearchPathFinder(maze, true);
	}
	
	public void calculateRegions(int minx, int miny, int maxx, int maxy) {
		
		//calculate center tile
		int midx = (maxx - minx) / 2 + minx;
		int midy = (maxy - miny) / 2 + miny;
		Region region = new Region(maze.tiles[midx][midy], 6, 6);

		region.countCoins(maze, allHiddenCoins);
		regions.add(region);
		if(maxx - minx < 14 || maxy - miny < 14) return;
		
		calculateRegions(minx, miny, midx, midy);
		calculateRegions(midx, miny, maxx, midy);
		calculateRegions(minx, midy, midx, maxy);
		calculateRegions(midx, midy, maxx, maxy);
	}
	
	public void clearRegions() {
		regions.clear();
	}
	
	public List<Region> getRegions() {
		return regions;
	}
	
	public List<Coin> getAllHiddenCoins() {
		return allHiddenCoins;
	}
	
	public void findAllHiddenCoins(int scoutLocationX, int scoutLocationY) {
		allHiddenCoins.clear();
		for(int x = 0; x < maze.tiles.length; x++) {
			for(int y = 0; y < maze.tiles[0].length; y++) {
				if(maze.tiles[x][y].isWall() == false) {
					for(Thing thing : maze.tiles[x][y].getContents()) {
						if(thing instanceof Coin && ((Coin) thing).getValue() == 1) {
								this.allHiddenCoins.add((Coin)thing);
						}
					}
				}
			}
		}
		LinkedList<Coin> removedCoins = new LinkedList<Coin>();
		for(Coin coin : allHiddenCoins) {
			if(pathFinder.findPath(maze.tiles[scoutLocationX][scoutLocationY], maze.tiles[coin.getX()][coin.getY()]).size() > 0) { // found a path to this coin
				System.out.println("Coin: " + coin.getX() + " " + coin.getY());
				removedCoins.add(coin); // add to list of found coins
			}
		}
		allHiddenCoins.removeAll(removedCoins);
	}
}