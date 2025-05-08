package Main;

import entity.Entity;

//import monster.MON_Boss;
import monster.MON_1;
import monster.MON_Boss;
import monster.MON_Greenslime;
import object.*;

public class AssetSetter {

    gamePanel gp;

    public AssetSetter(gamePanel gp) {
        this.gp = gp;
    }

    public void setDoor(int mapIndex) {
        gp.obj[mapIndex][0] = new OBJ_Door(gp);

        switch (mapIndex) {
            case 0:
                gp.obj[mapIndex][0].worldX = 28 * gp.tileSize;
                gp.obj[mapIndex][0].worldY = 23 * gp.tileSize;
                break;
            case 1:
                gp.obj[mapIndex][0].worldX = 28 * gp.tileSize;
                gp.obj[mapIndex][0].worldY = 23 * gp.tileSize;
                break;
            case 2:
                gp.obj[mapIndex][0].worldX = 15 * gp.tileSize;
                gp.obj[mapIndex][0].worldY = 15 * gp.tileSize;
                break;
            default:
                System.out.println("Không có cửa cho mapIndex: " + mapIndex);
                break;
        }
    }

    public void setObject() {

        int mapNum = 0;

        gp.obj[mapNum][0] = new OBJ_Invisible(gp);
        gp.obj[mapNum][0].worldX = 2 * gp.tileSize;
        gp.obj[mapNum][0].worldY = 3 * gp.tileSize;

    }
    public void setMonster00() {
        int mapNum = 0;

        // MON_Greenslime và MON_1 được phân bổ đều cho map 0
        gp.monster[mapNum][0] = new MON_Greenslime(gp);
        gp.monster[mapNum][0].worldX = 7 * gp.tileSize;
        gp.monster[mapNum][0].worldY = 14 * gp.tileSize;

//        gp.monster[mapNum][1] = new MON_Greenslime(gp);
//        gp.monster[mapNum][1].worldX = 18 * gp.tileSize;
//        gp.monster[mapNum][1].worldY = 9 * gp.tileSize;
//
//        gp.monster[mapNum][2] = new MON_1(gp);
//        gp.monster[mapNum][2].worldX = 15 * gp.tileSize;
//        gp.monster[mapNum][2].worldY = 7 * gp.tileSize;
//
//        gp.monster[mapNum][3] = new MON_Greenslime(gp);
//        gp.monster[mapNum][3].worldX = 5 * gp.tileSize;
//        gp.monster[mapNum][3].worldY = 1 * gp.tileSize;
//
//        gp.monster[mapNum][4] = new MON_Greenslime(gp);
//        gp.monster[mapNum][4].worldX = 10 * gp.tileSize;
//        gp.monster[mapNum][4].worldY = 18 * gp.tileSize;
//
//        gp.monster[mapNum][5] = new MON_1(gp);
//        gp.monster[mapNum][5].worldX = 25 * gp.tileSize;
//        gp.monster[mapNum][5].worldY = 22 * gp.tileSize;
//
//        gp.monster[mapNum][6] = new MON_Greenslime(gp);
//        gp.monster[mapNum][6].worldX = 8 * gp.tileSize;
//        gp.monster[mapNum][6].worldY = 5 * gp.tileSize;
//
//        gp.monster[mapNum][7] = new MON_Greenslime(gp);
//        gp.monster[mapNum][7].worldX = 22 * gp.tileSize;
//        gp.monster[mapNum][7].worldY = 3 * gp.tileSize;
//
//        gp.monster[mapNum][8] = new MON_1(gp);
//        gp.monster[mapNum][8].worldX = 12 * gp.tileSize;
//        gp.monster[mapNum][8].worldY = 10 * gp.tileSize;
//
//        gp.monster[mapNum][9] = new MON_Greenslime(gp);
//        gp.monster[mapNum][9].worldX = 3 * gp.tileSize;
//        gp.monster[mapNum][9].worldY = 15 * gp.tileSize;
//
//        gp.monster[mapNum][10] = new MON_Greenslime(gp);
//        gp.monster[mapNum][10].worldX = 19 * gp.tileSize;
//        gp.monster[mapNum][10].worldY = 4 * gp.tileSize;
//
//        gp.monster[mapNum][11] = new MON_1(gp);
//        gp.monster[mapNum][11].worldX = 14 * gp.tileSize;
//        gp.monster[mapNum][11].worldY = 20 * gp.tileSize;
//
//        gp.monster[mapNum][12] = new MON_Greenslime(gp);
//        gp.monster[mapNum][12].worldX = 6 * gp.tileSize;
//        gp.monster[mapNum][12].worldY = 12 * gp.tileSize;
//
//        gp.monster[mapNum][13] = new MON_Greenslime(gp);
//        gp.monster[mapNum][13].worldX = 24 * gp.tileSize;
//        gp.monster[mapNum][13].worldY = 8 * gp.tileSize;
//
//        gp.monster[mapNum][14] = new MON_1(gp);
//        gp.monster[mapNum][14].worldX = 9 * gp.tileSize;
//        gp.monster[mapNum][14].worldY = 17 * gp.tileSize;
//
//        gp.monster[mapNum][15] = new MON_Greenslime(gp);
//        gp.monster[mapNum][15].worldX = 16 * gp.tileSize;
//        gp.monster[mapNum][15].worldY = 6 * gp.tileSize;
//
//        gp.monster[mapNum][16] = new MON_Greenslime(gp);
//        gp.monster[mapNum][16].worldX = 27 * gp.tileSize;
//        gp.monster[mapNum][16].worldY = 2 * gp.tileSize;
//
//        gp.monster[mapNum][17] = new MON_1(gp);
//        gp.monster[mapNum][17].worldX = 11 * gp.tileSize;
//        gp.monster[mapNum][17].worldY = 19 * gp.tileSize;
//
//        gp.monster[mapNum][18] = new MON_Greenslime(gp);
//        gp.monster[mapNum][18].worldX = 4 * gp.tileSize;
//        gp.monster[mapNum][18].worldY = 13 * gp.tileSize;
//
//        gp.monster[mapNum][19] = new MON_Greenslime(gp);
//        gp.monster[mapNum][19].worldX = 20 * gp.tileSize;
//        gp.monster[mapNum][19].worldY = 11 * gp.tileSize;
    }
    public void setMonster01() {
        int mapNum = 1;

        gp.monster[mapNum][0] = new MON_Greenslime(gp);
        gp.monster[mapNum][0].worldX = 7 * gp.tileSize;
        gp.monster[mapNum][0].worldY = 14 * gp.tileSize;

        gp.monster[mapNum][1] = new MON_Greenslime(gp);
        gp.monster[mapNum][1].worldX = 18 * gp.tileSize;
        gp.monster[mapNum][1].worldY = 9 * gp.tileSize;

        gp.monster[mapNum][2] = new MON_1(gp);
        gp.monster[mapNum][2].worldX = 15 * gp.tileSize;
        gp.monster[mapNum][2].worldY = 7 * gp.tileSize;

        gp.monster[mapNum][3] = new MON_Greenslime(gp);
        gp.monster[mapNum][3].worldX = 5 * gp.tileSize;
        gp.monster[mapNum][3].worldY = 1 * gp.tileSize;

        // Đổi ô số 4 từ Greenslime sang MON_1
        gp.monster[mapNum][4] = new MON_1(gp); // Thay đổi ở đây
        gp.monster[mapNum][4].worldX = 10 * gp.tileSize;
        gp.monster[mapNum][4].worldY = 18 * gp.tileSize;

        gp.monster[mapNum][5] = new MON_1(gp);
        gp.monster[mapNum][5].worldX = 25 * gp.tileSize;
        gp.monster[mapNum][5].worldY = 22 * gp.tileSize;

        gp.monster[mapNum][6] = new MON_Greenslime(gp);
        gp.monster[mapNum][6].worldX = 8 * gp.tileSize;
        gp.monster[mapNum][6].worldY = 5 * gp.tileSize;

        gp.monster[mapNum][7] = new MON_Greenslime(gp);
        gp.monster[mapNum][7].worldX = 22 * gp.tileSize;
        gp.monster[mapNum][7].worldY = 3 * gp.tileSize;

        gp.monster[mapNum][8] = new MON_1(gp);
        gp.monster[mapNum][8].worldX = 12 * gp.tileSize;
        gp.monster[mapNum][8].worldY = 10 * gp.tileSize;

        gp.monster[mapNum][9] = new MON_Greenslime(gp);
        gp.monster[mapNum][9].worldX = 3 * gp.tileSize;
        gp.monster[mapNum][9].worldY = 15 * gp.tileSize;

        gp.monster[mapNum][10] = new MON_Greenslime(gp);
        gp.monster[mapNum][10].worldX = 19 * gp.tileSize;
        gp.monster[mapNum][10].worldY = 4 * gp.tileSize;

        gp.monster[mapNum][11] = new MON_1(gp);
        gp.monster[mapNum][11].worldX = 14 * gp.tileSize;
        gp.monster[mapNum][11].worldY = 20 * gp.tileSize;

        gp.monster[mapNum][12] = new MON_Greenslime(gp);
        gp.monster[mapNum][12].worldX = 6 * gp.tileSize;
        gp.monster[mapNum][12].worldY = 12 * gp.tileSize;

        gp.monster[mapNum][13] = new MON_Greenslime(gp);
        gp.monster[mapNum][13].worldX = 24 * gp.tileSize;
        gp.monster[mapNum][13].worldY = 8 * gp.tileSize;

        gp.monster[mapNum][14] = new MON_1(gp);
        gp.monster[mapNum][14].worldX = 9 * gp.tileSize;
        gp.monster[mapNum][14].worldY = 17 * gp.tileSize;

        gp.monster[mapNum][15] = new MON_Greenslime(gp);
        gp.monster[mapNum][15].worldX = 16 * gp.tileSize;
        gp.monster[mapNum][15].worldY = 6 * gp.tileSize;

        gp.monster[mapNum][16] = new MON_Greenslime(gp);
        gp.monster[mapNum][16].worldX = 27 * gp.tileSize;
        gp.monster[mapNum][16].worldY = 2 * gp.tileSize;

        gp.monster[mapNum][17] = new MON_1(gp);
        gp.monster[mapNum][17].worldX = 11 * gp.tileSize;
        gp.monster[mapNum][17].worldY = 19 * gp.tileSize;

        gp.monster[mapNum][18] = new MON_Greenslime(gp);
        gp.monster[mapNum][18].worldX = 4 * gp.tileSize;
        gp.monster[mapNum][18].worldY = 13 * gp.tileSize;

        gp.monster[mapNum][19] = new MON_Greenslime(gp);
        gp.monster[mapNum][19].worldX = 20 * gp.tileSize;
        gp.monster[mapNum][19].worldY = 11 * gp.tileSize;
    }
    public void setBoss() {
        gp.monster[2][0] = new MON_Boss(gp);
        gp.monster[2][0].worldX = gp.tileSize*4;
        gp.monster[2][0].worldY = 17* gp.tileSize;
    }
}
