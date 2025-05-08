package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_BombUP extends Entity {
    public OBJ_BombUP(gamePanel gp) {
        super(gp);
        name = "bombUpgrade_TYPE1";
        down1 = setup("/objects/bombUP");
        collision = true;
    }
}
