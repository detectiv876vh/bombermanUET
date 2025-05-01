package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Heart extends Entity {

    public OBJ_Heart(gamePanel gp) {
        super(gp);
        name = "Heart";

        image = setup("/objects/heartfull");
        image2 = setup("/objects/hearthalf");
        image3 = setup("/objects/heartblank");

    }
}
