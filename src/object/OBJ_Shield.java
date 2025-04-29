package object;

import Main.gamePanel;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class OBJ_Shield extends Entity {

    public OBJ_Shield(gamePanel gp) {
        super(gp);
        name = "Shield";
        down1 = setup("/objects/shield");
    }
}