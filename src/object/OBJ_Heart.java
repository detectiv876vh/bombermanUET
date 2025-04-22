package object;

import Main.UtilityTool;
import Main.gamePanel;
import entity.Entity;

public class OBJ_Heart extends Entity {

    public OBJ_Heart(gamePanel gp) {
        super(gp);
        name = "Heart";

        image = setup("/objects/heartfull");
        image2 = setup("/objects/hearthalf");
        image3 = setup("/objects/heartblank");
        image = UtilityTool.scaleImage(image,gp.tileSize,gp.tileSize);
        image2 = UtilityTool.scaleImage(image2,gp.tileSize,gp.tileSize);
        image3 = UtilityTool.scaleImage(image3,gp.tileSize,gp.tileSize);
    }

}
