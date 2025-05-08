package monster;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;

import java.awt.*;
import java.util.Random;

public class MON_Greenslime extends Entity {
    private final AI ai;

    public MON_Greenslime(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = 2;
        name = "GreenSlime";
        speed = 2;
        maxLife = 1;
        life = maxLife;

        solidArea = new Rectangle(0,0,48,48);

        getImage();

        // Khởi tạo AI
        ai = new AI(this, gp);
    }

    public void getImage() {
        up1 = setup("/monster/slime_run_1");
        up2 = setup("/monster/slime_run_2");
        up3 = setup("/monster/slime_run_3");
        up4 = setup("/monster/slime_run_4");
        up5 = setup("/monster/slime_run_5");
        up6 = setup("/monster/slime_run_6");

        down1 = setup("/monster/slime_run_1");
        down2 = setup("/monster/slime_run_2");
        down3 = setup("/monster/slime_run_3");
        down4 = setup("/monster/slime_run_4");
        down5 = setup("/monster/slime_run_5");
        down6 = setup("/monster/slime_run_6");

        left1 = setup("/monster/slime_run_1");
        left2 = setup("/monster/slime_run_2");
        left3 = setup("/monster/slime_run_3");
        left4 = setup("/monster/slime_run_4");
        left5 = setup("/monster/slime_run_5");
        left6 = setup("/monster/slime_run_6");

        right1 = setup("/monster/slime_run_1");
        right2 = setup("/monster/slime_run_2");
        right3 = setup("/monster/slime_run_3");
        right4 = setup("/monster/slime_run_4");
        right5 = setup("/monster/slime_run_5");
        right6 = setup("/monster/slime_run_6");
    }

    public void setAction() {
        ai.updateAI(); // Gọi logic AI
    }

    public void update() {
        super.update();

        if (moving) {
            pixelCounter += speed;
            if (pixelCounter >= 48) {
                moving = false;
                pixelCounter = 0;
            }
        }
    }

    public void damageReaction() {
        direction = gp.player.direction;
        hpBarOn = true;
        hpBarCounter = 0;
    }
}
