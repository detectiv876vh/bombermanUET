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

        gp.obj[0] = new OBJ_Key();
        gp.obj[0].worldX = 13* gp.tileSize;       //nhin o map vi tri dat key
        gp.obj[0].worldY = 2* gp.tileSize;

        gp.obj[1] = new OBJ_Boots();
        gp.obj[1].worldX = 4* gp.tileSize;       //nhin o map vi tri dat boot
        gp.obj[1].worldY= 6* gp.tileSize;

        gp.obj[2] = new OBJ_Door();
        gp.obj[2].worldX = 12* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[2].worldY= 2* gp.tileSize;

        gp.obj[3] = new OBJ_Chest();
        gp.obj[3].worldX = 6* gp.tileSize;       //nhin o map vi tri dat door
        gp.obj[3].worldY= 9* gp.tileSize;
    }
}
