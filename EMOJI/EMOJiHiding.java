package EMOJI;
import LepinskiEngine.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class EMOJiHiding implements PlayerHidingTeam {
    
    /*
     * The Mesa, baby
     * Hides:
     * * * Diamond coins like Maeve
     * * * Gold coins like Bernard
     * * * Slows like Maeve
     * * * Darks like Maeve
     * * * Stone like Akecheta
     */
    
    /*
     * This team's strategy is procedurally generated,
     * starting with a coin flip to determine the location of the two diamond coins.
     * true means they appear in the northwest and southeast
     * false puts them in the northeast and southwest
     */
    boolean flipped;
    Random randy = new Random();
    public EMOJiHiding() {
        flipped = randy.nextBoolean();
        System.out.println("Flipped:\t" + flipped);
    }
    
    ArrayList<MazeLocation> darkSpots = new ArrayList<MazeLocation>();
    
    LepinskiEngine.Maze maeze;
    
    boolean[][] occupied;
    GameState state; // not sure this is needed
    
    /* Coin + Obstacle counts */
    int stones = 1, darks = 12, slows = 12, diamonds = 2, golds = 24;
    
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
     *       as well as an assortment of DARK and SLOW obstacles (12 each)
     *
     * Obstacles are NOT objects
     */
    public List<PlaceObstacle> setObstacles(List<ObstacleType> obs, RectMaze maze, GameState state) {
        // this is supposed to get initialized in startgame but im not sure if it actually does
        occupied = new boolean[maze.getMaxX()][maze.getMaxY()];
        maeze = new LepinskiEngine.Maze(maze);
        for (int x=0; x < maze.getMaxX(); x++) {
            for (int y=0; y<maze.getMaxY(); y++) {
                occupied[x][y] = false;
            }
        }
        
        ArrayList<PlaceObstacle> obstacles = new ArrayList<PlaceObstacle>();
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
                    darkSpots.add(new MazeLocation(1, 1));
//                    occupied[1][1] = true;
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
                    darkSpots.add(new MazeLocation(maeze.getMaxX() - 2, maeze.getMaxY() - 2));
//                    occupied[maeze.getMaxX() - 2][maeze.getMaxY() - 2] = true;
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
                    darkSpots.add(new MazeLocation(maeze.getMaxX() - 2, 1));
//                    occupied[maeze.getMaxX() - 2][1] = true;
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
                    darkSpots.add(new MazeLocation(1, maeze.getMaxY() - 2));
//                    occupied[1][maeze.getMaxY() - 2] = true;
                    obstacles.addAll(placeDarks(2, maeze.getMaxY() - 2, darksPerCorner));
                }
        // Place stone
        int cx = maze.getMaxX() / 2, cy = maze.getMaxY() / 2;   // center values
        obstacles.add(new PlaceObstacle(ObstacleType.Stone, cx, cy));
        occupied[cx][cy] = true;
                
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
//                    occupied[i][l] = true;
                    darkSpots.add(maeze.getLocation(i, l));
                    darks--;
                }
            } k++;
            for (i = k; i < m; ++i) {
                if (darkGoesHere(maeze.getLocation(i, n-1)) && darks > 0) {
                    darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, i, n-1));
//                    occupied[i][n-1] = true;
                    darkSpots.add(maeze.getLocation(i, n-1));
                    darks--;
                }
            } n--;
            if (k < m) {
                for (i = n-1; i >= l; --i) {
                    if (darkGoesHere(maeze.getLocation(m-1, i)) && darks > 0) {
                        darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, m-1, i));
//                        occupied[m-1][i] = true;
                        darkSpots.add(maeze.getLocation(m-1, i));
                        darks--;
                    }
                } m--;
            }
            if (l < n) {
                for (i = m-1; i >= k; --i) {
                    if (darkGoesHere(maeze.getLocation(i, l)) && darks > 0) {
                        darkPlacements.add(new PlaceObstacle(ObstacleType.Dark, i, l));
//                        occupied[i][l] = true;
                        darkSpots.add(maeze.getLocation(i, l));
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
                darkSpots.add(maeze.getLocation(a, b));
                darks--;
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
        // Don't worry about it, the game will place everything we miss
        ArrayList<PlaceCoin> coinPlacements = new ArrayList<PlaceCoin>();
        
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
        
        while (golds > 0) {
            for (MazeLocation l : darkSpots) {
                if (golds > 0 && !occupied[l.getX()][l.getY()]) {
                    coinPlacements.add(new PlaceCoin(CoinType.Gold, l.getX(), l.getY()));
                    occupied[l.getX()][l.getY()] = true;
                    golds--;
                }
            }
            int a = randy.nextInt(maze.getMaxX()), b = randy.nextInt(maze.getMaxY());
            if (coinGoesHere(maeze.getLocation(a, b))) {
                coinPlacements.add(new PlaceCoin(CoinType.Gold, a, b));
                occupied[a][b] = true;
                golds--;
            }
        }
        return coinPlacements;
    }
    
    private boolean darkGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() == 0) return false;
        if (l.getDirections().size() > 3) return true;
        if (l.getDirections().size() <= 3) return randy.nextBoolean();
        return false;
    }
    
    /*
     * Criteria for a coin belonging in a particular location:
     * 1. Location doesn't already have a coin or obstacle
     * 2. Location has 2 or more walls
     * 3. Location isn't along any of the guides
     */
    private boolean coinGoesHere (MazeLocation l) {
        if (occupied[l.getX()][l.getY()] || l.getDirections().size() > 2) return false;
        return true;
    }
}
