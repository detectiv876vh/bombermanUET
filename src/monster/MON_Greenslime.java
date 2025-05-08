package monster;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;

import java.awt.*;
import java.util.Random;

public class MON_Greenslime extends Entity {

    public boolean bombDangerDetected = false;
    public int safeMoveCounter = 0;
    public int dangerCheckInterval = 30; // Kiểm tra nguy hiểm mỗi 30 frame

    public MON_Greenslime(gamePanel gp) {
        super(gp);

        this.gp = gp;

        type = 1;
        name = "GreenSlime";
        speed = 2;
        maxLife = 1;
        life = maxLife;

        solidArea = new Rectangle(2,2,44,44);

        getImage();

    }

    public void getImage() {
        up1 = setup("/monster/slime_uprun1");
        up2 = setup("/monster/slime_uprun2");
        up3 = setup("/monster/slime_uprun3");
        up4 = setup("/monster/slime_uprun4");
        up5 = setup("/monster/slime_uprun5");
        up6 = setup("/monster/slime_uprun6");

        down1 = setup("/monster/slime_idle");
        down2 = setup("/monster/slime_frontrun1");
        down3 = setup("/monster/slime_frontrun2");
        down4 = setup("/monster/sliime_frontrun3");
        down5 = setup("/monster/sliime_frontrun4");
        down6 = setup("/monster/sliime_frontrun6");

        left1 = setup("/monster/slime_leftrun1");
        left2 = setup("/monster/slime_leftrun2");
        left3 = setup("/monster/slime_leftrun3");
        left4 = setup("/monster/slime_leftrun4");
        left5 = setup("/monster/slime_leftrun5");
        left6 = setup("/monster/slime_leftrun6");

        right1 = setup("/monster/slime_rightrun1");
        right2 = setup("/monster/slime_rightrun2");
        right3 = setup("/monster/slime_rightrun3");
        right4 = setup("/monster/slime_rightrun4");
        right5 = setup("/monster/slime_rightrun5");
        right6 = setup("/monster/slime_rightrun6");
    }

    @Override
    public void setDyingSprites() {
        dyingSprites[0] = setup("/monster/slimedie1");
        dyingSprites[1] = setup("/monster/slimedie2");
        dyingSprites[2] = setup("/monster/slimedie3");
        dyingSprites[3] = setup("/monster/slimedie4");
    }



    public void setAction() {

        if (bombDangerDetected) {
            return; // Giữ nguyên hướng di chuyển đã được xác định
        }
        if (onPath) {

            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol,goalRow);
        } else {
            actionLockCounter++;
            if (actionLockCounter == 60) {

                Random random = new Random();
                int i = random.nextInt(100) + 1; // chon 1 so ngau nhien

                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }

                actionLockCounter = 0;
            }

        }
    }

    public void update() {
        super.update();

        // Kiểm tra bomb nguy hiểm định kỳ
        if (!dying && !bombDangerDetected && gp.bombManager != null) {
            dangerCheckInterval--;
            if (dangerCheckInterval <= 0) {
                checkBombDanger();
                dangerCheckInterval = 30; // Reset counter
            }
        }

        // Xử lý di chuyển né bomb
        if (bombDangerDetected) {
            safeMoveCounter--;
            if (safeMoveCounter <= 0) {
                bombDangerDetected = false; // Kết thúc trạng thái né bomb
                onPath = false; // Quay lại hành vi bình thường
            }
        }

        if (moving) {
            pixelCounter += speed;
            if (pixelCounter >= 48) {
                moving = false;
                pixelCounter = 0;
            }
        }

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        if (onPath == false && tileDistance < 5) {
            int i = new Random().nextInt(100) + 1;

            if (i >= 50) {
                onPath = true;
            }
        }
    }

    public void damageReaction() {
        dying = true;
        dyingCounter = 0;
        collisionOn = true; // Tắt va chạm khi đang chết
        invincible = true; // Không nhận sát thương thêm
        direction = gp.player.direction;
        hpBarOn = true;
        hpBarCounter = 0;
    }

    private void checkBombDanger() {
        // Chỉ kiểm tra nếu không đang chết
        if (dying) return;

        // Tính toán vị trí tile của quái
        int monsterCol = (worldX + solidArea.x) / gp.tileSize;
        int monsterRow = (worldY + solidArea.y) / gp.tileSize;

        // Kiểm tra tất cả bomb trên map hiện tại
        for (Bomb bomb : gp.bombManager.bombList[gp.currentMap]) {
            if (bomb != null && !bomb.exploded) {
                // Tính khoảng cách đến bomb
                int bombCol = bomb.bombXpos;
                int bombRow = bomb.bombYpos;

                int colDistance = Math.abs(monsterCol - bombCol);
                int rowDistance = Math.abs(monsterRow - bombRow);

                // Nếu bomb nằm trong phạm vi nguy hiểm (bán kính + 1)
                if ((colDistance <= (bomb.radius + 1) && rowDistance == 0) ||
                        (rowDistance <= (bomb.radius + 1) && colDistance == 0)) {

                    // Kích hoạt trạng thái né bomb
                    bombDangerDetected = true;
                    safeMoveCounter = 120; // Di chuyển an toàn trong 120 frame
                    onPath = false; // Tắt AI truy đuổi

                    // Xác định hướng an toàn để di chuyển
                    determineSafeDirection(bombCol, bombRow);
                    return;
                }
            }
        }
    }
    private void determineSafeDirection(int bombCol, int bombRow) {
        // Tính toán vị trí tile của quái
        int monsterCol = (worldX + solidArea.x) / gp.tileSize;
        int monsterRow = (worldY + solidArea.y) / gp.tileSize;

        // Ưu tiên di chuyển vuông góc với hướng bomb
        if (Math.abs(monsterCol - bombCol) > Math.abs(monsterRow - bombRow)) {
            // Bomb ở cùng hàng, di chuyển lên/xuống
            if (canMoveTo(monsterCol, monsterRow - 1)) {
                direction = "up";
            } else if (canMoveTo(monsterCol, monsterRow + 1)) {
                direction = "down";
            }
        } else {
            // Bomb ở cùng cột, di chuyển trái/phải
            if (canMoveTo(monsterCol - 1, monsterRow)) {
                direction = "left";
            } else if (canMoveTo(monsterCol + 1, monsterRow)) {
                direction = "right";
            }
        }

        // Nếu không di chuyển vuông góc được, thử các hướng khác
        if (direction == null || direction.isEmpty()) {
            String[] directions = {"up", "down", "left", "right"};
            for (String dir : directions) {
                int testCol = monsterCol;
                int testRow = monsterRow;

                switch (dir) {
                    case "up": testRow--; break;
                    case "down": testRow++; break;
                    case "left": testCol--; break;
                    case "right": testCol++; break;
                }

                if (canMoveTo(testCol, testRow)) {
                    direction = dir;
                    break;
                }
            }
        }

        // Nếu vẫn không tìm được hướng, đứng yên
        if (direction == null || direction.isEmpty()) {
            direction = "down"; // Hướng mặc định
        }
    }

    private boolean canMoveTo(int col, int row) {
        // Kiểm tra biên
        if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
            return false;
        }

        // Kiểm tra tile va chạm
        int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
        if (gp.tileM.tile[tileNum].collision) {
            return false;
        }

        // Kiểm tra bomb
        for (Bomb bomb : gp.bombManager.bombList[gp.currentMap]) {
            if (bomb != null && bomb.bombXpos == col && bomb.bombYpos == row) {
                return false;
            }
        }

        // Kiểm tra va chạm với quái khác
        for (Entity monster : gp.monster[gp.currentMap]) {
            if (monster != null && monster != this) {
                int mCol = (monster.worldX + monster.solidArea.x) / gp.tileSize;
                int mRow = (monster.worldY + monster.solidArea.y) / gp.tileSize;
                if (mCol == col && mRow == row) {
                    return false;
                }
            }
        }

        return true;
    }
}
