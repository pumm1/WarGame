package logic;

import java.util.ArrayList;
import java.util.List;
import units.GameObject;
import units.Squad;

public class Player {

    private List<GameObject> units = new ArrayList<>();
    private int i;  //tells apart the players
    private int maxAP = 10;

    public Player(int a) {

        i = a;
    }

    public void addUnit(Squad p) {
        units.add(p);
    }

    public void resetUints() {
        units.clear();
    }

    public List<GameObject> getUnits() {
        return units;
    }

    public int getAp() {
        return maxAP;
    }

}
