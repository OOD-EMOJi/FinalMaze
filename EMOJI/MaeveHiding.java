package EMOJI;
import LepinskiEngine.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class MaeveHiding implements PlayerHidingTeam {
    
    /*
     * Maeve is procedurally generated, starting with a coin flip
     * to determine the location of the two diamond coins
     * true means the diamonds appear in the top left and bottom right corners
     * false puts them in the top right and bottom left
     */
    boolean flipped;
    Random randy = new Random();
    public MaeveHiding() {
        flipped = randy.nextBoolean();
        System.out.println("Flipped:\t" + flipped);
    }
    
    Maze maeze;
    GameState state; // not sure this is needed either
    /* Coin + Obstacle counts */
    int stones = 0, darks = 0, slows = 0, diamonds = 0, golds = 0;
    
    /*
     * startGame is called once at the very start of the game
     * It looks like this function never gets called?
     */
    public void startGame(List<ObstacleType> obs, List<CoinType> coins, RectMaze maze, GameState state) {}

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
        for (int i=0; i<obs.size(); i++) {
            ObstacleType o = obs.get(i);
            switch (o) {
                case Dark: darks++; break;
                case Stone: stones++; break;
                case Slow: slows++; break;
            }
        }
        System.out.println("OBSTACLES\ndarks:\t" + darks);
        System.out.println("stones:\t" + stones);
        System.out.println("slows:\t" + slows);
        return new ArrayList<PlaceObstacle>();
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
        
        if (flipped) {
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, 0, 0)); // top left
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, maze.getMaxX() - 1, maze.getMaxY() - 1)); // bottom right
        } else {
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, maze.getMaxX() - 1, 0)); // top right
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, 0, maze.getMaxY() - 1)); // bottom left
        }
        
        coinPlacements.addAll(hideGold(golds, maze, state));
        return coinPlacements;
    }
    
    /*
     * Spirals gold coins out from center.
     * Chances of a coin appearing in a particular spot are tied to the difficulty of reaching that spot
     * The more walls a spot has, the more likely a coin is to appear there
     * Spiral pattern adapted from https://www.geeksforgeeks.org/print-given-matrix-reverse-spiral-form/
     */
    private List<PlaceCoin> hideGold(int coins, RectMaze maze, GameState state) {
        ArrayList<PlaceCoin> goldPlacements = new ArrayList<PlaceCoin>();
        /* k - starting row index
        l - starting column index*/
        int i, k = 0, l = 0;
        
        // Total spots in maze
        int m = maze.getMaxX(), n = maze.getMaxY();
        int size = m * n;
        
        while (k < m && l < n && coins > 0) {
            for (i = l; i < n; ++i) {
                if (coinGoesHere(maze.getLocation(k, i)))
                    goldPlacements.add(new PlaceCoin(CoinType.Gold, k, i));
                coins--;
            } k++;
            for (i = k; i < m; ++i) {
                if (coinGoesHere(maze.getLocation(i, n-1)))
                    goldPlacements.add(new PlaceCoin(CoinType.Gold, i, n-1));
                coins--;
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    if (coinGoesHere(maze.getLocation(m-1, i)))
                        goldPlacements.add(new PlaceCoin(CoinType.Gold, m-1, i));
                    coins--;
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    if (coinGoesHere(maze.getLocation(i, l)))
                        goldPlacements.add(new PlaceCoin(CoinType.Gold, i, l));
                    coins--;
                } l++;
            }
        }
        return goldPlacements;
    }
    
    private boolean coinGoesHere (MazeLocation l) {
        if (!l.getObstacles.isEmpty() || !l.getCoins.isEmpty()) return false;
        if (randy.nextInt(4) < l.getDirections().size()) return true;
    }
}
