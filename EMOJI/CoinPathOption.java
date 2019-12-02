package EMOJI;

import java.util.*;

public class CoinPathOption extends PathOption {

    List<Tile> path;
    int turnsLeft;
    int points;
    int earlierFactor;

    public CoinPathOption(List<Tile> path, int turns) {
        super(path, turns);
    }

    public void countPoints() {
        points = 0;
        earlierFactor = 0;
        for (int i = 0; i < turnsLeft && i < path.size(); i++) {
            List<Thing> contents = path.get(i).getContents();
            for (Thing t : contents) {
                if (t instanceof Coin) {
                    points += ((Coin) t).getValue();
                    earlierFactor += i;
                    i++;
                }
            }
        }
    }



}
