package entity;

import Main.gamePanel;

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
        //BẢN TRƯỚC KHI UPDATE
//        if (actionLockCounter == 60) {
//            Random random = new Random();
//            int i = random.nextInt(100) + 1; // +1 de no chay tu 1 den 100 thay vi 0 ddeens 99
//
//            if(i <= 25) {
//                direction = "up";
//            }
//            if(i > 25 && i <= 50) {
//                direction = "down";
//            }
//            if(i > 50 && i <= 75) {
//                direction = "right";
//            }
//            if(i > 75 && i <= 100) {
//                direction = "left";
//            }
//        actionLockCounter = 0;
//        }

        // BẢN SAU UPDATE DO DŨNGGGG
        if (actionLockCounter == 60) {
            String[] directions = {"up", "down", "left", "right"};
            Random random = new Random();

            for (int i = 0; i < 4; i++) {
                String tryDir = directions[random.nextInt(4)];
                direction = tryDir;

                // Thử check va chạm tại hướng này
                collisionOn = false;
                gp.checker.checkTile(this);

                if (!collisionOn) {
                    break; // nếu không va chạm thì dùng hướng này
                }
            }

            actionLockCounter = 0;
        }
    }
}
