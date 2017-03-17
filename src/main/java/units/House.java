package units;

import java.io.IOException;

public class House extends GameObject {

    private boolean detroyed;

    public House(int a, int b) throws IOException {
        super(a, b);
        destroyed = false;
        this.setIcon("house1.png");
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

}
