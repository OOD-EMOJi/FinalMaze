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
    
    LepinskiEngine.Maze maeze;
    boolean[][] occupied;
    GameState state; // not sure this is needed
    /* Coin + Obstacle counts */
    int stones = 0, darks = 0, slows = 0, diamonds = 0, golds = 0;
    
    /*
     * startGame is called once at the very start of the game
     * It looks like this function never gets called?
     */
    public void startGame(List<ObstacleType> obs, List<CoinType> coins, RectMaze maze, GameState state) {
        maeze = new LepinskiEngine.Maze(maze);
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
        maeze = new LepinskiEngine.Maze(maze);
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
        int slowsPerWall = slows / 4;
        int darksPerCorner = (darks - 2) / 2;
        if (flipped) {
            // top left
            for (int nw1 = 1; nw1 <= slowsPerWall; nw1++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, 0, nw1));
                occupied[0][nw1] = true;
            } for (int nw2 = 1; nw2 <= slowsPerWall; nw2++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, nw2, 0));
                occupied[nw2][0] = true;
            }
            obstacles.add(new PlaceObstacle(ObstacleType.Dark, 1, 1));
            occupied[1][1] = true;
            obstacles.addAll(placeDarks(2, 1, darksPerCorner));
            // bottom right
            for (int se1 = 1; se1 <= slowsPerWall; se1++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, maeze.getMaxX() - (1 + se1), maeze.getMaxY() - 1));
                occupied[maeze.getMaxX() - (1 + se1)][maeze.getMaxY() - 1] = true;
            } for (int se2 = 1; se2 <= slowsPerWall; se2++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, maeze.getMaxX() - 1, maeze.getMaxY() - (1 + se2)));
                occupied[maeze.getMaxX() - 1][maeze.getMaxY() - (1 + se2)] = true;
            }
            obstacles.add(new PlaceObstacle(ObstacleType.Dark, maeze.getMaxX() - 2, maeze.getMaxY() - 2));
            occupied[maeze.getMaxX() - 2][maeze.getMaxY() - 2] = true;
            obstacles.addAll(placeDarks(maeze.getMaxX() - 3, maeze.getMaxY() - 2, darksPerCorner));
        } else {
            // top right
            for (int ne1 = 1; ne1 <= slowsPerWall; ne1++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, maeze.getMaxX() - 1, ne1));
                occupied[maeze.getMaxX() - 1][ne1] = true;
            } for (int ne2 = 1; ne2 <= slowsPerWall; ne2++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, maeze.getMaxX() - (1 + ne2), 0));
                occupied[maeze.getMaxX() - (1 + ne2)][0] = true;
            }
            obstacles.add(new PlaceObstacle(ObstacleType.Dark, maeze.getMaxX() - 2, 1));
            occupied[maeze.getMaxX() - 2][1] = true;
            obstacles.addAll(placeDarks(maeze.getMaxX() - 3, 1, darksPerCorner));
            // bottom left
            for (int se1 = 1; se1 <= slowsPerWall; se1++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, 0, maeze.getMaxY() - (1 + se1)));
                occupied[0][maeze.getMaxY() - (1 + se1)] = true;
            } for (int se2 = 1; se2 <= slowsPerWall; se2++) {
                obstacles.add(new PlaceObstacle(ObstacleType.Slow, se2, maeze.getMaxY() - 1));
                occupied[se2][maeze.getMaxY() - 1] = true;
            }
            obstacles.add(new PlaceObstacle(ObstacleType.Dark, 1, maeze.getMaxY() - 2));
            occupied[1][maeze.getMaxY() - 2] = true;
            obstacles.addAll(placeDarks(2, maeze.getMaxY() - 2, darksPerCorner));
        }
//        obstacles.addAll(placeStones(stones));
        System.out.println("stones:\t" + stones);
        
        return obstacles;
    }
    
    /*
    * Spirals darks in from corners.
    * Chances of a dark appearing in a particular spot are tied to the difficulty of reaching that spot
    * The more walls a spot has, the less likely an obstacle is to appear there
    * Spiral pattern adapted from https://www.geeksforgeeks.org/print-given-matrix-reverse-spiral-form/
    */
    private List<PlaceObstacle> placeDarks(int x, int y, int darks) {
        ArrayList<PlaceObstacle> darkPlacements = new ArrayList<PlaceObstacle>();
        /* k - starting row index
        l - starting column index*/
        int i, k = x, l = y;
                
        // Total spots in maze
        int m = maeze.getMaxX() - 1, n = maeze.getMaxY() - 1;
        int size = m * n;
                
        while (k < m && l < n) {
            for (i = l; i < n; ++i) {
                if (darkGoesHere(maeze.getLocation(i, l)) && darks > 0) {
                    darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, i, l));
                    occupied[i][l] = true;
                    darks--;
                }
            } k++;
            for (i = k; i < m; ++i) {
                if (darkGoesHere(maeze.getLocation(i, n-1)) && darks > 0) {
                    darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, i, n-1));
                    occupied[i][n-1] = true;
                    darks--;
                }
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    if (darkGoesHere(maeze.getLocation(m-1, i)) && darks > 0) {
                        darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, m-1, i));
                        occupied[m-1][i] = true;
                        darks--;
                    }
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    if (darkGoesHere(maeze.getLocation(i, l)) && darks > 0) {
                        darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, i, l));
                        occupied[i][l] = true;
                        darks--;
                    }
                } l++;
            }
        }
        while (darks > 0) {
            System.out.print(darks + " ");
            int a = randy.nextInt(m), b = randy.nextInt(n);
            if (darkGoesHere(maeze.getLocation(a, b))) {
                darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, a, b));
                occupied[a][b] = true;
                darks--;
            }
        }
        return darkPlacements;
    }
    
    /*
    * Finds a spot for the stone(s).
    * Stones ONLY go in spots with the fewest walls
    * Spiral pattern adapted from https://www.geeksforgeeks.org/print-given-matrix-reverse-spiral-form/
    */
    private List<PlaceObstacle> placeStones(int stones) {
        ArrayList<PlaceObstacle> stonePlacements = new ArrayList<PlaceObstacle>();
        ArrayList<MazeLocation> possiblePlacements = new ArrayList<MazeLocation>();
                
        /* k - starting row index
        l - starting column index*/
        int i, k = 0, l = 0;
        
        // Total spots in maze
        int m = maeze.getMaxX(), n = maeze.getMaxY();
        int size = m * n;
        
        // locations with this many directions are allowed to get a stone
        int threshold = 4;
        
        while (k < m && l < n) {
            for (i = l; i < n; ++i) {
                possiblePlacements.add(maeze.getLocation(k, i));
            } k++;
            for (i = k; i < m; ++i) {
                possiblePlacements.add(maeze.getLocation(i, n-1));
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    possiblePlacements.add(maeze.getLocation(m-1, i));
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    possiblePlacements.add(maeze.getLocation(i, l));
                } l++;
            }
        }
        
        // old school for loop
        while (stones > 0) {
            for (int p = possiblePlacements.size() - 1; p > 0; p--) {
                MazeLocation ml = possiblePlacements.get(p);
                if (!occupied[ml.getX()][ml.getY()] && ml.getDirections().size() >= threshold && stones > 0) {
                    System.out.println("Stone placed at " + ml.getX() + ", " + ml.getY());
                    stonePlacements.add(new PlaceObstacle(ObstacleType.Stone, ml.getX(), ml.getY()));
                    occupied[ml.getX()][ml.getY()] = true;
                    stones--;
                }
            }
            threshold--;
        }
        
        return stonePlacements;
    }
    
    private boolean darkGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() == 0) return false;
        if (l.getDirections().size() > 3) return true;
        if (l.getDirections().size() <= 3) return randy.nextBoolean();
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
        
        if (flipped) {
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, 0, 0)); // top left
            occupied[0][0] = true;
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, maeze.getMaxX() - 1, maeze.getMaxY() - 1)); // bottom right
            occupied[maeze.getMaxX() - 1][maeze.getMaxY() - 1] = true;
        } else {
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, maeze.getMaxX() - 1, 0)); // top right
            occupied[maeze.getMaxX() - 1][0] = true;
            coinPlacements.add(new PlaceCoin(CoinType.Diamond, 0, maeze.getMaxY() - 1)); // bottom left
            occupied[0][maeze.getMaxY() - 1] = true;
        }
        
        coinPlacements.addAll(hideGold(golds, state));
        return coinPlacements;
    }
    
    /*
     * Spirals gold coins out from center.
     * Chances of a coin appearing in a particular spot are tied to the difficulty of reaching that spot
     * The more walls a spot has, the more likely a coin is to appear there
     * Spiral pattern adapted from https://www.geeksforgeeks.org/print-given-matrix-reverse-spiral-form/
     */
    private List<PlaceCoin> hideGold(int coins, GameState state) {
        ArrayList<MazeLocation> possiblePlacements = new ArrayList<MazeLocation>();
        ArrayList<PlaceCoin> goldPlacements = new ArrayList<PlaceCoin>();
        
        /* k - starting row index
        l - starting column index*/
        int i, k = 0, l = 0;
        
        // Total spots in maze
        int m = maeze.getMaxX(), n = maeze.getMaxY();
        int size = m * n;
        
        while (k < m && l < n) {
            for (i = l; i < n; ++i) {
                possiblePlacements.add(maeze.getLocation(k, i));
            } k++;
            for (i = k; i < m; ++i) {
                possiblePlacements.add(maeze.getLocation(i, n-1));
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    possiblePlacements.add(maeze.getLocation(m-1, i));
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    possiblePlacements.add(maeze.getLocation(i, l));
//                    if (coinGoesHere(maeze.getLocation(i, l)))
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
    
    private boolean coinGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() == 4) return false;
        if (l.getDirections().size() <= 1) return true;
        if (randy.nextInt(8) < l.getDirections().size()) return true;
        return false;
    }
}
