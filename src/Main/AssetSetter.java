package Main;

import entity.NPC_Test;
//import monster.MON_Boss;
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
        gp.obj[mapNum][1].worldX = 1* gp.tileSize;
        gp.obj[mapNum][1].worldY = 2* gp.tileSize;
    }
    public void setMonster() {
        int mapNum = 0;
        gp.monster[mapNum][0] = new MON_Greenslime(gp);
        gp.monster[mapNum][0].worldX = 2* gp.tileSize;
        gp.monster[mapNum][0].worldY = 14* gp.tileSize;
    }
//
//    public void setBoss() {
//        gp.monster[0] = new MON_Boss(gp);
//        gp.monster[0].worldX = gp.tileSize*4;
//        gp.monster[0].worldY = 17* gp.tileSize;
//    }
}
