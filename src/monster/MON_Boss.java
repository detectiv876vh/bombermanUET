package monster;

import Main.gamePanel;
import entity.Entity;
import java.awt.*;
import java.util.Random;

public class MON_Boss extends Entity {
    private int scanRange = 7 * gp.tileSize;
    private boolean playerDetected = false;
    private int attackCooldown = 0;
    private final int attackDelay = 90; // 1.5 giây (90 frames)
    private int attackCounter = 0;
    private boolean isAttacking = false;
    private Rectangle laserArea;
    private int laserDamage = 3;
    private int laserLength = 15 * gp.tileSize;
    private int moveCounter = 0;
    private int moveTime = 60;
    private boolean showLaserPreview = false;
    private int[] laserPreviewPoints = new int[4];

    public MON_Boss(gamePanel gp) {
        super(gp);
        this.type = 2;
        this.name = "Boss";
        this.speed = 2;
        this.maxLife = 30;
        this.life = maxLife;
        // Hitbox bằng kích thước boss (96x96)
        this.solidArea = new Rectangle(24, 24, 82, 82);
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
        this.width = 96;
        this.height = 96;

        getBossImage();
        laserArea = new Rectangle();
    }

    public void getBossImage() {
        up1 = setup96x96("/monster/TEST1");
        up2 = setup96x96("/monster/TEST2");
        down1 = setup96x96("/monster/TEST1");
        down2 = setup96x96("/monster/TEST2");
        left1 = setup96x96("/monster/TEST1");
        left2 = setup96x96("/monster/TEST2");
        right1 = setup96x96("/monster/TEST1");
        right2 = setup96x96("/monster/TEST2");
    }

    @Override
    public void setAction() {
        if (!isAttacking) {
            if (playerDetected) {
                moveCounter = 0;
                direction = getDirectionToPlayer();
                calculateLaserPreview();
            } else {
                moveCounter++;
                if (moveCounter > moveTime) {
                    Random random = new Random();
                    int i = random.nextInt(100) + 1;
                    if (i <= 25) direction = "up";
                    if (i > 25 && i <= 50) direction = "down";
                    if (i > 50 && i <= 75) direction = "left";
                    if (i > 75) direction = "right";
                    moveCounter = 0;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();

        checkPlayerInRange();

        if (playerDetected) {
            if (!isAttacking && attackCooldown <= 0) {
                startAttack();
                showLaserPreview = true;
            }

            if (isAttacking) {
                attackCounter++;
                if (attackCounter >= attackDelay) {
                    shootLaser();
                    finishAttack();
                    showLaserPreview = false;
                }
            }
        } else {
            showLaserPreview = false;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        checkContactDamage();
    }

    private void calculateLaserPreview() {
        int bossCenterX = worldX + width/2;
        int bossCenterY = worldY + height/2;
        int playerCenterX = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width/2;
        int playerCenterY = gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height/2;

        int dx = playerCenterX - bossCenterX;
        int dy = playerCenterY - bossCenterY;

        double length = Math.sqrt(dx*dx + dy*dy);
        if (length > 0) {
            dx = (int)(dx / length * laserLength);
            dy = (int)(dy / length * laserLength);
        }

        laserPreviewPoints[0] = bossCenterX;
        laserPreviewPoints[1] = bossCenterY;
        laserPreviewPoints[2] = bossCenterX + dx;
        laserPreviewPoints[3] = bossCenterY + dy;
    }

    private void checkPlayerInRange() {
        Rectangle playerHitbox = gp.player.getHitbox();
        Rectangle bossHitbox = getHitbox();

        int bossCenterX = bossHitbox.x + bossHitbox.width/2;
        int bossCenterY = bossHitbox.y + bossHitbox.height/2;
        int playerCenterX = playerHitbox.x + playerHitbox.width/2;
        int playerCenterY = playerHitbox.y + playerHitbox.height/2;

        double distance = Math.sqrt(Math.pow(playerCenterX - bossCenterX, 2) +
                Math.pow(playerCenterY - bossCenterY, 2));

        playerDetected = distance <= scanRange;
    }

    private void startAttack() {
        isAttacking = true;
        attackCounter = 0;
        direction = getDirectionToPlayer();
    }

    private void finishAttack() {
        isAttacking = false;
        attackCooldown = 120;
    }

    private String getDirectionToPlayer() {
        Rectangle playerHitbox = gp.player.getHitbox();
        Rectangle bossHitbox = getHitbox();

        int dx = (playerHitbox.x + playerHitbox.width/2) - (bossHitbox.x + bossHitbox.width/2);
        int dy = (playerHitbox.y + playerHitbox.height/2) - (bossHitbox.y + bossHitbox.height/2);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "right" : "left";
        } else {
            return dy > 0 ? "down" : "up";
        }
    }

    private void shootLaser() {
        int laserWidth = 16;
        int bossCenterX = worldX + width/2 - laserWidth/2;
        int bossCenterY = worldY + height/2 - laserWidth/2;

        switch(direction) {
            case "up":
                laserArea.setBounds(bossCenterX, worldY - laserLength, laserWidth, laserLength);
                break;
            case "down":
                laserArea.setBounds(bossCenterX, worldY + height, laserWidth, laserLength);
                break;
            case "left":
                laserArea.setBounds(worldX - laserLength, bossCenterY, laserLength, laserWidth);
                break;
            case "right":
                laserArea.setBounds(worldX + width, bossCenterY, laserLength, laserWidth);
                break;
        }

        // Kiểm tra va chạm chính xác hơn
        if (laserArea.intersects(gp.player.getHitbox())) {
            if (!gp.player.invincible && !gp.player.shieldActive) {
                gp.player.life -= laserDamage;
                gp.player.invincible = true;
                gp.player.invincibleCounter = 0;
                gp.playSE(6);
                System.out.println("Boss laser hit! Player life: " + gp.player.life);
            }
        }
    }

    private void checkContactDamage() {
        if (getHitbox().intersects(gp.player.getHitbox())) {
            if (!gp.player.invincible && !gp.player.shieldActive) {
                gp.player.life -= 1;
                gp.player.invincible = true;
                gp.player.invincibleCounter = 0;
                gp.playSE(6);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        super.draw(g2);

        if (isAttacking) {
            drawLaserCharge(g2);
        }

        if (showLaserPreview) {
            drawLaserPreview(g2);
        }

        if (isAttacking && attackCounter >= attackDelay) {
            drawRealLaser(g2);
        }
    }

    private void drawLaserCharge(Graphics2D g2) {
        int chargeX = screenX + width/2 - 24;
        int chargeY = screenY - 30;
        float chargeProgress = (float)attackCounter / attackDelay;
        int pulseSize = (int)(48 + 10 * Math.sin(chargeProgress * Math.PI * 4));

        g2.setColor(new Color(255, 50, 50, 200));
        g2.fillOval(chargeX, chargeY, pulseSize, pulseSize);

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(chargeX, chargeY, pulseSize, pulseSize);
    }

    private void drawLaserPreview(Graphics2D g2) {
        int x1 = laserPreviewPoints[0] - gp.player.worldX + gp.player.screenX;
        int y1 = laserPreviewPoints[1] - gp.player.worldY + gp.player.screenY;
        int x2 = laserPreviewPoints[2] - gp.player.worldX + gp.player.screenX;
        int y2 = laserPreviewPoints[3] - gp.player.worldY + gp.player.screenY;

        if (gp.player.worldX < gp.player.screenX) {
            x1 = laserPreviewPoints[0];
            x2 = laserPreviewPoints[2];
        }
        if (gp.player.worldY < gp.player.screenY) {
            y1 = laserPreviewPoints[1];
            y2 = laserPreviewPoints[3];
        }

        g2.setColor(new Color(255, 255, 0, 150));
        g2.setStroke(new BasicStroke(5));
        g2.drawLine(x1, y1, x2, y2);

        g2.setColor(Color.YELLOW);
        g2.fillOval(x2 - 10, y2 - 10, 20, 20);
    }

    private void drawRealLaser(Graphics2D g2) {
        int laserScreenX = laserArea.x - gp.player.worldX + gp.player.screenX;
        int laserScreenY = laserArea.y - gp.player.worldY + gp.player.screenY;
        int laserWidth = laserArea.width;
        int laserHeight = laserArea.height;

        if (gp.player.worldX < gp.player.screenX) {
            laserScreenX = laserArea.x;
        }
        if (gp.player.worldY < gp.player.screenY) {
            laserScreenY = laserArea.y;
        }

        GradientPaint gpaint = new GradientPaint(
                laserScreenX, laserScreenY, new Color(255, 0, 0, 200),
                laserScreenX + laserWidth, laserScreenY + laserHeight, new Color(255, 100, 0, 200)
        );
        g2.setPaint(gpaint);
        g2.fillRect(laserScreenX, laserScreenY, laserWidth, laserHeight);

        g2.setColor(new Color(255, 200, 100, 100));
        g2.fillRect(laserScreenX - 5, laserScreenY - 5, laserWidth + 10, laserHeight + 10);
    }

    @Override
    public void damageReaction() {
        hpBarOn = true;
        hpBarCounter = 0;
        direction = getDirectionToPlayer();

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
    public void takeBombDamage() {
        life -= 1; // Trừ 1 máu
        System.out.println("Boss took bomb damage! Life remaining: " + life);

        hpBarOn = true; // Hiển thị thanh máu
        hpBarCounter = 0; // Reset thời gian hiển thị thanh máu

        // Kiểm tra nếu boss chết
        if (life <= 0) {
            dying = true;
        }

        // Phản ứng khi bị damage (nếu cần)
        damageReaction();
    }


}