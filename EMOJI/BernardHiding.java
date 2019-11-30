package EMOJI;
import LepinskiEngine.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class BernardHiding implements PlayerHidingTeam {
    
    /*
     * Maeve is procedurally generated, starting with a coin flip
     * to determine the location of the two diamond coins
     * true means the diamonds appear in the top left and bottom right corners
     * false puts them in the top right and bottom left
     */
    Random randy = new Random();
    LepinskiEngine.Maze ourMaze;
    boolean[][] occupied;
    GameState state; // not sure this is needed
    
    /* Coin + Obstacle counts */
    int stones = 0, darks = 0, slows = 0, diamonds = 0, golds = 0;
    
    /*
     * Guides
     * West Vertical X, East Vertical X,
     * North Horiztonal Y, South Horizontal Y
     */
    int wvx, evx, nhy, shy;
    
    /*
     * startGame is called once at the very start of the game
     * It seems like this function never gets called?
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
        
        // TODO: delete if this is initialized in startGame()
        // (it's supposed to but idk if it actually does)
        //////////////////////////////////////////////////////
        ourMaze = new LepinskiEngine.Maze(maze);
        occupied = new boolean[maze.getMaxX()][maze.getMaxY()];
        for (int x=0; x < maze.getMaxX(); x++) {
            for (int y=0; y<maze.getMaxY(); y++) {
                occupied[x][y] = false;
            }
        }
        //////////////////////////////////////////////////////
        
        ArrayList<PlaceObstacle> obstacles = new ArrayList<PlaceObstacle>();
        // Assume 8 dark, 8 slow, 1 stone
        
        // Center + guide values
        int cx = maze.getMaxX() / 2, cy = maze.getMaxY() / 2;
        wvx = cx - 1; evx = cx + 1;
        nhy = cy - 1; shy = cy + 1;
        
        /* STONE */
        obstacles.add(new PlaceObstacle(ObstacleType.Stone, cx, cy));
        occupied[cx][cy] = true;
        
        /* COIN PLACEHOLDERS */
        occupied[wvx - 1][nhy - 1] = true;
        occupied[evx + 1][nhy - 1] = true;
        occupied[wvx - 1][shy + 1] = true;
        occupied[evx + 1][shy + 1] = true;
        
        /* SLOWS */
        // Top left diamond
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, wvx-1, nhy-2));  // n
        occupied[wvx-1][nhy-2] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, wvx, nhy-1));    // e
        occupied[wvx][nhy-1] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, wvx-1, nhy));    // s
        occupied[wvx-1][nhy] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, wvx-2, nhy-1));  // w
        occupied[wvx-2][nhy-1] = true;
        // bottom right diamond
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, evx+1, shy));    // n
        occupied[evx+1][shy] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, evx+2, shy+1));  // e
        occupied[evx+2][shy+1] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, evx+1, shy+2));  // s
        occupied[evx+1][shy+2] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, evx, shy+1));    // w
        occupied[evx][shy+1] = true;
        
        /* DARKS */
        // Top right gold
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, evx+1, nhy-2));  // n
        occupied[evx+1][nhy-2] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, evx+2, nhy-1));  // e
        occupied[evx+2][nhy-1] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, evx+1, nhy));    // s
        occupied[evx+1][nhy] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, evx, nhy-1));    // w
        occupied[evx][nhy-1] = true;
        // bottom left gold
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, wvx-1, shy));    // n
        occupied[wvx-1][shy] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, wvx, shy+1));    // e
        occupied[wvx][shy+1] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, wvx-1, shy+2));  // s
        occupied[wvx-1][shy+2] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, wvx-2, shy+1));    // w
        occupied[wvx-2][shy+1] = true;
        
        return obstacles;
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
        coinPlacements.add(new PlaceCoin(CoinType.Diamond, wvx - 1, nhy - 1));
        coinPlacements.add(new PlaceCoin(CoinType.Diamond, evx + 1, shy + 1));
        
        /*
         * Place golds
         * Opposite diamonds, then randomly (but not along guides)
         */
        coinPlacements.add(new PlaceCoin(CoinType.Gold, evx + 1, nhy - 1));
        coinPlacements.add(new PlaceCoin(CoinType.Gold, wvx - 1, shy + 1));
        
        golds -= 2;
        while (golds > 0) {
            int a = randy.nextInt(maze.getMaxX()), b = randy.nextInt(maze.getMaxY());
            if (coinGoesHere(ourMaze.getLocation(a, b))) {
                coinPlacements.add(new PlaceCoin(CoinType.Gold, a, b));
                occupied[a][b] = true;
                golds--;
            }
        }
        return coinPlacements;
    }
    
    /*
     * Criteria for a coin belonging in a particular location:
     * 1. Location doesn't already have a coin or obstacle
     * 2. Location has 2 or more walls
     * 3. Location isn't along any of the guides
     */
    private boolean coinGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() > 2 || l.getX() == wvx || l.getX() == evx || l.getY() == nhy || l.getY() == shy) return false;
        return true;
    }
}
