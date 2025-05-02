package entity;

import Main.gamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Test extends Entity {
    int pixelCounter = 0;
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
        // Nếu đang di chuyển mà chưa đủ 48 pixel thì không chọn hướng mới
        if (pixelCounter < gp.tileSize) {
            return;
        }

        // Khi đã đi đủ 1 ô, chọn hướng mới
        String[] directions = {"up", "down", "left", "right"};
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            String tryDir = directions[random.nextInt(4)];
            direction = tryDir;

            collisionOn = false;
            gp.checker.checkTile(this);

            if (!collisionOn) {
                break;
            }
        }

        pixelCounter = 0;
    }

    public void update() {
        setAction();

        collisionOn = false;
        gp.checker.checkTile(this);

        if (gp.checker.checkPlayer(this)) {
            collisionOn = true;
        }


        if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
            pixelCounter += speed;
        } else {
            // Nếu va chạm, chọn hướng khác
            pixelCounter = gp.tileSize; // ép gọi setAction lại
        }

    }

}

