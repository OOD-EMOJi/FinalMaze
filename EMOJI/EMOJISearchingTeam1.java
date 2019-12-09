package EMOJI;

import LepinskiEngine.*;
import java.util.*;


public class EMOJISearchingTeam1 implements PlayerSearchingTeam {
    MazeAdapter mazeA; // Lepinski's maze to our maze. (MazeAdapter extends our Maze class)
    Map<Integer, RobotBehavior> behaviors; // keep track of each robot and their behavior

	public List<Robot> chooseRobots(GameState state){

        //1. Initialize maze
        mazeA = new MazeAdapter(state);
        mazeA.generateMaze();
        //2. Initialize Bot Map and the robot list
        List<Robot> robotList = new ArrayList<Robot>();
        int ID = 0;
        behaviors = new HashMap<Integer, RobotBehavior>();
        Robot robot1 = new Robot( ModelType.VisionBot, ID++); 
//        Robot robot2 = new Robot( ModelType.ScoutBot, ID++);
        Robot robot2 = new Robot( ModelType.CoinBot, ID++);
        Robot robot3 = new Robot( ModelType.GhostBot, ID++); 
        Robot robot4 = new Robot( ModelType.FastBot, ID++); 
        Robot robot5 = new Robot( ModelType.FastBot, ID++); 
        //TODO : Pick a 5th Robot!
        robotList.add(robot1);
        robotList.add(robot2);
        robotList.add(robot3);
        robotList.add(robot4);
        robotList.add(robot5);
        //3. Fill bot map with robot ID's and associated bot behaviors according to robot.getType()
        boolean hasLeft = false;
        for (Robot bot : robotList) {
            Integer id = bot.getID();
            ModelType type = bot.getModel();
            RobotBehavior behavior = null;
            if (type == ModelType.CoinBot) behavior = new CoinBotBehavior(state.turns_remaining, bot, new CoinBotPathOptionGenerator(mazeA)); 
            else if (type == ModelType.ScoutBot) behavior = new ScoutBotBehavior(state.turns_remaining, bot, new ScoutBotPathOptionGenerator(mazeA));
            else if (type == ModelType.CoinBot) behavior = new CoinScout(state.turns_remaining, bot, new ScoutBotPathOptionGenerator(mazeA));
            else if (type == ModelType.GhostBot) behavior = new GhostBotBehavior(mazeA, state.turns_remaining);
            else if (type == ModelType.FastBot) behavior = new FastBotBehavior(state.turns_remaining, bot, new CoinBotPathOptionGenerator.SouthCoinBotPathOptionGenerator(mazeA));
            behaviors.put(id, behavior);
         } 
		 behaviors.put(4, new FastBotBehavior(state.turns_remaining, robotList.get(4), new CoinBotPathOptionGenerator.NorthCoinBotPathOptionGenerator(mazeA)));
         return robotList;            
    }

	

	public List<Command> requestCommands(List<Location> information, List<Robot> robotsAwaitingCommand, GameState state){
        //0. make new list of commands to return
        List<Command> commands = new ArrayList<Command>();
        //1. Update the Maze
        mazeA.updateMaze(information);
        
        //2. For every robot awaiting command:
        for (Robot bot : robotsAwaitingCommand) {
            RobotBehavior behavior = behaviors.get(bot.getID());
            Location location = null;
            for (Location loc : information) {
                if (loc.getRobots() != null) {
                    for (Robot robot : loc.getRobots()) {
                        if (robot.getID() == bot.getID()) {
                            location = loc;
                        }
                    }
                }
            }
            commands.add(behavior.getCommand(bot,location));
        }
        //a. retrieve behavior from map
        //b. call get command and add to list
        //3. return list
        MazePrinter p = new MazePrinter();
        p.printMaze(mazeA);
        return commands;
    }


}
