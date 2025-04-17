package Main;

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
        gp.obj[mapNum][0] = new OBJ_Key();
        gp.obj[mapNum][0].worldX = 13* gp.tileSize;       //nhin o map vi tri dat key
        gp.obj[mapNum][0].worldY = 2* gp.tileSize;

        gp.obj[mapNum][1] = new OBJ_Boots();
        gp.obj[mapNum][1].worldX = 4* gp.tileSize;       //nhin o map vi tri dat boot
        gp.obj[mapNum][1].worldY= 6* gp.tileSize;

        gp.obj[mapNum][2] = new OBJ_Door();
        gp.obj[mapNum][2].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][2].worldY= 2* gp.tileSize;

        gp.obj[mapNum][3] = new OBJ_Chest();
        gp.obj[mapNum][3].worldX = 6* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[mapNum][3].worldY= 9* gp.tileSize;
    }
}
