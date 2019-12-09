package EMOJI;
import LepinskiEngine.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class DoloresHiding implements PlayerHidingTeam {
    
    /*
     * Dolores uses a pathfinding approach to placing obstacles.
     * Coins are placed randomly and surrounded by obstacles based on the path
     */
    Random randy = new Random();
    LepinskiEngine.Maze ourMaze;
    boolean[][] occupied;
    GameState state; // not sure this is needed
    
    /* Coin + Obstacle counts */
    int stones = 0, darks = 0, slows = 0, diamonds = 0, golds = 0;
    
    /* Diamond locations */
    int dx1, dy1, dx2, dy2;
    
    /*
     * startGame is called once at the very start of the game
     * It looks like this function never gets called?
     */
    public void startGame(List<ObstacleType> obs, List<CoinType> coins, RectMaze maze, GameState state) {
        ourMaze = new LepinskiEngine.Maze(maze);
        occupied = new boolean[maze.getMaxX()][maze.getMaxY()];
        for (int x=0; x < maze.getMaxX(); x++) {
            for (int y=0; y<maze.getMaxY(); y++) {
                occupied[x][y] = false;
            }
        }
    }

    /*
     * setObstacles is called once after startGame
     * The player team is given a list of Obstacles to place
     * You should return a PlaceObstacle object for each obstacle you wish to place
     * Obstacles that you choose not to place will be lost forever
     *
     * NOTE: You will be given exactly one STONE obstacle
     *       as well as an assortment of DARK and SLOW obstacles (8 or 10)
     *
     * Obstacles are NOT objects
     */
    public List<PlaceObstacle> setObstacles(List<ObstacleType> obs, RectMaze maze, GameState state) {
        // this is supposed to get initialized in startgame but im not sure if it actually does
        ourMaze = new LepinskiEngine.Maze(maze);
        occupied = new boolean[maze.getMaxX()][maze.getMaxY()];
        for (int x=0; x < maze.getMaxX(); x++) {
            for (int y=0; y<maze.getMaxY(); y++) {
                occupied[x][y] = false;
            }
        }
        
        ArrayList<PlaceObstacle> obstacles = new ArrayList<PlaceObstacle>();
        for (int i=0; i<obs.size(); i++) {
            ObstacleType o = obs.get(i);
            switch (o) {
                case Dark: darks++; break;
                case Stone: stones++; break;
                case Slow: slows++; break;
            }
        }
        
        // Place stone
        int cx = maze.getMaxX() / 2, cy = maze.getMaxY() / 2;   // center values
        obstacles.add(new PlaceObstacle(ObstacleType.Stone, cx, cy));
        occupied[cx][cy] = true;
        
        // Diamond coin placeholders
        dx1 = randy.nextInt(maze.getMaxX()); dy1 = randy.nextInt(maze.getMaxY());
        while (dx1 >= maze.getMaxX() - 1) dx1--;
        if (dx1 == 0) dx1 = 2;
        while (dy1 >= maze.getMaxY() - 1) dy1--;
        if (dy1 == 0) dy1 = 2;
        occupied[dx1][dy1] = true;
        dx2 = randy.nextInt(maze.getMaxX()); dy2 = randy.nextInt(maze.getMaxY());
        while (dx2 >= maze.getMaxX() - 1) dx2--;
        if (dx2 == 0) dx2 = 2;
        while (dy2 >= maze.getMaxY() - 1) dy2--;
        if (dy2 == 0) dy2 = 2;
        occupied[dx2][dy2] = true;
        
        
        /*
         * SLOWS
         * 1. West of diamond 1
         * 2. North of diamond 1
         * 3. East of diamond 1
         * 4. South of diamond 1
         *
         * 5. North of diamond 2
         * 6. East of diamond 2
         * 7. South of diamond 2
         * 8. West of diamond 2
         */
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx1 - 1, dy1));
        occupied[dx1 - 1][dy1] = true;        // 1
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx1, dy1 - 1));
        occupied[dx1][dy1 - 1] = true;        // 2
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx1 + 1, dy1));
        occupied[dx1 + 1][dy1] = true;        // 3
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx1, dy1 + 1));
        occupied[dx1][dy1 + 1] = true;        // 4
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx2, dy2 - 1));
        occupied[dx2][dy2 - 1] = true;        // 5
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx2 + 1, dy2));
        occupied[dx2 + 1][dy2] = true;    // 6
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx2, dy2 + 1));
        occupied[dx2][dy2 + 1] = true;        // 7
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, dx2 - 1, dy2));
        occupied[dx2 - 1][dy2] = true;        // 8
        
        /* DARKS
         * Place on paths leading to diamonds
         */
        int oldDarks = darks;
        darks = oldDarks / 2;
        obstacles.addAll(placeDarks(dx1, dy1, new ArrayList<PlaceObstacle>()));
        darks = oldDarks / 2;
        obstacles.addAll(placeDarks(dx2, dy2, new ArrayList<PlaceObstacle>()));
        
        
        return obstacles;
    }
    
    /*
     * Places darks in paths leading to diamonds.
     * NOTE: Also adds stone(s)
     * (didn't change the name because I'm lazy)
     */
    private List<PlaceObstacle> placeDarks(int x, int y, List<PlaceObstacle> darkPlacements) {
        if (darks > 0) {
            if (!occupied[x][y]) {
                darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, x, y));
                occupied[x][y] = true;
                darks--;
            } for (int i = 0; i < 2; i++) {
                DirType d = ourMaze.getLocation(x,y).getDirections().get(randy.nextInt(ourMaze.getLocation(x,y).getDirections().size()));
                if (d == DirType.North)
                    placeDarks(x, y-1, darkPlacements);
                else if (d == DirType.East)
                    placeDarks(x+1, y, darkPlacements);
                else if (d == DirType.South)
                    placeDarks(x, y+1, darkPlacements);
                else if (d == DirType.West)
                    placeDarks(x-1, y, darkPlacements);
            }
        }
        return darkPlacements;
    }

    /*
     * hideCoins is called once after setObstacles
     * The player team is given a list of Coins to place
     * You should return a PlaceCoin object for each Coin you wish to place
     * Coins that you choose not to place will be place arbitrarily by the gameEngine
     *
     * NOTE: You will be given exactly two DIAMOND coins
     *       As well as a number of GOLD coins (20?)
     */
    public List<PlaceCoin> hideCoins(List<CoinType> coins, RectMaze maze, GameState state) {
        ArrayList<PlaceCoin> coinPlacements = new ArrayList<PlaceCoin>();
        // TODO: delete once we get a hard number of coins
        ///////////////////////////////////////////////////
        for (int j=0; j<coins.size(); j++) {
            CoinType c = coins.get(j);
            switch (c) {
                case Diamond: diamonds++; break;
                case Gold: golds++; break;
            }
        }
        ///////////////////////////////////////////////////
        
        /*
         * Place diamonds
         * (Spots already marked occupied by hideObstacles)
         */
        coinPlacements.add(new PlaceCoin(CoinType.Diamond, dx1, dy1));
        coinPlacements.add(new PlaceCoin(CoinType.Diamond, dx2, dy2));
        
        /*
         * Place golds
         * (Same as Maeve's algorithm)
         */
        coinPlacements.addAll(hideGold(golds));
        return coinPlacements;
    }
    
    /*
     * Places gold coins in an "X" pattern from center
     * Chances of a coin appearing in a particular spot are tied to the difficulty of reaching that spot
     * The more walls a spot has, the more likely a coin is to appear there
     */
    private List<PlaceCoin> hideGold(int coins) {
        ArrayList<PlaceCoin> goldPlacements = new ArrayList<PlaceCoin>();
        int y1 = 0, y2 = ourMaze.getMaxY() - 1;
        for (int x = 0; x < ourMaze.getMaxX(); x++) {
            if (coinGoesHere(ourMaze.getLocation(x, y1)) && coins > 0) {
                goldPlacements.add(new PlaceCoin(CoinType.Gold, x, y1));
                occupied[x][y1] = true;
                coins--;
            } y1++; if (y1 >= ourMaze.getMaxY()) y1 = ourMaze.getMaxY() - 1;
            if (coinGoesHere(ourMaze.getLocation(x, y2)) && coins > 0) {
                goldPlacements.add(new PlaceCoin(CoinType.Gold, x, y2));
                occupied[x][y2] = true;
                coins--;
            } y2--; if (y2 < 0) y2 = 0;
        }
        return goldPlacements;
    }
    
    private boolean coinGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() > 3) return false;
        return true;
    }
}
