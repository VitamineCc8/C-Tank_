package com.Project__.Tank__.tankgame;

import java.util.Vector;

/**
 * 个人的坦克
 */
public class Hero extends Tank {

    //子弹类
    Shoot_ shoot = null;

    Vector<Shoot_> shoot_s = new Vector<>();

    public Hero(int x, int y) {
        super(x, y);
    }

    public void shootEnemyTank() {

        if (shoot_s.size() == 5) {
            return;
        }

        switch (getDirect()) {
            case 0:
                shoot = new Shoot_(getX() + 20, getY(), 0);
                break;
            case 1:
                shoot = new Shoot_(getX() + 60, getY() + 20, 1);
                break;
            case 2:
                shoot = new Shoot_(getX() + 20, getY() + 60, 2);
                break;
            case 3:
                shoot = new Shoot_(getX(), getY() + 20, 3);
                break;
            default:
                break;
        }

        shoot_s.add(shoot);
        new Thread(shoot).start();

    }
}
