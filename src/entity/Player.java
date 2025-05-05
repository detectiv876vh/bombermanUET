package entity;

import Main.KeyHandler;
import Main.gamePanel;
import manager.BombManager;
import manager.ChemManager;
import object.OBJ_HeartItem;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Player extends Entity {

    KeyHandler kH;
    public Graphics2D g2d;
    public int maxBombs = 1;            //bom co ban =1

    public final int screenX;
    public final int screenY;
    public int hasKey = 0; // so key co duoc khi nhat tren map
    public int hasBoost = 0;
    private BombManager bombManager;
    private ChemManager chemManager;
    public boolean moving = false;
    public int pixelCounter = 0;
    int standCounter = 0;
    public int teleportCooldown = 0;
    public boolean attacking = false;
    public int attackCounter = 0;
    public int attackCooldown = 0;
    public final int attackCooldownMax = 30;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    // =========XUYEN =========
    private boolean xuyenMode = false;
    private int xuyenModeMin = 0;
    private final int xuyenModeMax = 300;

    public Player(gamePanel gp, KeyHandler kH) {
        super(gp);
        this.kH = kH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(1, 1, 46, 46);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();

        bombManager = new BombManager(gp, this);

    }

    //vị trí ban đầu của player.
    public void setDefaultValues() {

        worldX = gp.tileSize;
        worldY = gp.tileSize;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;               //sua lai sau khi test game
        life = maxLife - 3;

    }

    //gắn ảnh.
    public void getPlayerImage() {

        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
    }

    public void getPlayerAttackImage() {
        attackUp1 = setup("/player/2");
        attackUp2 = setup("/player/3");
        attackDown1 = setup("/player/2");
        attackDown2 = setup("/player/3");
        attackLeft1 = setup("/player/2");
        attackLeft2 = setup("/player/3");
        attcackRight2 = setup("/player/2");
        attcackRight1 = setup("/player/3");
    }

    public void update() {
        // Xử lý va chạm cơ bản
        collisionOn = false;
        gp.checker.checkTile(this);
        gp.checker.checkObject(this, true);
        gp.checker.checkEntity(this, gp.npc);
        gp.checker.checkEntity(this, gp.monster);

        // Xử lý trạng thái đứng yên và bắt đầu di chuyển
        if (!moving && !attacking) {
            if (kH.upPressed) direction = "up";
            else if (kH.downPressed) direction = "down";
            else if (kH.leftPressed) direction = "left";
            else if (kH.rightPressed) direction = "right";

            if (kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed) {
                moving = true;
                pixelCounter = 0;

                // Kiểm tra va chạm ngay khi bắt đầu di chuyển
                gp.checker.checkTile(this);
                int objIndex = gp.checker.checkObject(this, true);
                pickUpObject(objIndex);
                interactNPC(gp.checker.checkEntity(this, gp.npc));
                contactMonster(gp.checker.checkEntity(this, gp.monster));
            }
        }

        // Xử lý di chuyển mượt mà
        if (moving) {
            // Tính toán vị trí tiếp theo
            int nextX = worldX;
            int nextY = worldY;

            switch (direction) {
                case "up": nextY -= speed; break;
                case "down": nextY += speed; break;
                case "left": nextX -= speed; break;
                case "right": nextX += speed; break;
            }

            // Kiểm tra biên giới map (đảm bảo không đi ra ngoài)
            boolean withinMap = nextX >= 0 && nextX <= (gp.maxWorldCol-1)*gp.tileSize &&
                    nextY >= 0 && nextY <= (gp.maxWorldRow-1)*gp.tileSize;

            // Điều kiện di chuyển: xuyên tường hoặc không va chạm + trong map
            boolean canMove = (xuyenMode && withinMap) || (!collisionOn && withinMap);

            if (canMove) {
                // Cập nhật vị trí thực tế
                worldX = nextX;
                worldY = nextY;
                pixelCounter += speed;

                // Cập nhật animation
                spriteCounter++;
                if (spriteCounter > 12) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
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

        // Xử lý đặt bom (chỉ khi không ở chế độ xuyên)
        if (kH.spacePressed && !xuyenMode) {
            gp.bombManager.handleBombPlacement();
            gp.kH.spacePressed = false; // Tránh đặt bom liên tục
        }

        // Xử lý tấn công (chỉ khi không di chuyển)
        if (kH.qPressed && !moving) {
            gp.chemManager.handleChem();
            attacking = true;
            attackCooldown = attackCooldownMax;
            gp.kH.qPressed = false; // Tránh tấn công liên tục
        }

        // Cập nhật animation tấn công
        if (attacking) {
            attackCounter++;
            if (attackCounter <= 5) spriteNum = 1;
            else if (attackCounter <= 10) spriteNum = 2;
            else {
                attacking = false;
                attackCounter = 0;
                spriteNum = 1;
            }
        }

        // Giảm thời gian hồi chiêu
        if (attackCooldown > 0) attackCooldown--;

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
                        pushOutFromWall(); // Đẩy ra khỏi tường nếu đang ở trong
                    }
                }
            }

        // Xử lý bất tử tạm thời sau khi bị đánh
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 300) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Kiểm tra máu
        life = Math.min(life, maxLife);
        if (life <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    //=====================ATTACK======================
    public void attacking() {
        if(attacking) {
            spriteCounter++;
            if(spriteCounter > 25) {
                attacking = false;
                spriteCounter = 0;
            }
            else if(spriteCounter <= 5) spriteNum = 1;
            else spriteNum = 2;
        }
    }
        public void pickUpObject ( int i){

            if (i != 999) {

                String objectName = gp.obj[gp.currentMap][i].name;

                switch (objectName) {
                    case "Key":
                        hasKey++;
                        gp.obj[gp.currentMap][i] = null;
                        System.out.println("Key: " + hasKey);
                        break;
                    case "Door":
                        if (hasKey > 0) {
                            gp.obj[gp.currentMap][i] = null;
                            hasKey--;
                        }
                        System.out.println("Key: " + hasKey);
                        break;
                    case "Boost":
                        if(hasBoost <2 ) {
                            hasBoost++;
                            speed +=2;
                        }
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "Chest":
                        break;
                    case "bombItem":
                        maxBombs ++;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "heartItem" :
                        life += 1;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "Shield":
                        shieldActive = true;
                        shieldCounter = 0;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case "Invisible":
                        xuyenMode = true;
                        xuyenModeMin = 0;
                        gp.obj[gp.currentMap][i] = null;
                        break;
                }
            }
        }

        public void interactNPC ( int i){
            if (gp.kH.qPressed == true) {
                if (i != 999) {
                    System.out.println("you are hitting an npc");
                }
            }
        }

        public void contactMonster ( int i){          // giong voi interac
            if (i != 999) {
                if (!invincible && !shieldActive) {
                    invincible = true;
                    life -= 1;
                    System.out.println("Took damage! Life: " + life);
                }
            }
    }
        public void damageMonster (int i){
            if (i != 999 && gp.monster[i] != null) {
                if (gp.monster[i].invincible == false) {
                    gp.monster[i].life -= 1;
                    gp.monster[i].invincible = true;
                    gp.monster[i].damageReaction();

                    if (gp.monster[i].life <= 0) {
                        gp.monster[i].dying = true;
                    }
                    if(gp.monster[i].life <= 0) {
                        if (new Random().nextInt(100) < 20) {
                            OBJ_HeartItem heart = new OBJ_HeartItem(gp);
                            heart.worldX = gp.monster[i].worldX;
                            heart.worldY = gp.monster[i].worldY;
                            gp.obj[gp.currentMap][gp.obj[gp.currentMap].length - 1] = heart;
                        }
                    }
                }
            }
        }

        public void draw (Graphics2D g2d){

            BufferedImage image = null;
            int tempScreenX = screenX;
            int tempScreenY = screenY;


            if (attacking) {
                switch (direction) {
                    case "up":
                        tempScreenY -= gp.tileSize;
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                        break;
                    case "down":
                        tempScreenY += gp.tileSize;
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                        break;
                    case "left":
                        tempScreenX -= gp.tileSize;
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                        break;
                    case "right":
                        tempScreenX += gp.tileSize;
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                        break;
                }
            } else {
                switch (direction) {
                    case "up":
                        if (spriteNum == 1) image = up1;
                        if (spriteNum == 2) image = up2;
                        break;
                    case "down":
                        if (spriteNum == 1) image = down1;
                        if (spriteNum == 2) image = down2;

                        break;
                    case "left":
                        if (spriteNum == 1) image = left1;
                        if (spriteNum == 2) image = left2;
                        break;
                    case "right":
                        if (spriteNum == 1) image = right1;
                        if (spriteNum == 2) image = right2;

                        break;
                }
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
                // Nhấp nháy mỗi 10 frames
                if(invincibleCounter % 17 >= 5) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            }

            g2d.drawImage(image, x, y, gp.tileSize,gp.tileSize, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));// ve trong suot

            //DEBUG
//        g2d.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2d.setColor(Color.WHITE);
//        g2d.drawString("Invincible: " + invincibleCounter + "(nho xoa)", 10, 400);
//            g2d.drawImage(image, tempScreenX, tempScreenY, gp.tileSize, gp.tileSize, null);
            if(attacking) {
                drawAttackEffect(g2d);
            }
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

    private void drawAttackEffect(Graphics2D g2d) {
        int attackScreenX = gp.chemManager.attackArea.x - worldX + screenX;
        int attackScreenY = gp.chemManager.attackArea.y - worldY + screenY;

        // Hiệu chỉnh khi ở mép màn hình
        if (worldX < screenX) attackScreenX = gp.chemManager.attackArea.x;
        if (worldY < screenY) attackScreenY = gp.chemManager.attackArea.y;

        g2d.setColor(new Color(255, 0, 0, 100));
        g2d.fillRect(attackScreenX, attackScreenY, gp.tileSize, gp.tileSize);
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
    private void pushOutFromWall() {
        if (!isInsideWall()) return;

        // Danh sách các hướng kiểm tra (ưu tiên hướng hiện tại trước)
        String[] directionsToCheck = {direction, "up", "down", "left", "right"};

        for (String dir : directionsToCheck) {
            int testX = worldX;
            int testY = worldY;

            switch (dir) {
                case "up": testY -= gp.tileSize; break;
                case "down": testY += gp.tileSize; break;
                case "left": testX -= gp.tileSize; break;
                case "right": testX += gp.tileSize; break;
            }

            // Kiểm tra xem vị trí này có hợp lệ không
            boolean canMoveTo = true;

            // Kiểm tra biên giới map (đảm bảo không ra khỏi map)
            if (testX < 0) testX = 0;
            if (testY < 0) testY = 0;
            if (testX >= gp.maxWorldCol * gp.tileSize) testX = (gp.maxWorldCol - 1) * gp.tileSize;
            if (testY >= gp.maxWorldRow * gp.tileSize) testY = (gp.maxWorldRow - 1) * gp.tileSize;

            // Kiểm tra va chạm với tile (nếu trong map)
            int col = testX / gp.tileSize;
            int row = testY / gp.tileSize;

            // Thêm kiểm tra chỉ số mảng để tránh ArrayIndexOutOfBoundsException
            if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
                canMoveTo = false;
            } else {
                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                canMoveTo = !gp.tileM.tile[tileNum].collision;
            }

            // Nếu vị trí này hợp lệ, di chuyển player tới đó
            if (canMoveTo) {
                worldX = (testX / gp.tileSize) * gp.tileSize;
                worldY = (testY / gp.tileSize) * gp.tileSize;
                return; // Thoát sau khi tìm được hướng
            }
        }

        // Nếu không tìm được hướng nào, đẩy ra vị trí spawn mặc định
        worldX = gp.tileSize;
        worldY = gp.tileSize;
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
}