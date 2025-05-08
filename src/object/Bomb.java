package object;

import Main.gamePanel;
import entity.Entity;
import manager.DrawManager;
import monster.MON_Boss;
import java.awt.*;
import java.util.ArrayList;

public class Bomb extends Entity {
    public boolean exploded = false;
    DrawManager drawManager;
    private boolean hasHitBoss = false;
    public int bombXpos, bombYpos;
    public int[][][] mapTileNum;
    public int countToExplode = 0;
    public int intervalToExplode = 180;
    public int radius = 1;
    int frameExplosion = 0;
    int intervalExplosion = 5;
    public boolean explosionAnimationComplete = false;
    public int currentFrame = 0;
    private int animationCounter = 0;
    public int animationSpeed = 15;
    public Rectangle collisionArea;
    private boolean reversing = false;
    public boolean exploding = false;
    public int explosionCounter = 0;
    public final int explosionDuration = 60;

    public Bomb(gamePanel gp, DrawManager drawManager) {
        super(gp);
        this.speed = 0;
        this.name = "Bomb";
        this.alive = true;
        this.hasHitBoss = false;
        this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        this.solidAreaDefauftX = solidArea.x;
        this.solidAreaDefauftY = solidArea.y;
        this.drawManager = drawManager;
        if (this.drawManager != null) {
            getImage();
        }
    }

    public void getImage() {
        image = drawManager.bombAnim[0];
        image2 = drawManager.bombAnim[1];
        image3 = drawManager.bombAnim[2];
    }

    @Override
    public void update() {
        if (!exploded) {
            animationCounter++;
            if (animationCounter > animationSpeed) {
                currentFrame = (currentFrame + 1) % 3;
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
                gp.drawManager.indexAnimExplosion = 0;
            }

            if (!explosionAnimationComplete) {
                frameExplosion++;
                if (frameExplosion >= intervalExplosion) {
                    frameExplosion = 0;
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
        if (exploding) {
            explosionCounter++;
            if (explosionCounter >= explosionDuration) {
                explosionAnimationComplete = true;
            }
        }
    }

    public void triggerExplosion() {
        exploded = true;
        gp.playSE(5);

        gp.tileM.explodeTile(bombXpos, bombYpos);
        checkMonsterHit(bombXpos, bombYpos);
        checkBossHit(bombXpos, bombYpos);

        explodeInDirection(1, 0);  // Right
        explodeInDirection(-1, 0); // Left
        explodeInDirection(0, -1); // Up
        explodeInDirection(0, 1);  // Down

        checkBombChain();
    }

    private void explodeInDirection(int deltaX, int deltaY) {
        for (int i = 1; i <= radius; i++) {
            int targetTileX = bombXpos + deltaX * i;
            int targetTileY = bombYpos + deltaY * i;

            if (targetTileX < 0 || targetTileX >= gp.maxWorldCol ||
                    targetTileY < 0 || targetTileY >= gp.maxWorldRow) {
                break;
            }

            Bomb otherBomb = findBombAt(targetTileX, targetTileY);
            if (otherBomb != null && !otherBomb.exploded) {
                otherBomb.triggerExplosion();
                break;
            }

            int tileNum = gp.tileM.mapTileNum[gp.currentMap][targetTileX][targetTileY];
            boolean isCollision = gp.tileM.tile[tileNum].collision;

            gp.tileM.explodeTile(targetTileX, targetTileY);

            if (!hasHitBoss) {
                checkBossHit(targetTileX, targetTileY);
            }

            checkMonsterHit(targetTileX, targetTileY);

            if (isCollision) {
                break;
            }
        }
    }

    private Bomb findBombAt(int tileX, int tileY) {
        for (Bomb bomb : gp.bombManager.bombList[gp.currentMap]) {
            if (bomb.bombXpos == tileX && bomb.bombYpos == tileY && !bomb.exploded) {
                return bomb;
            }
        }
        return null;
    }

    private void checkBombChain() {
        ArrayList<Bomb> bombs = gp.bombManager.bombList[gp.currentMap];
        for (Bomb otherBomb : bombs) {
            if (otherBomb == this || otherBomb.exploded) continue;
            if (checkExplosionRange(otherBomb)) {
                otherBomb.triggerExplosion();
            }
        }
    }

    private boolean checkExplosionRange(Bomb otherBomb) {
        int dx = otherBomb.bombXpos - this.bombXpos;
        int dy = otherBomb.bombYpos - this.bombYpos;

        if (dx != 0 && dy != 0) return false;

        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);
        int distance = Math.max(Math.abs(dx), Math.abs(dy));

        if (distance > this.radius) return false;

        for (int i = 1; i <= distance; i++) {
            int checkX = this.bombXpos + stepX * i;
            int checkY = this.bombYpos + stepY * i;
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][checkX][checkY];
            boolean blocked = gp.tileM.tile[tileNum].collision;
            if (blocked) return false;
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
                    gp.playSE(6);
                }
            }
        }
    }

    private void checkBossHit(int tileX, int tileY) {
        if (hasHitBoss) return;

        // Tính toán vùng nổ chính xác (center-aligned)
        int explosionSize = gp.tileSize * 3 / 4;
        Rectangle explosionArea = new Rectangle(
                tileX * gp.tileSize + (gp.tileSize - explosionSize)/2,
                tileY * gp.tileSize + (gp.tileSize - explosionSize)/2,
                explosionSize,
                explosionSize
        );

        for (Entity monster : gp.monster[gp.currentMap]) {
            if (monster instanceof MON_Boss && monster.alive) {
                Rectangle bossHitbox = monster.getHitbox();
                if (explosionArea.intersects(bossHitbox)) {
                    ((MON_Boss) monster).takeBombDamage();
                    hasHitBoss = true;
                    System.out.println("Bomb hit boss at (" + tileX + "," + tileY + ")");
                    return;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.drawManager.drawBomb(g2, this);
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(
                worldX + solidArea.x,
                worldY + solidArea.y,
                solidArea.width,
                solidArea.height
        );
    }
}