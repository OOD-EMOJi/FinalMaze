package LepinskiEngine;

//CommandFastMove is only used by a FastBot
//It allows you to include two directions
//During a single turn, the FastBot will move in the first_direction
// ... and then in the same turn will also move in the second_direction

// For example, 
// CommandMove(bot, DirType.East, DirType.South)
//    ... will ask the GameEngine to first move the robot East and then South

public class CommandFastMove implements Command{
    private Robot the_robot;
    private DirType first_direction;
    private DirType second_direction;

    public Robot getRobot(){
	return the_robot;
    }

    public DirType getDir1(){
	return first_direction;
    }

    public DirType getDir2(){
        return second_direction;
    }

    public CommandFastMove(Robot rob, DirType dir1, DirType dir2){
	the_robot = rob;
	first_direction = dir1;
        second_direction = dir2;
    }

}