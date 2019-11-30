package EMOJI;
import LepinskiEngine.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class AkechetaHiding implements PlayerHidingTeam {
    
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
        
        // Diamond coin placeholders on either side of the stone
        int ld = cx - 1, rd = cx + 1; // diamond x values
        occupied[ld][cy] = true;
        occupied[rd][cy] = true;
                      
        /*
         * SLOWS
         * 1. West of left diamond
         * 2. North of left diamond
         * 3. Two north of stone
         * 4. North of right diamond
         * 5. East of right diamond
         * 6. South of right diamond
         * 7. Two south of stone
         * 8. South of left diamond
         */
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, ld - 1, cy));
        occupied[ld - 1][cy] = true;        // 1
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, ld, cy - 1));
        occupied[ld][cy - 1] = true;        // 2
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, cx, cy - 2));
        occupied[cx][cy - 2] = true;        // 3
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, rd, cy - 1));
        occupied[rd][cy - 1] = true;        // 4
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, rd + 1, cy));
        occupied[rd + 1][cy] = true;        // 5
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, rd, cy + 1));
        occupied[rd][cy + 1] = true;    // 6
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, cx, cy + 2));
        occupied[cx][cy + 2] = true;        // 7
        obstacles.add(new PlaceObstacle(ObstacleType.Slow, ld, cy + 1));
        occupied[ld][cy + 1] = true;        // 8
        
        /* DARKS
         * One on each starting position,
         * then spiraled from center
         */
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, cx, 0));
        occupied[cx][0] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, maze.getMaxX() - 1, cy));
        occupied[maze.getMaxX() - 1][cy] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, cx, maze.getMaxY() - 1));
        occupied[cx][maze.getMaxY() - 1] = true;
        obstacles.add(new PlaceObstacle(ObstacleType.Dark, 0, cy));
        occupied[0][cy] = true;
        obstacles.addAll(placeDarks(0, 0, darks - 4));
        
        return obstacles;
    }
    
    /*
    * Spirals darks out from center.
    * Chances of a dark appearing in a particular spot are tied to the difficulty of reaching that spot
    * The more walls a spot has, the less likely an obstacle is to appear there
    * Spiral pattern adapted from https://www.geeksforgeeks.org/print-given-matrix-reverse-spiral-form/
    */
    private List<PlaceObstacle> placeDarks(int x, int y, int darks) {
        ArrayList<MazeLocation> possiblePlacements = new ArrayList<MazeLocation>();
        ArrayList<PlaceObstacle> darkPlacements = new ArrayList<PlaceObstacle>();
        
        /* k - starting row index
        l - starting column index*/
    
        int i, k = 0, l = 0;
        
        // Total spots in maze
        int m = ourMaze.getMaxX(), n = ourMaze.getMaxY();
        int size = m * n;
        while (k < m && l < n) {
            for (i = l; i < n; ++i) {
                possiblePlacements.add(ourMaze.getLocation(k, i));
            } k++;
            for (i = k; i < m; ++i) {
                possiblePlacements.add(ourMaze.getLocation(i, n-1));
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    possiblePlacements.add(ourMaze.getLocation(m-1, i));
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    possiblePlacements.add(ourMaze.getLocation(i, l));
                } l++;
            }
        }
        
        // old school for loop
        int p = possiblePlacements.size() - 1;
        while (darks > 0 && p > 0) {
            MazeLocation ml = possiblePlacements.get(p);
            if (darkGoesHere(ml)) {
                darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, ml.getX(), ml.getY()));
                occupied[ml.getX()][ml.getY()] = true;
                darks--;
            }
            p--;
        }
        return darkPlacements;
    }
    
    
    private boolean darkGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() <= 1) return false;
        if (l.getDirections().size() > 3) return true;
        if (l.getDirections().size() >= 2) return randy.nextBoolean();
        return false;
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
        System.out.println("COINS\ndiamond:\t" + diamonds);
        System.out.println("gold: \t" + golds);
        ///////////////////////////////////////////////////
        
        
//        coinPlacements.addAll(hideGold(golds, state));
        return coinPlacements;
    }
    
    /*
     * Spirals gold coins out from center.
     * Chances of a coin appearing in a particular spot are tied to the difficulty of reaching that spot
     * The more walls a spot has, the more likely a coin is to appear there
     * Spiral pattern adapted from https://www.geeksforgeeks.org/print-given-matrix-reverse-spiral-form/
     */
    /*private List<PlaceCoin> hideGold(int coins, GameState state) {
        ArrayList<MazeLocation> possiblePlacements = new ArrayList<MazeLocation>();
        ArrayList<PlaceCoin> goldPlacements = new ArrayList<PlaceCoin>();
        
        /* k - starting row index
        l - starting column index*/
    /*
        int i, k = 0, l = 0;
        
        // Total spots in maze
        int m = ourMaze.getMaxX(), n = ourMaze.getMaxY();
        int size = m * n;
        
        while (k < m && l < n) {
            for (i = l; i < n; ++i) {
                possiblePlacements.add(ourMaze.getLocation(k, i));
            } k++;
            for (i = k; i < m; ++i) {
                possiblePlacements.add(ourMaze.getLocation(i, n-1));
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    possiblePlacements.add(ourMaze.getLocation(m-1, i));
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    possiblePlacements.add(ourMaze.getLocation(i, l));
//                    if (coinGoesHere(ourMaze.getLocation(i, l)))
//                        goldPlacements.add(new PlaceCoin(CoinType.Gold, i, l));
//                    coins--;
                } l++;
            }
        }
        
        // old school for loop
        int p = possiblePlacements.size() - 1;
        while (coins > 0 && p > 0) {
            MazeLocation ml = possiblePlacements.get(p);
            if (coinGoesHere(ml)) {
                goldPlacements.add(new PlaceCoin(CoinType.Gold, ml.getX(), ml.getY()));
                occupied[ml.getX()][ml.getY()] = true;
                coins--;
            }
            p--;
        }
        return goldPlacements;
    }
     */
    
    private boolean coinGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() == 4) return false;
        if (l.getDirections().size() <= 1) return true;
        if (randy.nextInt(8) < l.getDirections().size()) return true;
        return false;
    }
}
