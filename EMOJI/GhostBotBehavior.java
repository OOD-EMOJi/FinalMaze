package EMOJI;

import java.util.*;
import LepinskiEngine.*;

public class GhostBotBehavior implements RobotBehavior {
	private Maze maze;
	private Pathfinder pathFinder;
	private List<Coin> diamonds;
	int turns;
	
	public GhostBotBehavior(Maze maze, int turns) {
		this.maze = maze;
		this.pathFinder = new BreadthFirstSearchPathFinder(maze, false);
		findDiamondCoins();
		this.turns = turns;
	}
	
	private void findDiamondCoins() {
		diamonds = new ArrayList<Coin>();
		for(int x = 0; x < maze.tiles.length; x++) {
			for(int y = 0; y < maze.tiles[0].length; y++) {
				if(maze.tiles[x][y].isWall() == false) {
					for(Thing thing : maze.tiles[x][y].getContents()) {
						if(thing instanceof Coin && ((Coin) thing).getValue() == 10) {
								this.diamonds.add((Coin)thing);
						}
					}
				}
			}
		}
	}
	
	public Command getCommand(Robot robot, Location location) {
		if(location.getCoins() != null) {
			for(int i = 0; i < location.getCoins().size(); i++) {
				if(location.getCoins().get(i) == CoinType.Diamond) {
					return new CommandCoin(robot);
				}
			}
		}
		List<PathOption> options = new ArrayList<PathOption>();
		for(Coin diamond : diamonds) {
			List<Tile> path = pathFinder.findPath(maze.tiles[diamond.getX()][diamond.getY()], maze.tiles[location.getX()][location.getY()]);
			Collections.reverse(path);
			PathOption po = new CoinPathOption(path, turns);
			options.add(po);
		}
		Collections.sort(options);
		PathOption best = options.get(0);
		return new CommandMove(robot, PathOption.getDirection(location.getX(), location.getY(), best.path.get(1).getX(), best.path.get(1).getY()));
	}
}