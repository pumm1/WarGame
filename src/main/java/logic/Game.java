package logic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import units.GameObject;
import units.House;
import units.Squad;
import units.Tank;
import units.Tree;

public class Game extends JFrame implements KeyListener {
    /////////NOTE: X-AXIS POINTSDOWN, Y-AXIS RIGHT ///////////////////////// (because... reasons)

    private String console;
    private Container contents;
    private JButton[][] squares;
    private GameObject[][] units = new GameObject[20][20];
    private GameObject[][] objects = new GameObject[20][20];
    private int[][] explosion = new int[20][20];
    private int turn = 0;
    private int ap = 10;
    private int arty = 0;
    private ButtonHandler buttonHandler;
    private InputStream is0 = getClass().getClassLoader().getResourceAsStream("ground1.png");
    private BufferedImage ground0 = ImageIO.read(is0);
    private ImageIcon ground1 = new ImageIcon(ground0);
    private InputStream is1 = getClass().getClassLoader().getResourceAsStream("hole0.png");
    private BufferedImage hole0 = ImageIO.read(is1);
    private ImageIcon hole = new ImageIcon(hole0);
    private Random rand = new Random();
    private Player p0 = new Player(0);
    private Player p1 = new Player(1);
    private Squad chosen;
    private Squad temp;
    private Tank tempTank;
    private JTextArea txt;

    public Game() throws IOException {
        super("Taistelutanner");
        console = "";
        squares = new JButton[20][20];
//        initObjects();
        JPanel panel = new JPanel(new GridLayout(20, 20));
        contents = getContentPane();
//        contents.setLayout(new GridLayout(20, 20));
        buttonHandler = new ButtonHandler();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                squares[i][j] = new JButton();
                squares[i][j].setIcon(ground1);
                if (objects[i][j] != null) {
                    squares[i][j].setIcon(objects[i][j].getIcon());
                }
//                contents.add(squares[i][j]);
                squares[i][j].addActionListener(buttonHandler);
                squares[i][j].addKeyListener(this);
                panel.add(squares[i][j]);

            }
        }
        this.add(panel);
//        contents.add(panel, BorderLayout.NORTH);
        txt = new JTextArea();
        txt.setText(console);
        txt.setDisabledTextColor(Color.BLACK);
        txt.setPreferredSize(new Dimension(800, 100));
        contents.add(txt, BorderLayout.SOUTH);
        txt.disable();
        this.reset();
        setSize(800, 900);
        setResizable(true);
        setLocationRelativeTo(null); //centers windows
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initObjects() throws IOException { //initialize all units and objects
        int t = 0;
        int x = 0;
        int y = 0;
        p0 = new Player(0);
        p1 = new Player(1);

        while (p0.getUnits().size() < 6) {
            x = rand.nextInt(7) + 2;
            y = rand.nextInt(7) + 2;
            if (units[x][y] == null && objects[x][y] == null) {
                Squad s = new Squad(x, y);
                s.setOwner(0);
                p0.addUnit(s);
                units[x][y] = s;
            }
        }

        while (p0.getUnits().size() < 8) {
            x = rand.nextInt(7) + 2;
            y = rand.nextInt(7) + 2;
            if (units[x][y] == null && objects[x][y] == null) {
                Tank tank = new Tank(x, y);
                tank.setOwner(0);
                p0.addUnit(tank);
                units[x][y] = tank;
            }
        }
        while (p1.getUnits().size() < 6) {
            x = rand.nextInt(8) + 10;
            y = rand.nextInt(8) + 10;
            if (units[x][y] == null && objects[x][y] == null) {
                Squad s = new Squad(x, y);
                s.setOwner(1);
                p1.addUnit(s);
                units[x][y] = s;
            }
        }
        while (p1.getUnits().size() < 8) {
            x = rand.nextInt(8) + 10;
            y = rand.nextInt(8) + 10;
            if (units[x][y] == null && objects[x][y] == null) {
                Tank tank = new Tank(x, y);
                tank.setOwner(1);
                p1.addUnit(tank);
                units[x][y] = tank;
            }
        }
        t = 0;
        int i = 0;

        while (t < 35) { /////GENERATE MAP//////////  
            i = rand.nextInt(4);
            if (t > 12) {
                x = rand.nextInt(20);
                y = rand.nextInt(20);

            } else {
                x = rand.nextInt(8) + 4;
                y = rand.nextInt(8) + 4;
            }
            if (i == 0) {
                House h0 = new House(x, y);
                if (objects[x][y] == null && units[x][y] == null) {
                    objects[x][y] = h0;
                    t++;
                }
            } else {
                Tree t0 = new Tree(x, y);
                if (objects[x][y] == null && units[x][y] == null) {
                    objects[x][y] = t0;
                    t++;
                }
            }
        }

    }

    public void reset() throws IOException {
        console = "---reset--- \n---Game Starts!--- \nBlue side's turn";
        txt.setText(console);
        ap = 10;
        turn = 0;
        chosen = null;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                explosion[i][j] = 0;
                units[i][j] = null;
                objects[i][j] = null;
                squares[i][j].setIcon(ground1);
            }
        }
        p1.resetUints();
        p0.resetUints();
        this.initObjects();
        for (GameObject i : p0.getUnits()) {
            squares[i.getX()][i.getY()].setIcon(i.getIcon());
            units[i.getX()][i.getY()] = i;

        }
        for (GameObject i : p1.getUnits()) {
            squares[i.getX()][i.getY()].setIcon(i.getIcon());
            units[i.getX()][i.getY()] = i;
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (objects[i][j] != null) {
                    squares[i][j].setIcon(objects[i][j].getIcon());
                }
            }
        }

    }

    public void checkConsole() {
        int a = 0;
        String[] lines = console.split("\n");
        a = lines.length;
        if (a == 7) {
            lines[0] = lines[1];
            lines[1] = lines[2];
            lines[2] = lines[3];
            lines[3] = lines[4];
            lines[4] = lines[5];
            lines[5] = lines[6];
            console = lines[0] + "\n" + lines[1] + "\n" + lines[2] + "\n" + lines[3] + "\n" + lines[4] + "\n" + lines[5];
        }

        txt.setText(console);

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (turn == 1) {
                console += "\nBlue side's turn";
                checkConsole();
                turn = 0;
                ap = p0.getAp();
            } else {
                console += "\nRed side's turn";
                checkConsole();
                turn = 1;
                ap = p1.getAp();
            }
            chosen = null;
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            console += "\nCancel order";
            checkConsole();
            chosen = null;
            arty = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_T) {

            if (ap >= 5) {
                console += "\nWhere to aim?";
                checkConsole();
                chosen = null;
                arty = 1;
            } else {
                console += "\nNot enough AP to call in artillery.";
                checkConsole();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            try {
                this.reset();
            } catch (IOException ex) {
                System.out.println("erhe");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    ///////////check what the click does//////////////////
    private void processClick(int i, int j) throws IOException, InterruptedException {
        if (chosen == null) {
            /////////CHOSING SQUAD//////////////
            if (arty == 0) {
                if (units[i][j] != null) {
                    if (units[i][j] instanceof Tank) {
                        tempTank = (Tank) units[i][j];
                        boolean d = tempTank.destroyed();
                        if (tempTank.getOwner() == turn && !d) {
                            console += "\nUnit chosen";
                            checkConsole();
                            chosen = tempTank;
                        }
                    } else if (units[i][j] instanceof Squad) {

                        temp = (Squad) units[i][j];

                        if (temp.getOwner() == turn) {
                            console += "\nUnit chosen";
                            checkConsole();
                            chosen = temp;
                        }
                        temp = null;
                        tempTank = null;
                    }
                }
            } else {     ////////ORDERING ARTILLERY////////
                int x = 0;
                int y = 0;
                int hit = rand.nextInt(20);
                if (hit < 12) {
                    console += "\nWho gave the coordinates?!";
                    this.checkConsole();
                    x = rand.nextInt(2) + 1;
                    y = rand.nextInt(2) + 1;
                    int tx = rand.nextInt(2);
                    int ty = rand.nextInt(2);
                    if (tx != 0) {
                        tx = tx * (-1);
                    }
                    if (ty != 0) {
                        ty = ty * (-1);
                    }
                    i = i + x;
                    j = j + y;
                    if (i < 0) {
                        i = 0;
                    } else if (i >= 20) {
                        i = 19;
                    }
                    if (j < 0) {
                        j = 0;
                    } else if (j >= 20) {
                        j = 19;
                    }
                }
                explosion[i][j] = 1;
                if (units[i][j] != null) {
                    if (units[i][j] instanceof Squad && !(units[i][j] instanceof Tank)) {
                        temp = (Squad) units[i][j];
                        temp.getHit(explosion);
                        if (temp.getHealth() > 0) {
                            console += "\nDirect hit, still alive";
                            checkConsole();
                            temp = null;
                        } else {
                            console += "\nDirect hit, target is destroyed";
                            checkConsole();
//                            if (units[i][j] instanceof Tank) {
//                                temp = (Tank) units[i][j];
//                                squares[i][j].setIcon(temp.getIcon());
//                                tempTank = null;
//                            } else {
                            units[i][j] = null;
                            temp = null;
                            squares[i][j].setIcon(hole);
//                            }

                        }
                    } else if (units[i][j] instanceof Tank) {
                        tempTank = (Tank) units[i][j];
                        tempTank.getHit(explosion);
                        if (tempTank.getHealth() > 0) {
                            console += "\nDirect hit, still alive";
                            checkConsole();
                            squares[i][j].setIcon(tempTank.getIcon());
                        } else {
                            console += "\nDirect hit, target is destroyed";
                            checkConsole();
                            squares[i][j].setIcon(tempTank.getIcon());
                        }
                    }
                    temp = null;
                    tempTank = null;
                } else {
                    console += "\nNothing got hit..";
                    checkConsole();
                }

                objects[i][j] = null;
//                if (units[i][j] == null) {
//                    squares[i][j].setIcon(hole);
//                } else {
//                    squares[i][j].setIcon(units[i][j].getIcon());
//                }
                if (units[i][j] == null) {
                    squares[i][j].setIcon(hole);
                } else {
                    squares[i][j].setIcon(units[i][j].getIcon());
                }
                arty = 0;
                ap -= 5;
                System.out.println("AP: " + ap);
            }
        } else {
            if (units[i][j] == null && objects[i][j] == null) { //won't allow overlapping!
                ///////////////MOVING//////////////
                if (Math.abs(chosen.getX() - i) + Math.abs(chosen.getY() - j) <= ap) {
                    if (chosen instanceof Tank) {
                        Tank tem = (Tank) chosen;
                        boolean canMove = tem.canMove();
                        if (canMove) {
                            int t = Math.abs(chosen.getX() - i) + Math.abs(chosen.getY() - j);
                            ap = ap - t;
                            System.out.println("AP: " + ap);
                            console += "\nAP: " + ap;
                            checkConsole();
                            int x = chosen.getX();
                            int y = chosen.getY();

                            chosen.moveTo(i, j, explosion);
                            units[x][y] = null;
                            units[i][j] = chosen;
                            if (explosion[x][y] == 0) {
                                squares[x][y].setIcon(ground1);
                            } else {
                                squares[x][y].setIcon(hole);
                            }
                            squares[i][j].setIcon(units[i][j].getIcon());
                            x = chosen.getX();
                            y = chosen.getY();
                            chosen = null;
                        } else {
                            console += "\nChosen unit is stuck";
                            checkConsole();
                            chosen = null;
                        }
                    } else {
                        int t = Math.abs(chosen.getX() - i) + Math.abs(chosen.getY() - j);
                        ap = ap - t;
                        System.out.println("AP: " + ap);
                        console += "\nAP: " + ap;
                        checkConsole();
                        int x = chosen.getX();
                        int y = chosen.getY();

                        chosen.moveTo(i, j, explosion);
                        units[x][y] = null;
                        units[i][j] = chosen;
                        if (explosion[x][y] == 0) {
                            squares[x][y].setIcon(ground1);
                        } else {
                            squares[x][y].setIcon(hole);
                        }
                        squares[i][j].setIcon(units[i][j].getIcon());
                        x = chosen.getX();
                        y = chosen.getY();
                        chosen = null;
                    }
                } else {
                    console += "\nNot enough AP..";
                    checkConsole();
                    System.out.println("ei riitä AP");
                }
            } else if (units[i][j] != null && units[i][j] instanceof Squad) {
                ///////ATTACKING//////////
                if (units[i][j] instanceof Squad && !(units[i][j] instanceof Tank)) {
                    Squad t = (Squad) units[i][j];

                    if (t.getOwner() != chosen.getOwner()) {
                        if (ap >= 4) {
                            if (Math.abs(t.getX() - chosen.getX()) + Math.abs(t.getY() - chosen.getY()) <= chosen.getRange()) {
                                if (canHit(chosen, i, j)) {
                                    int roll = rand.nextInt(20);
                                    if (roll > 10) {
                                        chosen.attackPlatoon(t, explosion);
                                        console += "\nTarget is hit";
                                        checkConsole();
                                        squares[i][j].setIcon(t.getIcon());
                                        if (t.getHealth() == 0) { /////////SQUAD DESTROYED//////////
                                            console += "\nTarget destroyed";
                                            checkConsole();
                                            units[i][j] = null;
                                            squares[i][j].setIcon(ground1);
                                        } else {
                                            console += "\nTarget HP left: " + t.getHealth();
                                            checkConsole();
                                        }
                                    } else {
                                        console += "\nMiss!";
                                        checkConsole();
                                        t.underFire(explosion);
                                        squares[t.getX()][t.getY()].setIcon(t.getIcon());
                                    }
                                    ap -= 4;
                                    chosen = null;
                                    console += "\nAP left: " + ap;
                                    checkConsole();

                                }
                            } else {
                                console += "\nNot enough range";
                                checkConsole();
                            }
                        } else {
                            console += "\nNot enough AP";
                            checkConsole();
                        }

                    }
                } else if (units[i][j] instanceof Tank) {
                    tempTank = (Tank) units[i][j];
                    if (tempTank.getOwner() != chosen.getOwner()) {
                        if (ap >= 4) {
                            if (Math.abs(tempTank.getX() - chosen.getX()) + Math.abs(tempTank.getY() - chosen.getY()) <= chosen.getRange()) {
                                if (canHit(chosen, i, j)) {
                                    int roll = rand.nextInt(20);
                                    if (roll > 10) {
                                        chosen.attackPlatoon(tempTank, explosion);
                                        console += "\nTarget is hit";
                                        checkConsole();
                                        squares[i][j].setIcon(tempTank.getIcon());
                                        if (tempTank.getHealth() == 0) { /////////TANK DESTROYED//////////
                                            console += "\nTarget destroyed";
                                            checkConsole();
                                            squares[i][j].setIcon(tempTank.getIcon());
                                        } else {
                                            console += "\nTarget HP left: " + tempTank.getHealth();
                                            checkConsole();
                                        }
                                    } else {
                                        console += "\nMiss!";
                                        checkConsole();
                                        tempTank.underFire(explosion);
                                        squares[tempTank.getX()][tempTank.getY()].setIcon(tempTank.getIcon());
                                    }
                                    ap -= 4;
                                    chosen = null;
                                    console += "\nAP left: " + ap;
                                    checkConsole();

                                }
                            } else {
                                console += "\nNot enough range";
                                checkConsole();
                            }
                        }
                    }
                }
            }

        }
    }

    ///////////check if you can hit the target///////////////////
    public boolean canHit(Squad p, int i, int j) {
        int x = p.getX();
        int y = p.getY();

        if (i < x && y == j) {
            for (int a = x; a > i - 1; a--) {
                if (objects[a][j] != null) {
                    console += "\nCan't hit up";
                    checkConsole();
                    return false;
                }
            }
        } else if (i < x && j < y) {
            for (int a = x; a > i - 1; a--) {
                for (int b = y; b > j - 1; b--) {
                    if (objects[a][b] != null) {
                        if (Math.abs(x - i) + Math.abs(y - j) != 2) {
                            console += "\nCan't hit up left diagonally";
                            checkConsole();
                            return false;
                        }
                    }
                }
            }
        } else if (i == x && j < y) {
            for (int b = y; b > j - 1; b--) {
                if (objects[x][b] != null) {
                    console += "\nCan't hit left";
                    checkConsole();
                    return false;
                }
            }
        } else if (i > x && j < y) {
            for (int a = x; a < i + 1; a++) {
                for (int b = y; b > j - 1; b--) {
                    if (objects[a][b] != null) {
                        if (Math.abs(x - i) + Math.abs(y - j) != 2) {
                            console += "\nCan't hit down left diagonally";
                            checkConsole();
                            return false;
                        }
                    }
                }
            }
        } else if (i > x && j == y) {
            for (int a = x; a < i + 1; a++) {
                if (objects[a][y] != null) {
                    console += "\nCan't hit down";
                    checkConsole();
                    return false;
                }
            }
        } else if (i > x && j > y) {
            for (int a = x; a < i + 1; a++) {
                for (int b = y; b < j + 1; b++) {
                    if (objects[a][b] != null) {
                        if (Math.abs(x - i) + Math.abs(y - j) != 2) {
                            console += "\nCan't hit down right diagonally";
                            checkConsole();
                            return false;
                        }
                    }
                }
            }
        } else if (i == x && j > y) {
            for (int b = y; b < j + 1; b++) {
                if (objects[x][b] != null) {
                    System.out.println("can't hit right");
                    console += "\nCan't hit right";
                    checkConsole();
                    return false;
                }
            }
        } else if (i < x && j > y) {
            for (int a = x; a > i - 1; a--) {
                for (int b = y; b < j + 1; b++) {
                    if (objects[a][b] != null) {
                        if (Math.abs(x - i) + Math.abs(y - j) != 2) {
                            console += "\nCan't hit up right diagonally";
                            checkConsole();
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    if (source == squares[i][j]) {
                        try {
                            processClick(i, j);
                        } catch (IOException ex) {
                            System.out.println("erhe");
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                }
            }
        }
    }

}
