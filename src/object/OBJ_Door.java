package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Door extends Entity {

    public OBJ_Door(gamePanel gp) {
        super(gp);
        name = "Door";
        down1 = setup("/objects/door");
        collision = true;
    }
}