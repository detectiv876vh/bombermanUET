package monster;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;
import java.util.Random;

public class MON_Greenslime extends Entity {
    // Các hằng số
    private final int VISION_RANGE = 5 * 48;
    private final int BOMB_ESCAPE_RANGE = 3 * 48;
    private final int ESCAPE_DURATION = 120;

    // Trạng thái
    private boolean isEscapingBomb = false;
    private int escapeTargetX, escapeTargetY;
    private int escapeCounter = 0;
    private Random rand = new Random();

    public MON_Greenslime(gamePanel gp) {
        super(gp);
        this.gp = gp;

        // Giữ nguyên phần khởi tạo
        type = 2;
        name = "GreenSlime";
        speed = 2;
        maxLife = 3;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        // Giữ nguyên phần load ảnh
        up1 = setup("/monster/5");
        up2 = setup("/monster/9");
        down1 = setup("/monster/9");
        down2 = setup("/monster/13");
        left1 = setup("/monster/left1");
        left2 = setup("/monster/left2");
        right1 = setup("/monster/right1");
        right2 = setup("/monster/right2");
    }

    public void setAction() {
        if (moving) return;

        // Phần mới: Xử lý trạng thái né bom
        if (isEscapingBomb) {
            handleBombEscaping();
            return;
        }

        // Logic cũ với cải tiến
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
        // Kiểm tra điều kiện dừng né bom
        int distance = Math.abs(worldX - escapeTargetX) + Math.abs(worldY - escapeTargetY);
        if (distance > BOMB_ESCAPE_RANGE * 2 || escapeCounter++ > ESCAPE_DURATION) {
            isEscapingBomb = false;
            escapeCounter = 0;
            return;
        }

        // Cập nhật hướng né bom
        if (moveCounter++ > 30) {
            direction = calculateEscapeDirection();
            checkCollisionAndMove();
            moveCounter = 0;
        }
    }

    private String calculateEscapeDirection() {
        int dx = worldX - escapeTargetX;
        int dy = worldY - escapeTargetY;

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
        direction = calculateEscapeDirection();
        checkCollisionAndMove();
    }

    // Các phương thức giữ nguyên từ code gốc
    private Bomb findNearbyBomb() {
        for (Entity entity : gp.projectileList[gp.currentMap]) {
            if (entity instanceof Bomb) {
                int distance = Math.abs(worldX - entity.worldX) + Math.abs(worldY - entity.worldY);
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
        int distance = Math.abs(worldX - playerX) + Math.abs(worldY - playerY);
        return distance <= VISION_RANGE;
    }

    private void chasePlayer() {
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;

        int dx = Integer.compare(playerX, worldX);
        int dy = Integer.compare(playerY, worldY);

        if (Math.abs(playerX - worldX) > Math.abs(playerY - worldY)) {
            direction = dx > 0 ? "right" : "left";
        } else {
            direction = dy > 0 ? "down" : "up";
        }

        checkCollisionAndMove();
    }

    private void checkCollisionAndMove() {
        collisionOn = false;
        gp.checker.checkTile(this);

        if (!collisionOn) {
            startMoving();
        } else {
            tryAlternativeDirections();
        }
    }

    private void tryAlternativeDirections() {
        String originalDir = direction;
        for (String dir : new String[]{"up", "down", "left", "right"}) {
            if (!dir.equals(originalDir)) {
                direction = dir;
                collisionOn = false;
                gp.checker.checkTile(this);
                if (!collisionOn) {
                    startMoving();
                    return;
                }
            }
        }
        direction = originalDir;
    }

    private void startMoving() {
        moving = true;
        pixelCounter = 0;
    }

    private void randomMovement() {
        if (moveCounter++ < 60) return;

        direction = new String[]{"up", "down", "left", "right"}[rand.nextInt(4)];
        checkCollisionAndMove();
        moveCounter = 0;
    }

    public void update() {
        super.update();

        if (moving) {
            pixelCounter += speed;
            if (pixelCounter >= 48) {
                moving = false;
                pixelCounter = 0;
            }
        }
    }

    public void damageReaction() {
        direction = gp.player.direction;
        hpBarOn = true;
        hpBarCounter = 0;
    }
}