package object;

import Main.gamePanel;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class OBJ_Key extends Entity {

    public OBJ_Key(gamePanel gp) {
        super(gp);
        name = "Key";
        down1 = setup("/objects/key");
    }
}