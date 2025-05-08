package monster;

import Main.gamePanel;
import entity.Entity;
import java.awt.*;
import java.util.Random;

public class MON_Slime extends Entity {
    public MON_Slime(gamePanel gp) {
        super(gp);

        type = 1;
        name = "Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        damager = 1;

        solidArea = new Rectangle(4, 4, 40, 40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getSlimeImage();
    }

    public void getSlimeImage() {
        up1 = setup("/monster/slime_up1");
        up2 = setup("/monster/slime_up2");
        down1 = setup("/monster/slime_down1");
        down2 = setup("/monster/slime_down2");
        left1 = setup("/monster/slime_left1");
        left2 = setup("/monster/slime_left2");
        right1 = setup("/monster/slime_right1");
        right2 = setup("/monster/slime_right2");
    }

    @Override
    public void setAction() {
        Random random = new Random();
        int i = random.nextInt(100) + 1;

        if(i <= 25) direction = "up";
        else if(i <= 50) direction = "down";
        else if(i <= 75) direction = "left";
        else direction = "right";

        actionLockCounter = 0;
    }

    @Override
    public void update() {
        super.update();

        // Đổi hướng ngẫu nhiên sau mỗi 2 giây
        if(actionLockCounter++ > 120) {
            setAction();
        }
    }
}