package EMOJI;
import LepinskiEngine.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class RandomHiding implements PlayerHidingTeam {
    
    /*
     * The hiding team previously known as Clementine
     */
    Random randy = new Random();
    
    boolean[][] occupied;
    GameState state; // not sure this is needed
    /* Coin + Obstacle counts */
    int stones = 0, darks = 0, slows = 0, diamonds = 0, golds = 0;
    
    /*
     * startGame is called once at the very start of the game
     * It looks like this function never gets called?
     */
    public void startGame(List<ObstacleType> obs, List<CoinType> coins, RectMaze maze, GameState state) {
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
        occupied = new boolean[maze.getMaxX()][maze.getMaxY()];
        for (int x=0; x < maze.getMaxX(); x++) {
            for (int y=0; y<maze.getMaxY(); y++) {
                occupied[x][y] = false;
            }
        }
        
        ArrayList<PlaceObstacle> obstacles = new ArrayList<PlaceObstacle>();
        int i = 0;
        while (i < obs.size()) {
            int x = randy.nextInt(maze.getMaxX()), y = randy.nextInt(maze.getMaxY());
            boolean square = occupied[x][y];
            if (!square) {
                obstacles.add(new PlaceObstacle(obs.get(i), x, y));
                i++;
                square = true;
            }
        }
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
        // Don't worry about it, the game will place everything we miss
        ArrayList<PlaceCoin> coinPlacements = new ArrayList<PlaceCoin>();
        for (int i = 0; i < coins.size(); i++) {
            int x = randy.nextInt(maze.getMaxX()), y = randy.nextInt(maze.getMaxY());
            coinPlacements.add(new PlaceCoin(coins.get(i), x, y));
        }
        return coinPlacements;
    }
}
