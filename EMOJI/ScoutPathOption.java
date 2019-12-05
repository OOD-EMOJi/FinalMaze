package EMOJI;
import LepinskiEngine.*;
import java.util.*;

public class ScoutPathOption extends PathOption {

    List<Tile> path;
    int turnsLeft;
    int points;
    int earlierFactor;
    List<Coin> hiddenCoins;
    Maze maze;

    public ScoutPathOption(List<Tile> path, int turns, List<Coin> hiddenCoins, Maze maze) {
        super(path,turns);
        this.path = path;
        this.turnsLeft = turns;
        this.points = 0;
        this.earlierFactor = 0;
        this.hiddenCoins = hiddenCoins;
        this.maze = maze;
    }

    public void countPoints() {
        points = 0;
        earlierFactor = 0;
        List<Coin> cloned_list= new ArrayList<Coin>();
        cloned_list.addAll(hiddenCoins);
		System.out.println("HiddenCoins: " + hiddenCoins.size());
        for (int i = 0; i < turnsLeft && i < path.size(); i++) {

            // get hidden coins in range of scout radius
            // For dark spots that affect revealling hiddencoins,we should skip to the next tile using "continue Statement, with a Label"
           
            
     start:{int x = path.get(i).getX();
            int y = path.get(i).getY();
            int height = maze.tiles[0].length;
            int width = maze.tiles.length;
            int[][] SHIFT = {{0,0},{0, 2}, {2, 0}, {0, -2}, {-2, 0} };// {0,0} to search the current tile itself, of course there is a better way of doing this!😅
            for(int counter= 0; counter < 3 ; counter++){
                for(int[] shift: SHIFT){
                    x = x + (shift[0]*counter);
                    y = y + (shift[1]*counter);
                    if (x >= 0 && x < width && y >= 0 && y < height){
                        List<Thing> contents = maze.tiles[x][y].getContents();
                        for (Thing t : contents) {
                            if (t instanceof Dark) { i++ ; break start;}
                            if (t instanceof Slow) i++ ;
							System.out.println("Is Coin: " + (t instanceof Coin));
							System.out.println("Is Gold: " + (((Coin) t).getValue()==1));
							System.out.println("Is Hidden: " +  cloned_list.contains(t));
							System.out.println("List: " + cloned_list.size());
                            if (t instanceof Coin && ((Coin) t).getValue()==1 && cloned_list.contains(t)) {
                                points += ((Coin) t).getValue();
                                earlierFactor += i;
                                cloned_list.remove(t);
                                i++;
                            }
                        }    
                    }
                }
            }
        }
      }
    }

}


