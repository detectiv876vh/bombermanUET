package object;

import Main.gamePanel;
import entity.Player;
import entity.Projectile;

public class Bomb extends Projectile {

    public int lifeFrame = 0;
    public int maxLife = 120; // 2 giây nếu 60 FPS
    public boolean exploded = false;

    public Bomb(gamePanel gp) {
        super(gp);
        name = "Bomb";
        alive = true;
        collision = true;
        getImage();
    }

    public void getImage() {
        down1 = setup("/objects/bomb1");
        down2 = setup("/objects/bomb2");
    }

    @Override
    public void update() {
        // Hoạt ảnh bomb nảy
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        // Đếm thời gian sống
        lifeFrame++;
        if (lifeFrame >= maxLife && !exploded) {
            exploded = true;
            alive = false; // giữ lại 30 frame sau khi nổ để hiển thị hình ảnh
            gp.bombManager.triggerExplosion(worldX, worldY, user);
        }

        // Sau khi nổ, giảm life cho đến khi biến mất
        if (exploded) {
            life--;
            if (life <= 0) {
                alive = false;
            }
        }
    }
}
