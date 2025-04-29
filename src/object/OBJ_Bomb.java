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
        image1 = setup("/objects/bomb1");
        image2 = setup("/objects/bomb2");
        down1 = image1;
        stackable = true;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void update() {
        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
                down1 = image2;
            } else {
                spriteNum = 1;
                down1 = image1;
            }
            spriteCounter = 0;
        }
    }
}