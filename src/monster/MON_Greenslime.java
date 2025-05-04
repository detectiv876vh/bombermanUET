package monster;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;

public class MON_Greenslime extends Entity {
    private final AI ai;

    public MON_Greenslime(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = 2;
        name = "GreenSlime";
        speed = 2;
        maxLife = 3;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();

        // Khởi tạo AI
        ai = new AI(this, gp);
    }

    public void getImage() {
        up1 = setup("/monster/5");
        up2 = setup("/monster/9");
        down1 = setup("/monster/9");
        down2 = setup("/monster/13");
        left1 = setup("/monster/left1");
        left2 = setup("/monster/left2");
        right1 = setup("/monster/right1");
        right2 = setup("/monster/right2");
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