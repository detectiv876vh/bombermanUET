package object;

import Main.gamePanel;
import entity.Projectile;
import manager.DrawManager;

import java.awt.*;

public class Bomb extends Projectile {

    public boolean exploded = false;
    DrawManager drawManager;
    public int bombXpos, bombYpos; // Tọa độ bomb theo tile
    public int[][][] mapTileNum;   // Reference đến tile map
    public int countToExplode = 0;
    public int intervalToExplode = 3; // Số lần hoạt ảnh trước khi nổ
    public int frameCounter = 0;
    public int explosionLife = 60; // thời gian tồn tại lửa sau khi nổ
    public int radius = 1; // Range mặc định (1 ô)
    int frameExplosion = 0;
    int intervalExplosion = 3;

    public Bomb(gamePanel gp, DrawManager drawManager) {
        super(gp);
        this.speed = 0; // Bomb không di chuyển
        this.name = "Bomb";
        this.alive = true;

        this.drawManager = drawManager; // Gán vào
        if (this.drawManager != null) {
            getImage();
        }
        maxLife = explosionLife; // thời gian sống sau khi nổ
    }

    public void getImage() {
        image = drawManager.bombAnim[0]; // ảnh bomb
        image2 = drawManager.bombAnim[1]; // ảnh bomb cho animation
    }

    @Override
    public void update() {
        if (!exploded) {
            frameCounter++;
            if (frameCounter >= gp.drawManager.intervalBomb) {
                frameCounter = 0;
                gp.drawManager.indexAnimBomb++;
                if (gp.drawManager.indexAnimBomb > 2) {
                    gp.drawManager.indexAnimBomb = 0;
                    countToExplode++;
                }
            }

            if (countToExplode >= intervalToExplode) {
                exploded = true;
                triggerExplosion();
            }
        }
        else {
            life--;
            if (life <= 0) {
                alive = false;
            }

            frameExplosion++;
            if (frameExplosion >= gp.drawManager.intervalExplosion) {
                frameExplosion = 0;
                gp.drawManager.indexAnimExplosion++;
                if (gp.drawManager.indexAnimExplosion >= 4) {
                    gp.drawManager.indexAnimExplosion = 0;
                }
            }
        }
    }

    public void triggerExplosion() {
        exploded = true;
        gp.playSE(5);

        // Phá tường tại vị trí bomb (tâm)
        gp.tileM.explodeTile(bombXpos, bombYpos);

        // Phá tường 4 hướng xung quanh
        for (int i = 1; i <= radius; i++) {
            // Right
            gp.tileM.explodeTile(bombXpos + i, bombYpos);
            // Left
            gp.tileM.explodeTile(bombXpos - i, bombYpos);
            // Up
            gp.tileM.explodeTile(bombXpos, bombYpos - i);
            // Down
            gp.tileM.explodeTile(bombXpos, bombYpos + i);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.drawManager.drawBomb(g2, this);
    }
}
