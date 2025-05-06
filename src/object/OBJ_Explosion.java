package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Explosion extends Entity {
    public OBJ_Explosion(gamePanel gp) {
        super(gp);
        name = "Explosion+";
        down1 = setup_obj("/objects/Explosion");
    }
}
