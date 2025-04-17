package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Chest extends Entity {

    public OBJ_Chest(gamePanel gp) {
        super(gp);
        name = "Chest";
        down1 = setup("/objects/chest");
    }
}