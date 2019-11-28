package EMOJI;
import java.util.Random;

public class MaeveHiding implements PlayerHidingTeam {
    
    /*
     * Maeve is procedurally generated, starting with a coin flip
     * to determine the location of the two diamond coins
     * true means the diamonds appear in the top left and bottom right corners
     * false puts them in the top right and bottom left
     */
    boolean style;
    Random randy = new Random();
    public MaeveHiding() {
        style = randy.nextBoolean();
    }
    
    public void startGame(List<ObstacleType> obs, List<CoinType> coins, RectMaze maze, GameState state) {
        
    }

    public List<PlaceObstacle> setObstacles(List<ObstacleType> obs, RectMaze maze, GameState state) {
        
    }

    public List<PlaceCoin> hideCoins(List<CoinType> coins, RectMaze maze, GameState state) {
        
    }
}
