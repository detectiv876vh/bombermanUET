package monster;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;
import java.util.Random;

public class AI {
    private final Entity entity;
    private final gamePanel gp;
    private final Random rand = new Random();

    // Các hằng số AI
    private final int VISION_RANGE = 5 * 48;
    private final int BOMB_ESCAPE_RANGE = 3 * 48;
    private final int ESCAPE_DURATION = 120;

    // Trạng thái AI
    private boolean isEscapingBomb = false;
    private int escapeTargetX, escapeTargetY;
    private int escapeCounter = 0;
    private int moveCounter = 0;

    public AI(Entity entity, gamePanel gp) {
        this.entity = entity;
        this.gp = gp;
    }

    public void updateAI() {
        if (entity.moving) return;

        if (isEscapingBomb) {
            handleBombEscaping();
            return;
        }

        Bomb bomb = findNearbyBomb();
        if (bomb != null) {
            startEscaping(bomb);
            return;
        }

        if (isPlayerInRange()) {
            chasePlayer();
        } else {
            randomMovement();
        }
    }

    private void handleBombEscaping() {
        int distance = Math.abs(entity.worldX - escapeTargetX) + Math.abs(entity.worldY - escapeTargetY);
        if (distance > BOMB_ESCAPE_RANGE * 2 || escapeCounter++ > ESCAPE_DURATION) {
            isEscapingBomb = false;
            escapeCounter = 0;
            return;
        }

        if (moveCounter++ > 30) {
            entity.direction = calculateEscapeDirection();
            checkCollisionAndMove();
            moveCounter = 0;
        }
    }

    private String calculateEscapeDirection() {
        int dx = entity.worldX - escapeTargetX;
        int dy = entity.worldY - escapeTargetY;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "right" : "left";
        }
        return dy > 0 ? "down" : "up";
    }

    private void startEscaping(Bomb bomb) {
        isEscapingBomb = true;
        escapeTargetX = bomb.worldX;
        escapeTargetY = bomb.worldY;
        escapeCounter = 0;
        entity.direction = calculateEscapeDirection();
        checkCollisionAndMove();
    }

    private Bomb findNearbyBomb() {
        for (Entity entity : gp.projectileList[gp.currentMap]) {
            if (entity instanceof Bomb) {
                int distance = Math.abs(this.entity.worldX - entity.worldX)
                        + Math.abs(this.entity.worldY - entity.worldY);
                if (distance <= BOMB_ESCAPE_RANGE) {
                    return (Bomb) entity;
                }
            }
        }
        return null;
    }

    private boolean isPlayerInRange() {
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;
        int distance = Math.abs(entity.worldX - playerX)
                + Math.abs(entity.worldY - playerY);
        return distance <= VISION_RANGE;
    }

    private void chasePlayer() {
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;

        int dx = Integer.compare(playerX, entity.worldX);
        int dy = Integer.compare(playerY, entity.worldY);

        if (Math.abs(playerX - entity.worldX) > Math.abs(playerY - entity.worldY)) {
            entity.direction = dx > 0 ? "right" : "left";
        } else {
            entity.direction = dy > 0 ? "down" : "up";
        }

        checkCollisionAndMove();
    }

    private void checkCollisionAndMove() {
        entity.collisionOn = false;
        gp.checker.checkTile(entity);

        if (!entity.collisionOn) {
            startMoving();
        } else {
            tryAlternativeDirections();
        }
    }

    private void tryAlternativeDirections() {
        String originalDir = entity.direction;
        for (String dir : new String[]{"up", "down", "left", "right"}) {
            if (!dir.equals(originalDir)) {
                entity.direction = dir;
                entity.collisionOn = false;
                gp.checker.checkTile(entity);
                if (!entity.collisionOn) {
                    startMoving();
                    return;
                }
            }
        }
        entity.direction = originalDir;
    }

    private void startMoving() {
        entity.moving = true;
        entity.pixelCounter = 0;
    }

    private void randomMovement() {
        if (moveCounter++ < 60) return;

        entity.direction = new String[]{"up", "down", "left", "right"}[rand.nextInt(4)];
        checkCollisionAndMove();
        moveCounter = 0;
    }
}