package EMOJI;

import LepinskiEngine.*;

public class FastBotBehavior extends CoinBotBehavior {

	public FastBotBehavior(int currentTurns, Robot robot, PathOptionGenerator pathOptionGenerator) {
		super(currentTurns, robot, pathOptionGenerator);
	}
	
	public Command getCommand(Robot robot, Location location) {
		Command first = super.getCommand(robot, location);
		Command second = null;
		if(first instanceof CommandCoin) {
			Tile tileWeAreOn = option.path.get(0);
			Tile nextTileAfter = option.path.get(1);
			DirType dir = PathOption.getDirection(tileWeAreOn.getX(), tileWeAreOn.getY(), nextTileAfter.getX(), nextTileAfter.getY());
			second = new CommandMove(robot, dir);
			return new CommandFastMove(robot, first, second);
		}
		
		Tile tileWeAreOn = option.path.get(2);
		for(Thing thing : tileWeAreOn.getContents()) {
			if((thing instanceof Coin) && ((Coin) thing).getValue() == 1) {
				second = new CommandCoin(robot);
				return new CommandFastMove(robot, first, second);
			}
		}
		Tile nextTileAfter = option.path.get(3);
		DirType dir = PathOption.getDirection(tileWeAreOn.getX(), tileWeAreOn.getY(), nextTileAfter.getX(), nextTileAfter.getY());
		second = new CommandMove(robot, dir);
		
		return new CommandFastMove(robot, first, second);
	}
}