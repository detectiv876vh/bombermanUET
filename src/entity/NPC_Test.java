package entity;

import Main.gamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Test extends Entity{

    public NPC_Test(gamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
    }
    public void getImage() {

        up1 = setup("/npc/testsau1");
        up2 = setup("/npc/testsau2");
        up3 = setup("/npc/testsau3");
        down1 = setup("/npc/testtruoc1");
        down2 = setup("/npc/testtruoc2");
        down3 = setup("/npc/testtruoc3");
        left1 = setup("/npc/testtrai1");
        left2 = setup("/npc/testtrai2");
        left3 = setup("/npc/testtrai3");
        right1 = setup("/npc/testphai1");
        right2 = setup("/npc/testphai2");
        right3 = setup("/npc/testphai3");
    }

    public void setAction() {
            actionLockCounter++;
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            // BẢN SAU UPDATE DO DŨNGGGG
        if (actionLockCounter == 60) {
            String[] directions = {"up", "down", "left", "right"};
            Random random = new Random();

            for (int i = 0; i < 4; i++) {
                String tryDir = directions[random.nextInt(4)];
                direction = tryDir;

                collisionOn = false;
                gp.checker.checkTile(this);
                gp.checker.checkPlayer(this);

                if (!collisionOn) {
                    break; // nếu không va chạm thì dùng hướng này
                }
            }

            actionLockCounter = 0;
        }
    }
}
