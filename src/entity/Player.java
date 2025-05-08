package entity;

import Main.KeyHandler;
import Main.gamePanel;
import manager.BombManager;
import object.Bomb;
//import manager.ChemManager;
import object.OBJ_HeartItem;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Random;

public class Player extends Entity {

    KeyHandler kH;
    public int maxBombs = 1;            //bom co ban =1
    public int bombRadius = 1; // radius mặc định ban đầu

    public final int screenX;
    public final int screenY;
    public int hasBomb = maxBombs;
    public int hasKey = 0; // so key co duoc khi nhat tren map
    public int hasBoost = 0;
    public boolean moving = false;
    public int pixelCounter = 0;
    int standCounter = 0;
    public int teleportCooldown = 0;
    public BufferedImage dying1, dying2, dying3, dying4;


    // =========XUYEN =========
    private boolean xuyenMode = false;
    private int xuyenModeMin = 0;
    private final int xuyenModeMax = 300;

    public Player(gamePanel gp, KeyHandler kH) {
        super(gp);
        this.kH = kH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(2,2,44,44);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();

    }

    //vị trí ban đầu của player.
    public void setDefaultValues() {

        worldX = gp.tileSize;
        worldY = gp.tileSize;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6 ;               //sua lai sau khi test game
        life = maxLife - 4;
    }

    //gắn ảnh.
    public void getPlayerImage() {

        up1 = setup("/entities/player_up1");
        up2 = setup("/entities/player_up2");
        up3 = setup("/entities/player_up3");
        up4 = setup("/entities/player_up4");
        up5 = setup("/entities/player_up5");
        up6 = setup("/entities/player_up6");
        down1 = setup("/entities/player_idle");
        down2 = setup("/entities/player_run1");
        down3 = setup("/entities/player_run2");
        down4 = setup("/entities/player_run3");
        down5 = setup("/entities/player_run4");
        down6 = setup("/entities/player_run5");
        left1 = setup("/entities/player_left1");
        left2 = setup("/entities/player_left2");
        left3 = setup("/entities/player_left3");
        left4 = setup("/entities/player_left4");
        left5 = setup("/entities/player_left5");
        left6 = setup("/entities/player_left6");
        right1 = setup("/entities/player_right1");
        right2 = setup("/entities/player_right2");
        right3 = setup("/entities/player_right3");
        right4 = setup("/entities/player_right4");
        right5 = setup("/entities/player_right5");
        right6 = setup("/entities/player_right6");
        dying1 = setup("/entities/player_dying1");
        dying2 = setup("/entities/player_dying2");
        dying3 = setup("/entities/player_dying3");
        dyingSprites = new BufferedImage[]{dying1, dying2, dying3, dying4};

    }

    public void update() {

        if (dying) {
            dyingAnimation();
            return; // Skip other updates while dying
        }
        if (!moving) {

            if (!attacking && (kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed)) {
                if (kH.upPressed) {
                    direction = "up";
                } else if (kH.downPressed) {
                    direction = "down";
                } else if (kH.leftPressed) {
                    direction = "left";
                } else if (kH.rightPressed) {
                    direction = "right";
                }

                moving = true;

                //Check tile collision.
                collisionOn = false;
                gp.checker.checkTile(this);

                gp.checker.checkBomb(this);


                // check object collision
                int objIndex = gp.checker.checkObject(this, true); //entity va boolean cua player
                pickUpObject(objIndex);

                int monsterIndex = gp.checker.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);

                // Bắt đầu di chuyển nếu không có va chạm
                if (!collisionOn) {
                    moving = true;
                }
            } else {
                standCounter++;
                if (standCounter == 12) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }

        // Xử lý di chuyển mượt mà
        if (moving) {
            // Tính toán vị trí tiếp theo
            int nextX = worldX;
            int nextY = worldY;

            switch (direction) {
                case "up":
                    nextY -= speed;
                    break;
                case "down":
                    nextY += speed;
                    break;
                case "left":
                    nextX -= speed;
                    break;
                case "right":
                    nextX += speed;
                    break;
            }

            // Kiểm tra biên giới map (đảm bảo không đi ra ngoài)
            boolean withinMap = nextX >= 0 && nextX <= (gp.maxWorldCol - 1) * gp.tileSize &&
                    nextY >= 0 && nextY <= (gp.maxWorldRow - 1) * gp.tileSize;

            // Điều kiện di chuyển: xuyên tường hoặc không va chạm + trong map
            boolean canMove = (xuyenMode && withinMap) || (!collisionOn && withinMap);

            if (canMove) {
                // Cập nhật vị trí thực tế
                worldX = nextX;
                worldY = nextY;
                pixelCounter += speed;

                // Cập nhật animation
                spriteCounter++;
                if (spriteCounter > 8) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 3;
                    }
                    if (spriteNum == 3) {
                        spriteNum = 4;
                    } else if (spriteNum == 4) {
                        spriteNum = 5;
                    } else if (spriteNum == 5) {
                        spriteNum = 6;
                    } else if (spriteNum == 6) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }

                // Kết thúc di chuyển khi hoàn thành 1 tile
                if (pixelCounter >= gp.tileSize) {
                    moving = false;
                    pixelCounter = 0;
                    // Căn chỉnh vị trí chính xác về tile
                    worldX = (worldX / gp.tileSize) * gp.tileSize;
                    worldY = (worldY / gp.tileSize) * gp.tileSize;
                }
            } else {
                // Nếu không thể di chuyển, dừng lại ngay
                moving = false;
                pixelCounter = 0;
            }
        }

        // CHECK EVENT
        gp.eHandler.checkEvent();

        if (teleportCooldown > 0) {
            teleportCooldown--;
        }

        if (kH.spacePressed) {
            gp.bombManager.handleBombPlacement();
            gp.kH.spacePressed = false; // Tránh đặt bom liên tục
        }


        // Xử lý shield
        if (shieldActive) {
            shieldCounter++;
            if (shieldCounter >= shieldDuration) {
                shieldActive = false;
                shieldCounter = 0;
            }
        }

        // Xử lý chế độ xuyên tường
        if (xuyenMode) {
            xuyenModeMin++;
            if (xuyenModeMin >= xuyenModeMax) {
                xuyenMode = false;
                xuyenModeMin = 0;

                // Kiểm tra xem player có đang trong tường không
                if (isInsideWall()) {
                    pushToNearestValidPosition();
                }
            }
        }

        // Xử lý bất tử tạm thời sau khi bị đánh
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 150) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (life > maxLife) {
            life = maxLife;
        }

        // Kiểm tra máu
        life = Math.min(life, maxLife);
//        if (life <= 0) {
//            int mapNum = gp.currentMap;
//            gp.gameState = gp.gameOverState;
//            gp.ui.showTransition = true;
//            gp.ui.transitionTimer = 0;
//            gp.tileM.loadMap("/maps/map0" + mapNum + ".txt", mapNum);
//            hasBomb = maxBombs;
//            return;
//        }

        if (life <= 0 && !dying) {
            startDying();
        }
    }

    private void startDying() {
        dying = true;
        dyingCounter = 0;
        // Stop all movement
        moving = false;
        attacking = false;
    }

        public void pickUpObject ( int i){

            if (i != 999) {

                String objectName = gp.obj[gp.currentMap][i].name;

                switch (objectName) {
                    case "Key":
                        gp.playSE(1);
                        hasKey++;
                        gp.obj[gp.currentMap][i] = null;
                        System.out.println("Key: " + hasKey);
                        break;
                    case "Door":
                        if (hasKey > 0) {
                            gp.playSE(2);
                            gp.obj[gp.currentMap][i] = null;
                            hasKey--;
                        }
                        System.out.println("Key: " + hasKey);
                        break;
                    case "Boost":
                        gp.playSE(1);
                        if (hasBoost < 2) {
                            hasBoost++;
                            speed += 2;
                        }
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "Chest":
                        break;
                    case "bombItem":
                        gp.playSE(1);
                        maxBombs++;
                        hasBomb++;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "heartItem":
                        gp.playSE(1);
                        life += 1;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "Shield":
                        gp.playSE(1);
                        shieldActive = true;
                        shieldCounter = 0;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "Invisible":
                        gp.playSE(1);
                        xuyenMode = true;
                        xuyenModeMin = 0;
                        gp.obj[gp.currentMap][i] = null;
                        break;

                    case "Explosion+":
                        gp.playSE(1);
                        if(bombRadius < 6) {
                            bombRadius++;
                        }
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "bombUpgrade_TYPE1":
                        gp.playSE(1);
                        // Kích hoạt cho tất cả bomb mới
                        gp.bombManager.globalBreakThrough = true;
                        gp.obj[gp.currentMap][i] = null;
                        break;

                }
            }
        }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible && !shieldActive) {
                life = 0; // Set máu về 0 ngay lập tức
                startDying(); // Bắt đầu animation chết
            }
        }
    }
//        public void damageMonster (int i){
//            if (i != 999 && gp.monster[i] != null) {
//                if (gp.monster[i].invincible == false) {
//                    gp.monster[i].life -= 1;
//                    gp.monster[i].invincible = true;
//                    gp.monster[i].damageReaction();
//
//                    if (gp.monster[i].life <= 0) {
//                        gp.monster[i].dying = true;
//                    }
//                    if(gp.monster[i].life <= 0) {
//                        if (new Random().nextInt(100) < 20) {
//                            OBJ_HeartItem heart = new OBJ_HeartItem(gp);
//                            heart.worldX = gp.monster[i].worldX;
//                            heart.worldY = gp.monster[i].worldY;
//                            gp.obj[gp.currentMap][gp.obj[gp.currentMap].length - 1] = heart;
//                        }
//                    }
//                }
//            }
//        }

        public void draw (Graphics2D g2d) {

            BufferedImage image = null;
            int tempScreenX = screenX;
            int tempScreenY = screenY;

            if (dying) {
                // Show dying animation
                int x = screenX;
                int y = screenY;

                // Handle screen edge cases (same as your existing code)
                if (screenX > worldX) x = worldX;
                if (screenY > worldY) y = worldY;
                int rightOffset = gp.screenWidth - screenX;
                if (rightOffset > gp.worldWidth - worldX) {
                    x = gp.screenWidth - (gp.worldWidth - worldX);
                }
                int bottomOffset = gp.screenHeight - screenY;
                if (bottomOffset > gp.worldHeight - worldY) {
                    y = gp.screenHeight - (gp.worldHeight - worldY);
                }

                g2d.drawImage(this.image, x, y, gp.tileSize, gp.tileSize, null);
                return;
            }

            switch (direction) {
                case "up":
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    if (spriteNum == 3) image = up3;
                    if (spriteNum == 4) image = up4;
                    if (spriteNum == 5) image = up5;
                    if (spriteNum == 6) image = up6;
                    break;
                case "down":
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                    if (spriteNum == 3) image = down3;
                    if (spriteNum == 4) image = down4;
                    if (spriteNum == 5) image = down5;
                    if (spriteNum == 6) image = down6;

                    break;
                case "left":
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                    if (spriteNum == 3) image = left3;
                    if (spriteNum == 4) image = left4;
                    if (spriteNum == 5) image = left5;
                    if (spriteNum == 6) image = left6;
                    break;
                case "right":
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                    if (spriteNum == 3) image = right3;
                    if (spriteNum == 4) image = right4;
                    if (spriteNum == 5) image = right5;
                    if (spriteNum == 6) image = right6;

                    break;
            }

            int x = tempScreenX;
            int y = tempScreenY;

            if (screenX > worldX) {
                x = worldX;
            }
            if (screenY > worldY) {
                y = worldY;
            }
            int rightOffset = gp.screenWidth - screenX;
            if (rightOffset > gp.worldWidth - worldX) {
                x = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - screenY;
            if (bottomOffset > gp.worldHeight - worldY) {
                y = gp.screenHeight - (gp.worldHeight - worldY);
            }

            if(invincible) {
                // Nhấp nháy mỗi 17 frames
                if(invincibleCounter % 17 >= 5) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            }

            g2d.drawImage(image, x, y, gp.tileSize,gp.tileSize, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));// ve trong suot

            //=========XUYEN=======
            if(xuyenMode) {
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(alcom);
                // Hiển thị thời gian còn lại
                int secondsLeft = (xuyenModeMax - xuyenModeMin) / 60 + 1;
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.setColor(Color.WHITE);
                g2d.drawString("Ghost: " + secondsLeft + "s", screenX, screenY - 10);
            }

            if(xuyenMode) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }

    public void setXuyenMode(boolean xuyenMode) {
        this.xuyenMode = xuyenMode;

        if(xuyenMode) {
            xuyenModeMin = 0;
        }
    }

    public boolean isXuyenMode() {
        return xuyenMode;
    }

    public boolean isInsideWall() {
        int playerCol = worldX / gp.tileSize;
        int playerRow = worldY / gp.tileSize;

        // Kiểm tra xem có đang ở ngoài map không
        if (playerCol < 0 || playerCol >= gp.maxWorldCol || playerRow < 0 || playerRow >= gp.maxWorldRow) {
            return true; // Nếu ra khỏi map, coi như đang trong tường
        }

        // Kiểm tra null hoặc chỉ số không hợp lệ
        if (gp.tileM.mapTileNum == null || gp.currentMap < 0 || gp.currentMap >= gp.tileM.mapTileNum.length) {
            return true;
        }

        int tileNum = gp.tileM.mapTileNum[gp.currentMap][playerCol][playerRow];
        return gp.tileM.tile[tileNum].collision;
    }

    private void pushToNearestValidPosition() {
        // Lưu lại vị trí ban đầu để so sánh
        int originalX = worldX;
        int originalY = worldY;

        // Tìm kiếm theo hình xoắn ốc từ trong ra ngoài
        for (int radius = 1; radius <= 5; radius++) {
            // Kiểm tra theo 4 hướng chính trước
            for (int i = 0; i < 4; i++) {
                int testX = originalX;
                int testY = originalY;

                switch (i) {
                    case 0: testX += radius * gp.tileSize; break; // Phải
                    case 1: testX -= radius * gp.tileSize; break; // Trái
                    case 2: testY += radius * gp.tileSize; break; // Xuống
                    case 3: testY -= radius * gp.tileSize; break; // Lên
                }

                if (isValidPosition(testX, testY)) {
                    worldX = testX;
                    worldY = testY;
                    return;
                }
            }

            // Kiểm tra các hướng chéo nếu cần
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (dx == 0 || dy == 0) continue; // Đã kiểm tra ở trên

                    int testX = originalX + dx * gp.tileSize;
                    int testY = originalY + dy * gp.tileSize;

                    if (isValidPosition(testX, testY)) {
                        worldX = testX;
                        worldY = testY;
                        return;
                    }
                }
            }
        }

        // Nếu vẫn không tìm thấy (rất hiếm), dùng vị trí mặc định gần nhất
        worldX = gp.tileSize * 3; // Ví dụ vị trí an toàn
        worldY = gp.tileSize * 3;
    }

    private boolean isValidPosition(int x, int y) {
        // Kiểm tra trong map
        if (x < 0 || x  >= gp.maxWorldCol * gp.tileSize ||
                y < 0 || y >= gp.maxWorldRow * gp.tileSize) {
            return false;
        }

        // Kiểm tra tile không phải tường
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;
        int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
        return !gp.tileM.tile[tileNum].collision;
    }

    @Override
    public void dyingAnimation() {
        dyingCounter++;
        int interval = 15; // Adjust this for faster/slower animation

        if (dyingCounter < interval) {
            image = dyingSprites[0];
        }
        else if (dyingCounter < interval * 2) {
            image = dyingSprites[1];
        }
        else if (dyingCounter < interval * 3) {
            image = dyingSprites[2];
        }

        else {
            // Animation complete - trigger game over
            dying = false;
            alive = false;
            int numMap = gp.currentMap;
            gp.playSE(7);
            gp.gameState = gp.gameOverState;
            gp.ui.showTransition = true;
            gp.ui.transitionTimer = 0;
            gp.loadMap(numMap);
            hasBomb = maxBombs;
            return;
        }
    }
}

