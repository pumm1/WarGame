package units;

import java.io.IOException;
import java.util.Random;

public class Squad extends GameObject {

    private int owner;
    private int health;
    private int morale;
    private int range = 4;
    private boolean underFire = false;
    private int[][] explosion;
    private Random r = new Random();

    public Squad(int a, int b) throws IOException {
        super(a, b);
        this.setIcon("squad0.png");
        health = maxHealth;
        morale = 10;
    }

    public void moveTo(int a, int b, int[][] e) throws IOException {
        if (underFire) {
            this.notUnderFire();
        }
        x = a;
        y = b;
        explosion = e;
        if (explosion[x][y] == 1) {
            if (health > 50) {
                if (owner == 0) {
                    this.setIcon("squad0hole.png");
                } else {
                    this.setIcon("squad1hole.png");
                }
            } else if (health <= 50 && health > 0) {
                if (owner == 0) {
                    this.setIcon("squad02hole.png");
                } else {
                    this.setIcon("squad12hole.png");
                }
            }
        } else {
            System.out.println("ei reikää");
            if (health > 50) {
                if (owner == 0) {
                    this.setIcon("squad0.png");
                } else {
                    this.setIcon("squad1.png");
                }
            } else if (health <= 50 && health > 0) {
                if (owner == 0) {
                    this.setIcon("squad02.png");
                } else {
                    this.setIcon("squad12.png");
                }
            }
        }
    }

    public int getRange() {
        return range;
    }

    public void getHit(int[][] e) throws IOException {
        health -= 50;
        explosion = e;
        if (health > 0 && health <= 50) {
            if (explosion[x][y] == 0) {
                if (owner == 0) {
                    this.setIcon("squad02.png");
                } else {
                    this.setIcon("squad12.png");
                }
            } else {
                if (owner == 0) {
                    this.setIcon("squad02hole.png");
                } else {
                    this.setIcon("squad12hole.png");
                }
            }
        } else if (health < 0) {
            health = 0;
        }
    }

    public void underFire(int[][] e) throws IOException {
        explosion = e;
        underFire = true;
        if (morale > 2) {
            morale--;
        }
        if (health > 50) {
            if (explosion[x][y] == 0) {
                if (owner == 0) {
                    this.setIcon("squad0miss.png");
                } else {
                    this.setIcon("squad1miss.png");
                }
            } else {
                if (owner == 0) {
                    this.setIcon("squad0holemiss.png");
                } else {
                    this.setIcon("squad1holemiss.png");
                }
            }
        } else if (health <= 50 && health > 0) {
            if (explosion[x][y] == 0) {

                if (owner == 0) {
                    System.out.println("ei reikää ja <50");
                    this.setIcon("squad02miss.png");
                } else {
                    this.setIcon("squad12miss.png");
                }
            } else {
                if (owner == 0) {
                    this.setIcon("squad02holemiss.png");
                } else {
                    this.setIcon("squad12holemiss.png");
                }
            }
        }
    }

    public void notUnderFire() throws IOException {
        underFire = false;
        if (morale < 10) {
            morale++;
        }
        if (owner == 0) {
            if (health > 50) {
                this.setIcon("squad0.png");
            } else {
                this.setIcon("squad02.png");
            }
        } else {
            if (health > 50) {
                this.setIcon("squad1.png");
            } else {
                this.setIcon("squad12.png");
            }
        }
    }

    public int getHealth() {
        return health;
    }
    
    public int getMorale(){
        return morale;
    }

    public void attackPlatoon(Squad p, int[][] e) throws IOException {
        int roll = r.nextInt(20);
        
        p.getHit(e);
        if (p.getHealth() == 0) {
            p.destroy();
        }
    }

    public void setOwner(int a) throws IOException {
        owner = a;
        if (owner == 1) {
            this.setIcon("squad1.png");
        }
    }

    public int getOwner() {
        return owner;
    }

}
