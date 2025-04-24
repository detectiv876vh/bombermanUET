package Main;

import entity.NPC_Test;
import monster.MON_Greenslime;
import object.OBJ_Boots;
import object.OBJ_Key;
import object.OBJ_Door;
import object.OBJ_Chest;

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

        gp.obj[mapNum][1] = new OBJ_Boots(gp);
        gp.obj[mapNum][1].worldX = 4* gp.tileSize;       //nhin o map vi tri dat boot
        gp.obj[mapNum][1].worldY= 6* gp.tileSize;

        gp.obj[mapNum][2] = new OBJ_Door(gp);
        gp.obj[mapNum][2].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][2].worldY= 2* gp.tileSize;

        gp.obj[mapNum][3] = new OBJ_Chest(gp);
        gp.obj[mapNum][3].worldX = 6* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][3].worldY= 9* gp.tileSize;
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
}
