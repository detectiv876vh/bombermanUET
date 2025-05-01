package Main;

import entity.NPC_Test;
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
    }

    public void setNPC() {
        int mapNum = 0;
        gp.npc[mapNum][0] = new NPC_Test(gp);
        gp.npc[mapNum][0].worldX = gp.tileSize*3;
        gp.npc[mapNum][0].worldY = 16* gp.tileSize;
    }

    public void setMonster() {
        int mapNum = 0;
        gp.monster[mapNum][0] = new MON_Greenslime(gp);
        gp.monster[mapNum][0].worldX = 2* gp.tileSize;
        gp.monster[mapNum][0].worldY = 14* gp.tileSize;
    }
}
