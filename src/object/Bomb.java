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
    public int intervalToExplode = 180; // Số lần hoạt ảnh trước khi nổ
    public int frameCounter = 0;
    public int explosionLife = 20; // thời gian tồn tại lửa sau khi nổ
    public int radius = 3; // Range mặc định (1 ô)
    int frameExplosion = 0;
    int intervalExplosion = 5;
    public boolean explosionAnimationComplete = false;
    public int currentFrame = 0;
    private int animationCounter = 0;
    public int animationSpeed = 15; // Thay đổi giá trị này để điều chỉnh tốc độ animation
    public Rectangle collisionArea; // Vùng va chạm thực tế của bomb


    public Bomb(gamePanel gp, DrawManager drawManager) {
        super(gp);
        this.speed = 0; // Bomb không di chuyển
        this.name = "Bomb";
        this.alive = true;
        // Vùng va chạm nhỏ hơn kích thước bomb để dễ đi qua
        this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        this.solidAreaDefauftX = solidArea.x;
        this.solidAreaDefauftY = solidArea.y;

        this.drawManager = drawManager; // Gán vào
        if (this.drawManager != null) {
            getImage();
        }
    }

    public void getImage() {
        image = drawManager.bombAnim[0]; // ảnh bomb
        image2 = drawManager.bombAnim[1]; // ảnh bomb cho animation
        image3 = drawManager.bombAnim[2];
    }

    @Override
    public void update() {
        if (!exploded) {
            // Cập nhật animation bomb
            animationCounter++;
            if (animationCounter > animationSpeed) {
                currentFrame = (currentFrame + 1) % 3; // Quay lại 0 sau khi đạt 2
                animationCounter = 0;
            }

            countToExplode++;
            if (countToExplode >= intervalToExplode) {
                exploded = true;
                triggerExplosion();
            }
        }
        else {
            life--;
            if (life <= 0) {
                alive = false;
                collisionArea = new Rectangle(0, 0, 0, 0);
                gp.drawManager.indexAnimExplosion = 0; // Reset về 0 khi lửa tắt

            }

            if (!explosionAnimationComplete) {
                frameExplosion++;
                if (frameExplosion >= intervalExplosion) {
                    frameExplosion = 0;
                    gp.drawManager.indexAnimExplosion++;

                    // Nếu đã chạy hết animation
                    if (gp.drawManager.indexAnimExplosion >= 4) {
                        explosionAnimationComplete = true; // Dừng animation
                        gp.drawManager.indexAnimExplosion = 3; // Giữ ở frame cuối
                    }
                }
            }
        }
    }

    public void triggerExplosion() {
        exploded = true;
        gp.playSE(5);

        // Phá tường tại vị trí bomb (tâm)
        gp.tileM.explodeTile(bombXpos, bombYpos);

        // Phá tường 4 hướng xung quanh với kiểm tra va chạm
        explodeInDirection(1, 0);  // Right
        explodeInDirection(-1, 0); // Left
        explodeInDirection(0, -1); // Up
        explodeInDirection(0, 1);  // Down
    }

    private void explodeInDirection(int deltaX, int deltaY) {
        for (int i = 1; i <= radius; i++) {
            int targetTileX = bombXpos + deltaX * i;
            int targetTileY = bombYpos + deltaY * i;

            // Kiểm tra biên
            if (targetTileX < 0 || targetTileX >= gp.maxWorldCol ||
                    targetTileY < 0 || targetTileY >= gp.maxWorldRow) {
                break;
            }

            // Kiểm tra va chạm
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][targetTileX][targetTileY];
            boolean isCollision = gp.tileM.tile[tileNum].collision;
            boolean isBreakable = gp.tileM.tile[tileNum].breakable;

            // Phá tường nếu có thể
            gp.tileM.explodeTile(targetTileX, targetTileY);

            // Dừng nếu gặp vật cản không phá được
            if (isCollision && !isBreakable) {
                break;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.drawManager.drawBomb(g2, this);
    }
}
