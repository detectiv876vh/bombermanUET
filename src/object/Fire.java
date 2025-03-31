package object;

import Main.gamePanel;
import entity.Projectile;

public class Fire extends Projectile {

    gamePanel gp;

    public Fire(gamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "fire";
        speed = 10;
        maxLife = 20;
        alive = false;
        life = maxLife;
    }


}
