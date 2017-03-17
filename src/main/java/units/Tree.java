package units;

import java.io.IOException;

public class Tree extends GameObject {

    private boolean destroyed;

    public Tree(int a, int b) throws IOException {
        super(a, b);
        destroyed = false;
        this.setIcon("tree.png");
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

}
