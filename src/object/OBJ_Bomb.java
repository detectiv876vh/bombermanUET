package object;

import Main.gamePanel;
import entity.Entity;
import java.awt.image.BufferedImage;

public class OBJ_Bomb extends Entity {

    gamePanel gp;
    private BufferedImage image1, image2;
    private int spriteCounter = 0;
    private int spriteNum = 1;

    public OBJ_Bomb(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "bombItem";
        down1 = setup("/objects/Bomb");
        stackable = true;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}