package EMOJI;

import LepinskiEngine.*;
import static LepinskiEngine.DirType.*;
import java.util.*;

public class LeftWallScout implements RobotBehavior {

	//make constructor that requires location

    public Command getCommand(Robot robot, Location location) {
        List<DirType> directions = location.getDirections();
        if (directions.contains(South))
            return new CommandMove(robot, South);
        else if (directions.contains(West))
            return new CommandMove(robot, West);
        else if (directions.contains(North))
            return new CommandMove(robot, North);
        else
            return new CommandMove(robot, East);
    }
}
