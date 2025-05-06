package object;

import Main.gamePanel;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import manager.DrawManager;

import java.awt.*;
import java.util.ArrayList;

public class Bomb extends Entity {

    public boolean exploded = false;
    DrawManager drawManager;
    public int bombXpos, bombYpos; // Tọa độ bomb theo tile
    public int[][][] mapTileNum;   // Reference đến tile map
    public int countToExplode = 0;
    public int intervalToExplode = 180; // Số lần hoạt ảnh trước khi nổ
    public int radius = 1; // Range mặc định (1 ô)
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

        // Gây sát thương lên monster tại ô này
        checkMonsterHit(bombXpos, bombYpos);

        // Phá tường 4 hướng xung quanh với kiểm tra va chạm
        explodeInDirection(1, 0);  // Right
        explodeInDirection(-1, 0); // Left
        explodeInDirection(0, -1); // Up
        explodeInDirection(0, 1);  // Down

        // Kiểm tra va chạm với player và quái


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

            // Gây sát thương lên monster tại ô này
            checkMonsterHit(targetTileX, targetTileY);

            // Dừng nếu gặp vật cản không phá được
            if (isCollision /*&& isBreakable*/) {
                break;
            }
        }
    }

    //tìm kiếm xung quanh có bomb không
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
        int dx = otherBomb.bombXpos - this.bombXpos;
        int dy = otherBomb.bombYpos - this.bombYpos;

        // Không cùng hàng hoặc cột => loại
        if (dx != 0 && dy != 0) return false;

        int stepX = Integer.compare(dx, 0); // -1, 0, 1
        int stepY = Integer.compare(dy, 0); // -1, 0, 1

        int distance = Math.max(Math.abs(dx), Math.abs(dy));

        // Vượt quá tầm nổ => loại
        if (distance > this.radius) return false;

        // Kiểm tra từng ô giữa hai bomb
        for (int i = 1; i <= distance; i++) {
            int checkX = this.bombXpos + stepX * i;
            int checkY = this.bombYpos + stepY * i;

            int tileNum = gp.tileM.mapTileNum[gp.currentMap][checkX][checkY];
            boolean blocked = gp.tileM.tile[tileNum].collision;


            // Nếu gặp vật cản thì không nối được
            if (blocked) return false;

            // Nếu đến vị trí bomb kia => hợp lệ
            if (checkX == otherBomb.bombXpos && checkY == otherBomb.bombYpos) {
                return true;
            }
        }

        return false;
    }

    private void checkMonsterHit(int tileX, int tileY) {
        for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
            Entity monster = gp.monster[gp.currentMap][i];
            if (monster != null && monster.alive) {
                int monsterTileX = monster.worldX / gp.tileSize;
                int monsterTileY = monster.worldY / gp.tileSize;

                if (monsterTileX == tileX && monsterTileY == tileY) {
                    monster.alive = false;
                    gp.playSE(6); // Âm thanh quái chết nếu có
                }
            }
        }
    }



    @Override
    public void draw(Graphics2D g2) {
        gp.drawManager.drawBomb(g2, this);
    }
}
