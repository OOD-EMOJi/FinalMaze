package EMOJI;
import LepinskiEngine.*;
import java.util.*;

public  class ScoutBotBehavior implements RobotBehavior {

    int currentTurns;
    PathOptionGenerator pathOptionGenerator;
    Robot robot;

    public ScoutBotBehavior(int currentTurns, Robot robot, PathOptionGenerator pathOptionGenerator) {
        this.currentTurns = currentTurns;
        this.robot = robot;
        this.pathOptionGenerator = pathOptionGenerator;
    }


    public Command getCommand(Robot robot, Location location) {
        // Make paths
        int x = location.getX();
        int y = location.getY();
        List<PathOption> pathList = new ArrayList<PathOption>();
        pathList = pathOptionGenerator.generatePathOptions(2 * x + 1, 2 * y + 1, 2 * currentTurns);
        // Decide best path and get the next step
        PathOption pathOption = pathList.get(pathList.size() - 1);
		System.out.println("-- ( " + location.getX() + " , "  + location.getY() + " )");
		for(Tile t : pathOption.path) {
			System.out.println(t.getX() + " " + t.getY());
		}
        Tile currentTile = pathOption.path.get(0);
        Tile nextStep = pathOption.path.get(1);
        Command command = new CommandMove(robot, PathOption.getDirection(currentTile.getX(), currentTile.getY(), nextStep.getX(), nextStep.getY()));
        System.out.println(((CommandMove)command).getDir());
        currentTurns -= 1;
        return command;
    }

}
