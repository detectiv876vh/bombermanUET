package object;

import Main.gamePanel;
import entity.Projectile;

public class Bomb extends Projectile {

    public Bomb(gamePanel gp) {
        super(gp);
        name = "Bomb";
        maxLife = 120;
        life = maxLife;
        alive = true;
        getImage();
    }

    public void getImage() {
        down1 = setup("/objects/bomb1");
        down2 = setup("/objects/bomb2");
    }
}
