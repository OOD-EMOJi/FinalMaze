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
        List<Coin> removedCoins = new ArrayList<Coin>();
        cloned_list.addAll(hiddenCoins);
		//System.out.println("HiddenCoins: " + hiddenCoins.size());
        for (int i = 0; i < turnsLeft && i < path.size(); i++) {

            // get hidden coins in range of scout radius
            // For dark spots that affect revealling hiddencoins,we should skip to the next tile using "continue Statement, with a Label"
           
            
  /*start:{*/int x = path.get(i).getX();
            int y = path.get(i).getY();
            System.out.println("                        At Center " + " x = "+ x + " y = " + y);
            int height = maze.tiles[0].length;
            int width = maze.tiles.length;
            int[][] SHIFT = {{0,0},{0, 2}, {2, 0}, {0, -2}, {-2, 0} };// {0,0} to search the current tile itself, of course there is a better way of doing this!😅
            for(int counter= 0; counter < 4 ; counter++){
                for(int[] shift: SHIFT){
                    System.out.println("shift[0] =  " + shift[0] + "  shift[1] =  " + shift[1] + "                             counter * shift[0] =  "+ (counter * (shift[0])) + "   counter * shift[1] = " +(counter *(shift[1])) );
                    int change1 = (counter * (shift[0]));
                    int change2 = (counter * (shift[1]));
                    System.out.println("   change1 = " +change1 + "     change2  =  "+   change1 );
                    x = x + change1 ;
                    y = y + change2 ;
                    System.out.println(" Before : " + " tile x: "+ x);
                    System.out.println(" Before : " + " tile y: "+ y);
                    if (x >= 0 && x < width && y >= 0 && y < height){
                        //List<Thing> contents = maze.tiles[x][y].getContents();
                        for (Coin coin: cloned_list) {
                            //if (t instanceof Dark) { i++ ; break start;}
                            //if (t instanceof Slow) i++ ;
                            System.out.println("coin x: " + coin.getX() + " tile x: "+ x);
                            System.out.println("coin y: " + coin.getY() + " tile y: "+ y);
							//System.out.println("Is same x: " + (coin.getX() == x));
							//System.out.println("Is same y: " + (coin.getY() == y));
							//System.out.println("Is Hidden: " +  !removedCoins.contains(coin));
							//System.out.println("List: " + cloned_list.size());
                            if (coin.getX() == x && coin.getY() == y  && !removedCoins.contains(coin)) {
                                points += 1;
                                System.out.println("                   ScoutPathOption points =  " + points);
                                earlierFactor += i;
                                removedCoins.add(coin);
                                i++;
                            }
                        }  cloned_list.removeAll(removedCoins);  
                    }
                }
            }
        
      }
    }

}


