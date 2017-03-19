/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import java.io.IOException;

/**
 *
 * @author Sagu
 */
public class Tank extends Squad {

    private boolean canMove;

    public Tank(int a, int b) throws IOException {
        super(a, b);
//        this.setIcon("tank0.png");
        maxHealth = 100;
        health = maxHealth;
        canMove = true;
        destroyed = false;
    }

    @Override
    public void setOwner(int a) throws IOException {
        owner = a;
        if (owner == 1) {
            this.setIcon("tank1.png");
        } else if (owner == 0) {
            this.setIcon("tank0.png");
        }
    }

    public boolean checkForHoles(int a, int b, int[][] e) throws IOException {
        if (e[a][b] == 1) {
            return false;
        }
        moveTo(a, b, e);
        return true;
    }

    public boolean destroyed() {
        return destroyed;
    }

    public boolean canMove() {
        return canMove;
    }

    @Override
    public void getHit(int[][] e) throws IOException {
        health -= 50;
        explosion = e;
        if (health > 0 && health <= 50) {
            if (explosion[x][y] == 0) {
                if (owner == 0) {
                    this.setIcon("tank02.png");
                } else {
                    this.setIcon("tank12.png");
                }
            } else {
                canMove = false;
                if (owner == 0) {
                    this.setIcon("tank02hole.png");
                } else {
                    this.setIcon("tank12hole.png");
                }
            }
        } else if (health == 0) {
            destroyed = true;
            if (explosion[x][y] == 0) {
                if (owner == 0) {
                    this.setIcon("tank03.png");
                } else {
                    this.setIcon("tank13.png");
                }
            } else {
                canMove = false;
                if (owner == 0) {
                    this.setIcon("tank03hole.png");
                } else {
                    this.setIcon("tank13hole.png");
                }
            }
        }
    }

    @Override
    public void underFire(int[][] e) throws IOException {
        explosion = e;
        underFire = true;
        if (morale > 2) {
            morale--;
        }
        if (health > 50) {
            if (explosion[x][y] == 0) {
                if (owner == 0) {
                    this.setIcon("tank0miss.png");
                } else {
                    this.setIcon("tank1miss.png");
                }
            } else {
                if (owner == 0) {
                    this.setIcon("tank0holemiss.png");
                } else {
                    this.setIcon("tank1holemiss.png");
                }
            }
        } else if (health == 50) {
            if (explosion[x][y] == 0) {
                if (owner == 0) {
                    this.setIcon("tank02miss.png");
                } else {
                    this.setIcon("tank12miss.png");
                }
            } else {
                if (owner == 0) {
                    this.setIcon("tank02holemiss.png");
                } else {
                    this.setIcon("tank12holemiss.png");
                }
            }
        }
    }

    @Override
    public void moveTo(int a, int b, int[][] e) throws IOException {
        if (canMove) {
            if (underFire) {
                this.notUnderFire();
            }
            x = a;
            y = b;
            explosion = e;
            if (explosion[x][y] == 1) {
                canMove = false;
                if (health == 100) {
                    if (owner == 0) {
                        this.setIcon("tank0hole.png");
                    } else {
                        this.setIcon("tank1hole.png");
                    }
                } else if (health == 50) {
                    if (owner == 0) {
                        this.setIcon("tank02hole.png");
                    } else {
                        this.setIcon("tank12hole.png");
                    }
                } else if (health == 0) {
                    if (owner == 0) {
                        this.setIcon("tank03hole.png");
                    } else {
                        this.setIcon("tank13hole.png");
                    }

                }
            } else {
                if (health == 100) {
                    if (owner == 0) {
                        this.setIcon("tank0.png");
                    } else {
                        this.setIcon("tank1.png");
                    }
                } else if (health == 50) {
                    if (owner == 0) {
                        this.setIcon("tank02.png");
                    } else {
                        this.setIcon("tank12.png");
                    }
                } else if (this.health == 0) {
                    if (owner == 0) {

                    } else {

                    }
                }
            }
        }

    }

}
