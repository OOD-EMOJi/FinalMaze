/*

This class represents the algorithm for the coin bot. It has the getCommand() method that gets called each turn in the game.
There are two different commands for the coin bot, either picking a coin or moving to a nearby tile. 

In the getCommand():
1] If the current tile of the coin bot has a coin, we create a coinCommand for the coin bot, 
2] else, the call the generatePathOptions() method that retunrs a list of pathOption objects that is already sorted to give the coin robot the best pathOption.
3] take the best pathOption
4] get the direction for the next move for the coin bot
5] create a moveCommand for the coin bot with the new dirction




*/

package EMOJI;

import LepinskiEngine.*;
import java.util.*;

public class CoinBotBehavior implements RobotBehavior {

    int currentTurns;
    PathOptionGenerator pathOptionGenerator;
    Robot robot;
	PathOption option;

    public CoinBotBehavior(int currentTurns, Robot robot, PathOptionGenerator pathOptionGenerator) {
        this.currentTurns = currentTurns;
        this.robot = robot;
        this.pathOptionGenerator = pathOptionGenerator;
    }

    public Command getCommand(Robot robot, Location location) {
        if (location.getCoins() != null ) {
			for(CoinType type : location.getCoins()) {
				if(type == CoinType.Gold)
					return new CommandCoin(robot);
			}
            //((CoinBotPathOptionGenerator)pathOptionGenerator).maze.tiles[location.getX() * 2 + 1][location.getY() * 2 + 1].clearContents();
        }
        // Make paths
        int x = location.getX();
        int y = location.getY();
        Command command;

        List<PathOption> pathList = pathOptionGenerator.generatePathOptions(2 * x + 1, 2 * y + 1, 2 * currentTurns);
        // Decide best path and get the next step
        if (pathList.size()>0){
            option = pathList.get(pathList.size() - 1);
            Tile currentTile = option.path.get(0);
            Tile nextStep = option.path.get(1);
            command = new CommandMove(robot, PathOption.getDirection(currentTile.getX(), currentTile.getY(), nextStep.getX(), nextStep.getY()));
        }else{
            DirType dirction= location.getDirections().get(0);
            command = new CommandMove(robot, dirction);
        }
        currentTurns -= 1;
        return command;
    }
}
