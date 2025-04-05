package object;

import Main.gamePanel;
import entity.Projectile;

public class Fire extends Projectile {

    gamePanel gp;

    public Fire(gamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Fire";
        speed = 10;
        //maxLife change = shorter range.
        maxLife = 8;
        alive = false;
        life = maxLife;
        getImage();
    }

    public void getImage() {
        up1 = setup("/objects/fireball_up_1");
        up2 = setup("/objects/fireball_up_2");
        down1 = setup("/objects/fireball_down_1");
        down2 = setup("/objects/fireball_down_2");
        left1 = setup("/objects/fireball_left_1");
        left2 = setup("/objects/fireball_left_2");
        right1 = setup("/objects/fireball_right_1");
        right2 = setup("/objects/fireball_right_2");
    }

}
