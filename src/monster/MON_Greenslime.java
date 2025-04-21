package monster;

import Main.gamePanel;
import entity.Entity;

import java.util.Random;

public class MON_Greenslime extends Entity {

    public MON_Greenslime(gamePanel gp) {
        super(gp);

        type = 2;
        name = "GreenSlime"; // sua o day
        speed = 1;
        maxLife = 20;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefauftX =  solidArea.x;
        solidAreaDefauftY =  solidArea.y;

        getImage();
    }


    public void getImage () {
        up1 = setup("/monster/5");
        up2 = setup("/monster/9");
        down1 = setup("/monster/9");
        down2 = setup("/monster/13");
        left1 = setup("/monster/left1");
        left2 = setup("/monster/left2");
        right1 = setup("/monster/right1");
        right2 = setup("/monster/right2");
    }

    public void setAction () {
        actionLockCounter++;
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

    public void damegeReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }
}
