package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_HeartItem extends Entity {

    public OBJ_HeartItem(gamePanel gp) {
        super(gp);
        name = "heartItem";
        down1 = setup_obj("/objects/heal");
    }
}

