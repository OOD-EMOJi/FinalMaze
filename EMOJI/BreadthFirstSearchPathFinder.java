package EMOJI;

import java.util.*;

public class BreadthFirstSearchPathFinder implements Pathfinder {
	
	Maze maze;
	int width, height;
	boolean considerWalls;
	
	public BreadthFirstSearchPathFinder(Maze maze, boolean considerWalls) {
		this.maze = maze;
		this.width = maze.tiles.length;
		this.height = maze.tiles[0].length;
		this.considerWalls = considerWalls;
	}
	
	public List<Tile> findPath(Tile startTile, Tile endTile) {
		boolean[][] visited = new boolean[width][height];
		Map<Tile, Tile> prev = new HashMap<Tile, Tile>();
		Queue<Tile> queue = new LinkedList<Tile>();
		
		//start with startTile
		visited[startTile.getX()][startTile.getY()] = true;
		prev.put(startTile, null);
		queue.add(startTile);
		
		//bfs
		while(!queue.isEmpty()) {
			Tile tile = queue.remove();
			List<Tile> neighbors = maze.getNeighbors(tile);
			for(Tile neighbor : neighbors) {
				if(visited[neighbor.getX()][neighbor.getY()] == false && (considerWalls ? neighbor.isWall() == false : true)) {
					visited[neighbor.getX()][neighbor.getY()] = true;
					prev.put(neighbor, tile); //remember previous tile
					queue.add(neighbor);
				}
			}
			
		}
		
		List<Tile> path = new ArrayList<Tile>();
		for(Tile current = endTile; current != null; current = prev.get(current)){ 
			path.add(current);
		}
		Collections.reverse(path);
		if(path.get(0) != startTile) path.clear();
		
		return path;
	}
}