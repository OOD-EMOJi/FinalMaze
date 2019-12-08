package EMOJI;
import LepinskiEngine.*;
import java.util.*;

public class ScoutBotBehavior implements RobotBehavior {

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
        Command command;
        List<PathOption> pathList = new ArrayList<PathOption>();
        System.out.println("         ready to call generator! ");
        pathList = pathOptionGenerator.generatePathOptions(2 * x + 1, 2 * y + 1, 2 * currentTurns);
        System.out.println("         called to call generator! ");
        // Decide best path and get the next step
		//System.out.println(pathList.size() + " " + pathList);
        if(pathList.size()>0){
            PathOption pathOption = pathList.get(pathList.size() - 1);
    		System.out.println("-- ( " + location.getX() + " , "  + location.getY() + " )");
    		for(Tile t : pathOption.path) {
    			System.out.println(t.getX() + " " + t.getY());
    		}
            if(pathOption.path.size()>2){
                Tile currentTile = pathOption.path.get(0);
                Tile nextStep = pathOption.path.get(1);
                command = new CommandMove(robot, PathOption.getDirection(currentTile.getX(), currentTile.getY(), nextStep.getX(), nextStep.getY()));
                currentTurns -= 1;
                return command;
                
            }         
			
        }
        Random random = new Random();
        int randomInteger = random.nextInt(4);
        while(randomInteger > location.getDirections().size() -1){
            randomInteger = random.nextInt(4);
        }
        DirType dirction= location.getDirections().get(randomInteger);
        command = new CommandMove(robot, dirction);
        currentTurns -= 1;
        return command;        


        //System.out.println(((CommandMove)command).getDir());
    }

}
