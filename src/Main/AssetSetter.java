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

        gp.obj[mapNum][1] = new OBJ_Boost(gp);
        gp.obj[mapNum][1].worldX = 4* gp.tileSize;       //nhin o map vi tri dat boot
        gp.obj[mapNum][1].worldY= 6* gp.tileSize;

        gp.obj[mapNum][2] = new OBJ_Door(gp);
        gp.obj[mapNum][2].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][2].worldY= 2* gp.tileSize;

        gp.obj[mapNum][3] = new OBJ_Chest(gp);
        gp.obj[mapNum][3].worldX = 6* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][3].worldY= 9* gp.tileSize;

        gp.obj[mapNum][4] = new OBJ_Bomb(gp);
        gp.obj[mapNum][4].worldX = 2* gp.tileSize;
        gp.obj[mapNum][4].worldY= 21* gp.tileSize;

        gp.obj[mapNum][5] = new OBJ_Bomb(gp);
        gp.obj[mapNum][5].worldX = 2* gp.tileSize;
        gp.obj[mapNum][5].worldY= 22* gp.tileSize;

        gp.obj[mapNum][6] = new OBJ_Bomb(gp);
        gp.obj[mapNum][6].worldX = 2* gp.tileSize;
        gp.obj[mapNum][6].worldY= 23* gp.tileSize;

        gp.obj[mapNum][7] = new OBJ_Boost(gp);
        gp.obj[mapNum][7].worldX = 4* gp.tileSize;       //nhin o map vi tri dat boot
        gp.obj[mapNum][7].worldY= 7* gp.tileSize;

        gp.obj[mapNum][8] = new OBJ_HeartItem(gp);
        gp.obj[mapNum][8].worldX = 4* gp.tileSize;
        gp.obj[mapNum][8].worldY= 13* gp.tileSize;

        gp.obj[mapNum][9] = new OBJ_Shield(gp);
        gp.obj[mapNum][9].worldX = 2* gp.tileSize;
        gp.obj[mapNum][9].worldY= 13* gp.tileSize;

        gp.obj[mapNum][10] = new OBJ_Invisible(gp);
        gp.obj[mapNum][10].worldX = 7* gp.tileSize;
        gp.obj[mapNum][10].worldY= 13* gp.tileSize;

        gp.obj[mapNum][11] = new OBJ_Invisible(gp);
        gp.obj[mapNum][11].worldX = 4* gp.tileSize;
        gp.obj[mapNum][11].worldY= 33* gp.tileSize;

    }

    public void setNPC() {
        gp.npc[0] = new NPC_Test(gp);
        gp.npc[0].worldX = gp.tileSize*3;
        gp.npc[0].worldY = 16* gp.tileSize;
    }

    public void setMonster() {
        gp.monster[0] = new MON_Greenslime(gp);
        gp.monster[0].worldX = gp.tileSize*3;
        gp.monster[0].worldY = 17* gp.tileSize;
    }
//
//    public void setBoss() {
//        gp.monster[0] = new MON_Boss(gp);
//        gp.monster[0].worldX = gp.tileSize*4;
//        gp.monster[0].worldY = 17* gp.tileSize;
//    }
}
