package com.Project__.Tank__.tankgame;

import java.io.*;
import java.security.Key;
import java.util.Vector;

//用来记录相关信息
public class Recorder {
    private static int allEnemyTankNum = 0;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private static String RECORD_FILE = "src/com/Project__/Tank__/tankgame/MyRecord.txt";
    private static Vector<EnemyTank> enemyTanks = null;

    private static Vector<Node> nodes = new Vector<>();

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static String getRecordFile() {
        return RECORD_FILE;
    }

    public static Vector<Node> getNodesAndEnemyTankRec() throws IOException {
        br = new BufferedReader(new FileReader(RECORD_FILE));
        allEnemyTankNum = Integer.parseInt(br.readLine());
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] xyd = line.split(" ");
            Node node = new Node(Integer.parseInt(xyd[0]),
                    Integer.parseInt(xyd[1]),
                    Integer.parseInt(xyd[2]));
            nodes.add(node);
        }
        if (br != null) {
            br.close();
        }
        return nodes;
    }

    public static void keepRecord() throws IOException {
        bw = new BufferedWriter(new FileWriter(RECORD_FILE));
        bw.write(allEnemyTankNum + "\r\n");
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.isLive) {
                //保存该坦克的信息
                String record = enemyTank.getX() + " " + enemyTank.getY() + " " + enemyTank.getDirect();
                bw.write(record + "\r\n");
            }
        }
        if (bw != null) {
            bw.close();
        }
    }

    public static int getAllEnemyTank() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTank(int allEnemyTank) {
        Recorder.allEnemyTankNum = allEnemyTank;
    }

    public static void addAllEnemyTank() {
        Recorder.allEnemyTankNum++;
    }
}
