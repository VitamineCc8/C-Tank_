package com.Project__.Tank__.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;

    Vector<EnemyTank> enemyTanks = new Vector<>();
    Vector<Node> nodes = new Vector<>();
    Vector<Bomb> bombs = new Vector<>();
    int enemyTankSize = 3;

    //定义三张图片用于显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;


    public MyPanel(String key) throws IOException {
        File file = new File(Recorder.getRecordFile());
        if (file.exists()) {
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("记录不存在，只能重新开始游戏！");
            key = "1";
        }
        Recorder.setEnemyTanks(enemyTanks);
        hero = new Hero(500, 100);
        hero.setSpeed(5);
        image1 = Toolkit.getDefaultToolkit().getImage("E:\\visual studio series\\java-Work\\learnprogress\\src\\bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage("E:\\visual studio series\\java-Work\\learnprogress\\src\\bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage("E:\\visual studio series\\java-Work\\learnprogress\\src\\bomb_3.gif");

        switch (key) {
            case "1":
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(2);
                    new Thread(enemyTank).start();
                    //只发射一颗子弹
//            Shoot_ shoot_ = new Shoot_(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
//            enemyTank.shoot_s.add(shoot_);
//            Thread thread = new Thread(shoot_);
//            thread.start();
                    enemyTanks.add(enemyTank);
                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(node.getDirect());
                    new Thread(enemyTank).start();
                    //只发射一颗子弹
//            Shoot_ shoot_ = new Shoot_(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
//            enemyTank.shoot_s.add(shoot_);
//            Thread thread = new Thread(shoot_);
//            thread.start();
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("你的选择输入有错误");
        }

        new AePlayWave("src/com/Project__/Tank__/tankgame/111.wav").start();
    }

    public void showInfo(Graphics g) {
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累积击毁敌方坦克", 1020, 30);
        drawTank(1020, 60, g, 0, 0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTank() + " ", 1080, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        showInfo(g);
        g.fillRect(0, 0, 1000, 750);

        if (hero != null && hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
        }

        for (EnemyTank enemyTank : enemyTanks) {
            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);
                for (int j = 0; j < enemyTank.shoot_s.size(); j++) {
                    //取出子弹
                    Shoot_ shoot_ = enemyTank.shoot_s.get(j);
                    //绘制子弹
                    if (shoot_.isLive) {
                        g.draw3DRect(shoot_.x, shoot_.y, 2, 2, false);
                    } else {
                        enemyTank.shoot_s.remove(shoot_);
                    }
                }
            }
        }

        //画出主角的子弹
        //g.fill3DRect(hero.shoot.x, hero.shoot.y,5,5,false);
        for (int i = 0; i < hero.shoot_s.size(); i++) {
            Shoot_ shoot_ = hero.shoot_s.get(i);
            if (shoot_ != null && shoot_.isLive) {
                g.draw3DRect(shoot_.x, shoot_.y, 2, 2, false);
            } else {
                hero.shoot_s.remove(shoot_);
            }
        }

        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            bomb.lifeDown();
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }

    }


    /**
     * @param x      坦克的左上角x坐标
     * @param y      坦克的左上角y坐标
     * @param g      画笔
     * @param direct 坦克方向
     * @param type   坦克类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {

        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
            default:
                g.setColor(Color.cyan);
                break;
        }


        //方向：0向上，1向右，2向下，3向左
        switch (direct) {
            case 0://向上
                g.fill3DRect(x, y, 10, 60, false);  //左
                g.fill3DRect(x + 30, y, 10, 60, false); //右
                g.fill3DRect(x + 10, y + 10, 20, 40, false); //方盖
                g.fillOval(x + 10, y + 20, 20, 20); //圆盖
                g.drawLine(x + 20, y + 30, x + 20, y); //炮筒
                break;
            case 1:
                g.fill3DRect(x, y, 60, 10, false);  //上边
                g.fill3DRect(x, y + 30, 60, 10, false);  //下
                g.fill3DRect(x + 10, y + 10, 40, 20, false);  //方盖
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2://向上
                g.fill3DRect(x, y, 10, 60, false);  //左
                g.fill3DRect(x + 30, y, 10, 60, false); //右
                g.fill3DRect(x + 10, y + 10, 20, 40, false); //方盖
                g.fillOval(x + 10, y + 20, 20, 20); //圆盖
                g.drawLine(x + 20, y + 30, x + 20, y + 60); //炮筒
                break;
            case 3:
                g.fill3DRect(x, y, 60, 10, false);  //上边
                g.fill3DRect(x, y + 30, 60, 10, false);  //下
                g.fill3DRect(x + 10, y + 10, 40, 20, false);  //方盖
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
            default:
                System.out.println("请输入正确的按键！");
        }
    }

    //我方坦克击中对方坦克
    public void hitEnemyTank() {
        for (int j = 0; j < hero.shoot_s.size(); j++) {
            Shoot_ shoot_ = hero.shoot_s.get(j);
            if (shoot_ != null && shoot_.isLive) {
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(shoot_, enemyTank);
                }
            }
        }
    }


    //敌方坦克击中我方坦克
    public void hitHero() {
        //遍历所有的敌方坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shoot_s.size(); j++) {
                Shoot_ shoot_ = enemyTank.shoot_s.get(j);
                if (hero.isLive && shoot_.isLive) {
                    hitTank(shoot_, hero);
                }
            }
        }
    }


    //是否击中
    public void hitTank(Shoot_ s, Tank tank) {
        switch (tank.getDirect()) {
            case 0:
            case 2:
                if (s.x > tank.getX() && s.x < tank.getX() + 40
                        && s.y > tank.getY() && s.y < tank.getY() + 60) {
                    s.isLive = false;
                    tank.isLive = false;
                    enemyTanks.remove(tank);
                    if (tank instanceof EnemyTank) {
                        Recorder.addAllEnemyTank();
                    }
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if (s.x > tank.getX() && s.x < tank.getX() + 60
                        && s.y > tank.getY() && s.y < tank.getY() + 40) {
                    s.isLive = false;
                    tank.isLive = false;
                    enemyTanks.remove(tank);
                    if (tank instanceof EnemyTank) {
                        Recorder.addAllEnemyTank();
                    }
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDowm();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_J) {
//            if (hero.shoot == null || !hero.shoot.isLive) {    //发射一颗子弹的情况
            hero.shootEnemyTank();
//            }
        }

        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hitHero();
            hitEnemyTank();
            this.repaint();
        }
    }
}
