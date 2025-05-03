package object;

import Main.gamePanel;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import manager.DrawManager;

import java.awt.*;
import java.util.ArrayList;

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
    private boolean reversing = false; // Biến để kiểm soát chiều animation


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
        //logic cập nhật hoạt ảnh bomb & explosion
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
        } else {
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

                    // Update animation index in ping-pong style
                    if (!reversing) {
                        gp.drawManager.indexAnimExplosion++;
                        if (gp.drawManager.indexAnimExplosion >= 3) {
                            gp.drawManager.indexAnimExplosion = 3;
                            reversing = true;
                        }
                    } else {
                        gp.drawManager.indexAnimExplosion--;
                        if (gp.drawManager.indexAnimExplosion <= 0) {
                            gp.drawManager.indexAnimExplosion = 0;
                            reversing = false;
                            explosionAnimationComplete = true;
                        }
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

        // Kiểm tra va chạm với player và quái
        checkEntityCollision();

        //kiểm tra
        checkBombChain();
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

            Bomb otherBomb = findBombAt(targetTileX, targetTileY);
            if (otherBomb != null && !otherBomb.exploded) {
                otherBomb.triggerExplosion();
                break; // dừng vụ nổ ngay khi gặp bomb ( tránh phá xuyên bomb khác)
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

    //tìm kiếm xung quanh các
    private Bomb findBombAt(int tileX, int tileY) {
        for (Bomb bomb : gp.bombManager.bombList[gp.currentMap]) {
            if (bomb.bombXpos == tileX && bomb.bombYpos == tileY && !bomb.exploded) {
                return bomb;
            }
        }
        return null;
    }

    private void checkBombChain() {
        // Get all bombs on current map
        ArrayList<Bomb> bombs = gp.bombManager.bombList[gp.currentMap];

        // Check each bomb
        for (Bomb otherBomb : bombs) {
            // Skip self and already exploded bombs
            if (otherBomb == this || otherBomb.exploded) continue;

            // Check if other bomb is within explosion radius
            if (checkExplosionRange(otherBomb)) {
                // Trigger explosion immediately
                otherBomb.triggerExplosion();
            }
        }
    }

    private boolean checkExplosionRange(Bomb otherBomb) {
        // Calculate distance in tiles
        int dx = Math.abs(otherBomb.bombXpos - this.bombXpos);
        int dy = Math.abs(otherBomb.bombYpos - this.bombYpos);

        // Check if bomb is within explosion range (either horizontally or vertically)
        return (dx == 0 && dy <= this.radius) ||  // Same column
                (dy == 0 && dx <= this.radius);    // Same row
    }

    private void checkEntityCollision() {
        // Kiểm tra va chạm với player
        if (isInExplosionRange(gp.player)) {
            gp.player.contactMonster(999); // Sử dụng phương thức sẵn có
        }

        // Kiểm tra va chạm với quái
        for (Entity monster : gp.monster[gp.currentMap]) {
            if (monster != null && monster.alive && isInExplosionRange(monster)) {
                monster.life -= 1; // Giảm 1 máu
                if (monster.life <= 0) {
                    monster.alive = false;
                }
            }
        }
    }

    private boolean isInExplosionRange(Entity entity) {
        // Tính toán khoảng cách giữa bomb và entity
        int entityTileX = (entity.worldX + entity.solidArea.x + entity.solidArea.width/2) / gp.tileSize;
        int entityTileY = (entity.worldY + entity.solidArea.y + entity.solidArea.height/2) / gp.tileSize;

        // Kiểm tra theo 4 hướng
        boolean inHorizontalRange = (entityTileY == bombYpos) &&
                (Math.abs(entityTileX - bombXpos) <= radius);
        boolean inVerticalRange = (entityTileX == bombXpos) &&
                (Math.abs(entityTileY - bombYpos) <= radius);

        return inHorizontalRange || inVerticalRange;
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.drawManager.drawBomb(g2, this);
    }
}
