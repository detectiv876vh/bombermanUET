package Main;

import monster.MON_Boss;
import monster.MON_Greenslime;
import object.*;

public class AssetSetter {

    gamePanel gp;

    public AssetSetter(gamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        int mapNum = 0;
        gp.obj[mapNum][0] = new OBJ_Key(gp);
        gp.obj[mapNum][0].worldX = 13* gp.tileSize;       //nhin o map vi tri dat key
        gp.obj[mapNum][0].worldY = 2* gp.tileSize;

        gp.obj[mapNum][2] = new OBJ_Door(gp);
        gp.obj[mapNum][2].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][2].worldY= 2* gp.tileSize;

        gp.obj[mapNum][1] = new OBJ_Invisible(gp);
        gp.obj[mapNum][1].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][1].worldY= 1* gp.tileSize;

        gp.obj[1][3] = new OBJ_Invisible(gp);
        gp.obj[1][3].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[1][3].worldY= 3* gp.tileSize;

        gp.obj[1][3] = new OBJ_Shield(gp);
        gp.obj[1][3].worldX = 2* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[1][3].worldY= 3* gp.tileSize;

    }
    public void setMonster() {
        int mapNum = 1;
        gp.monster[mapNum][0] = new MON_Greenslime(gp);
        gp.monster[mapNum][0].worldX = 2* gp.tileSize;
        gp.monster[mapNum][0].worldY = 14* gp.tileSize;


        gp.monster[mapNum][2] = new MON_Greenslime(gp);
        gp.monster[mapNum][2].worldX = 4* gp.tileSize;
        gp.monster[mapNum][2].worldY = 14* gp.tileSize;

        gp.monster[mapNum][3] = new MON_Greenslime(gp);
        gp.monster[mapNum][3].worldX = 1* gp.tileSize;
        gp.monster[mapNum][3].worldY = 14* gp.tileSize;


    }

    public void setBoss() {
        int mapNum = 1;
        gp.monster[mapNum][0] = new MON_Boss(gp);
        gp.monster[mapNum][0].worldX = 2* gp.tileSize;
        gp.monster[mapNum][0].worldY = 7* gp.tileSize;
    }
}
