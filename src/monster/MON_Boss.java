//package monster;
//
//import Main.gamePanel;
//import entity.Entity;
//
//import java.awt.*;
//
//public class MON_Boss extends Entity {
//    private int scanRangeWidth = 7 * gp.tileSize; // 7 tiles width (7x48px)
//    private int scanRangeHeight = 48; // 1 tile height
//    private boolean playerDetected = false;
//    private int attackCooldown = 0;
//    private final int attackDelay = 60; // 1.5 giây (90 frames với 60 FPS)
//    private int attackCounter = 0;
//    private boolean isAttacking = false;
//    private Rectangle laserArea;
//    private int laserDamage = 2;
//
//    public MON_Boss(gamePanel gp) {
//        super(gp);
//        this.type = 2; // Monster type
//        this.name = "Boss";
//        this.speed = 1;
//        this.maxLife = 3;
//        this.life = maxLife;
//        this.solidArea = new Rectangle(16, 16, 64, 64); // Hitbox nhỏ hơn sprite
//
//        // Kích thước boss
//        this.width = 96;
//        this.height = 96;
//
//        getBossImage();
//        laserArea = new Rectangle();
//    }
//
//    public void getBossImage() {
//        up1 = setup1("/monster/TEST1",2*gp.tileSize,2*gp.tileSize);
//        up2 = setup1("/monster/TEST2",2*gp.tileSize,2*gp.tileSize);
//        down1 = setup1("/monster/TEST1",2*gp.tileSize,2*gp.tileSize);
//        down2 = setup1("/monster/TEST2",2*gp.tileSize,2*gp.tileSize);
//        left1 = setup1("/monster/TEST1",2*gp.tileSize,2*gp.tileSize);
//        left2 = setup1("/monster/TEST2",2*gp.tileSize,2*gp.tileSize);
//        right1 = setup1("/monster/TEST1",2*gp.tileSize,2*gp.tileSize);
//        right2 = setup1("/monster/TEST2",2*gp.tileSize,2*gp.tileSize);
//    }
//
//    @Override
//    public void setAction() {
//        // Logic di chuyển cơ bản của boss
//        if (!isAttacking) {
//            actionLockCounter++;
//
//            if (actionLockCounter == 120) { // Thay đổi hướng mỗi 2 giây
//                int random = (int)(Math.random() * 4) + 1;
//
//                switch(random) {
//                    case 1: direction = "up"; break;
//                    case 2: direction = "down"; break;
//                    case 3: direction = "left"; break;
//                    case 4: direction = "right"; break;
//                }
//                actionLockCounter = 0;
//            }
//        }
//    }
//
//    @Override
//    public void update() {
//        super.update();
//
//        // Kiểm tra phát hiện người chơi
//        checkPlayerDetection();
//
//        // Xử lý tấn công
//        if (playerDetected) {
//            if (!isAttacking && attackCooldown <= 0) {
//                isAttacking = true;
//                attackCounter = 0;
//                // Hướng về phía người chơi
//                direction = getDirectionToPlayer();
//            }
//
//            if (isAttacking) {
//                attackCounter++;
//
//                // Sau 1.5 giây, bắn laze
//                if (attackCounter >= attackDelay) {
//                    shootLaser();
//                    isAttacking = false;
//                    attackCooldown = 180; // 3 giây hồi chiêu
//                }
//            }
//        }
//
//        if (attackCooldown > 0) {
//            attackCooldown--;
//        }
//    }
//
//    private void checkPlayerDetection() {
//        // Tạo vùng quét phía trước boss dựa trên hướng hiện tại
//        Rectangle scanArea = new Rectangle();
//
//        switch(direction) {
//            case "up":
//                scanArea.setBounds(
//                        worldX + (width - scanRangeWidth)/2,
//                        worldY - scanRangeHeight,
//                        scanRangeWidth,
//                        scanRangeHeight
//                );
//                break;
//            case "down":
//                scanArea.setBounds(
//                        worldX + (width - scanRangeWidth)/2,
//                        worldY + height,
//                        scanRangeWidth,
//                        scanRangeHeight
//                );
//                break;
//            case "left":
//                scanArea.setBounds(
//                        worldX - scanRangeHeight,
//                        worldY + (height - scanRangeWidth)/2,
//                        scanRangeHeight,
//                        scanRangeWidth
//                );
//                break;
//            case "right":
//                scanArea.setBounds(
//                        worldX + width,
//                        worldY + (height - scanRangeWidth)/2,
//                        scanRangeHeight,
//                        scanRangeWidth
//                );
//                break;
//        }
//
//        // Kiểm tra va chạm với người chơi
//        playerDetected = scanArea.intersects(gp.player.getHitbox());
//    }
//
//    private String getDirectionToPlayer() {
//        // Xác định hướng về phía người chơi
//        int playerX = gp.player.worldX + gp.player.solidArea.x;
//        int playerY = gp.player.worldY + gp.player.solidArea.y;
//        int bossX = worldX + solidArea.x;
//        int bossY = worldY + solidArea.y;
//
//        int dx = playerX - bossX;
//        int dy = playerY - bossY;
//
//        if (Math.abs(dx) > Math.abs(dy)) {
//            return dx > 0 ? "right" : "left";
//        } else {
//            return dy > 0 ? "down" : "up";
//        }
//    }
//
//    private void shootLaser() {
//        // Tạo vùng laze tấn công
//        int laserLength = 20 * gp.tileSize; // Chiều dài laze
//
//        switch(direction) {
//            case "up":
//                laserArea.setBounds(
//                        worldX + width/2 - 8,
//                        worldY - laserLength,
//                        16,
//                        laserLength
//                );
//                break;
//            case "down":
//                laserArea.setBounds(
//                        worldX + width/2 - 8,
//                        worldY + height,
//                        16,
//                        laserLength
//                );
//                break;
//            case "left":
//                laserArea.setBounds(
//                        worldX - laserLength,
//                        worldY + height/2 - 8,
//                        laserLength,
//                        16
//                );
//                break;
//            case "right":
//                laserArea.setBounds(
//                        worldX + width,
//                        worldY + height/2 - 8,
//                        laserLength,
//                        16
//                );
//                break;
//        }
//
//        // Kiểm tra va chạm với người chơi
//        if (laserArea.intersects(gp.player.getHitbox())) {
//            if (!gp.player.invincible && !gp.player.shieldActive) {
//                gp.player.life -= laserDamage;
//                gp.player.invincible = true;
//                gp.player.invincibleCounter = 0;
//            }
//        }
//    }
//
//    @Override
//    public void draw(Graphics2D g2) {
//        screenX = worldX - gp.player.worldX + gp.player.screenX;
//        screenY = worldY - gp.player.worldY + gp.player.screenY;
//        super.draw(g2);
//
//        // Vẽ vùng quét khi đang tìm kiếm
//        if (playerDetected && !isAttacking) {
//            drawScanArea(g2);
//        }
//
//        // Vẽ laze khi đang tấn công
//        if (isAttacking) {
//            drawLaserCharge(g2);
//
//            // Vẽ laze khi đã sẵn sàng bắn
//            if (attackCounter >= attackDelay) {
//                drawLaser(g2);
//            }
//        }
//    }
//
//    private void drawScanArea(Graphics2D g2) {
//        Rectangle scanArea = new Rectangle();
//
//        switch(direction) {
//            case "up":
//                scanArea.setBounds(
//                        worldX - gp.player.worldX + gp.player.screenX + (width - scanRangeWidth)/2,
//                        worldY - gp.player.worldY + gp.player.screenY - scanRangeHeight,
//                        scanRangeWidth,
//                        scanRangeHeight
//                );
//                break;
//            case "down":
//                scanArea.setBounds(
//                        worldX - gp.player.worldX + gp.player.screenX + (width - scanRangeWidth)/2,
//                        worldY - gp.player.worldY + gp.player.screenY + height,
//                        scanRangeWidth,
//                        scanRangeHeight
//                );
//                break;
//            case "left":
//                scanArea.setBounds(
//                        worldX - gp.player.worldX + gp.player.screenX - scanRangeHeight,
//                        worldY - gp.player.worldY + gp.player.screenY + (height - scanRangeWidth)/2,
//                        scanRangeHeight,
//                        scanRangeWidth
//                );
//                break;
//            case "right":
//                scanArea.setBounds(
//                        worldX - gp.player.worldX + gp.player.screenX + width,
//                        worldY - gp.player.worldY + gp.player.screenY + (height - scanRangeWidth)/2,
//                        scanRangeHeight,
//                        scanRangeWidth
//                );
//                break;
//        }
//
//        g2.setColor(new Color(255, 255, 0, 100));
//        g2.fill(scanArea);
//    }
//
//    private void drawLaserCharge(Graphics2D g2) {
//        // Hiệu ứng tích tụ năng lượng trước khi bắn
//        int chargeX = screenX + width/2 - 16;
//        int chargeY = screenY + height/2 - 16;
//
//        float progress = (float)attackCounter / attackDelay;
//        int chargeSize = (int)(32 * progress);
//
//        g2.setColor(new Color(255, 0, 0, 150));
//        g2.fillOval(
//                chargeX + (32 - chargeSize)/2,
//                chargeY + (32 - chargeSize)/2,
//                chargeSize,
//                chargeSize
//        );
//    }
//
//    private void drawLaser(Graphics2D g2) {
//        // Vẽ laze
//        int laserScreenX = laserArea.x - gp.player.worldX + gp.player.screenX;
//        int laserScreenY = laserArea.y - gp.player.worldY + gp.player.screenY;
//
//        // Hiệu chỉnh khi ở mép màn hình
//        if (gp.player.worldX < gp.player.screenX) {
//            laserScreenX = laserArea.x;
//        }
//        if (gp.player.worldY < gp.player.screenY) {
//            laserScreenY = laserArea.y;
//        }
//
//        g2.setColor(new Color(255, 0, 0, 150));
//        g2.fillRect(laserScreenX, laserScreenY, laserArea.width, laserArea.height);
//
//        // Hiệu ứng ánh sáng
//        g2.setColor(new Color(255, 100, 100, 70));
//        g2.fillRect(laserScreenX - 5, laserScreenY - 5, laserArea.width + 10, laserArea.height + 10);
//    }
//}