package units;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameObject {

    //all objects in game are subgroub of this
    protected int x;
    protected int y;
    protected int maxHealth = 100;
    protected boolean destroyed = false;
    protected ImageIcon icon;

    public GameObject(int a, int b) {
        x = a;
        y = b;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setIcon(String s) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(s);
        BufferedImage im = ImageIO.read(is);
        icon = new ImageIcon(im);
    }

    public void destroy() {
        destroyed = true;
    }

    public ImageIcon getIcon() {
        return icon;
    }

}
