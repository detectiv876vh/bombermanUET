package monster;

import Main.gamePanel;
import entity.Entity;
import entity.Projectile;

import java.awt.*;
import java.util.Random;

public class MON_Boss extends Entity {
    private int scanRange = 7 * gp.tileSize;
    private boolean playerDetected = false;
    private int attackCooldown = 0;
    private final int attackDelay = 90; // 1.5 giây (90 frames)
    private int attackCounter = 0;
    private boolean isAttacking = false;
    private int moveCounter = 0;
    private int moveTime = 60;
    private int projectileSpeed = 5; // ~6 tiles/giây (5*60 = 300 pixel/giây)
    private int projectileDamage = 3;
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

        getBossImage();}

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

    private void shootProjectile() {
        // Tạo đạn mới
        Projectile projectile = new Projectile(gp);

        // Thiết lập thuộc tính đạn
        projectile.worldX = worldX + width/2 - 8; // Căn giữa boss
        projectile.worldY = worldY + height/2 - 8;
        projectile.direction = direction;
        projectile.speed = projectileSpeed;
//        projectile.damage = projectileDamage;
        projectile.alive = true;

        // Thêm đạn vào game
        gp.projectileList.add(projectile);
        gp.playSE(4); // Chạy âm thanh bắn đạn
    }

    @Override
    public void setAction() {
        if (!isAttacking) {
            if (playerDetected) {
                // Di chuyển thẳng về phía người chơi
                direction = getDirectionToPlayer();
                moveCounter = 0;
            } else {
                // Di chuyển ngẫu nhiên
                if (moveCounter++ > moveTime) {
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
            }

            if (isAttacking) {
                attackCounter++;
                if (attackCounter >= attackDelay) {
                    shootProjectile();
                    finishAttack();
                }
            }
        }
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        checkContactDamage();
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
        super.draw(g2);

        // Vẽ hiệu ứng tích năng lượng khi chuẩn bị bắn
        if (isAttacking) {
            int chargeX = screenX + width/2 - 24;
            int chargeY = screenY - 30;
            float chargeProgress = (float)attackCounter / attackDelay;

            // Vẽ vòng tròn tích năng lượng
            g2.setColor(new Color(255, 50, 50, 200));
            g2.fillOval(chargeX, chargeY, (int)(48 * chargeProgress), (int)(48 * chargeProgress));
        }
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
    private void checkMonsterHit(int tileX, int tileY) {
        Rectangle fireArea = new Rectangle(tileX * gp.tileSize, tileY * gp.tileSize, gp.tileSize, gp.tileSize);

        for (Entity monster : gp.monster[gp.currentMap]) {
            if (monster != null && monster.alive && !monster.dying) {
                Rectangle monsterArea = new Rectangle(
                        monster.worldX + monster.solidArea.x,
                        monster.worldY + monster.solidArea.y,
                        monster.solidArea.width,
                        monster.solidArea.height
                );

                if (fireArea.intersects(monsterArea)) {
                    if (monster.type == 2) { // Nếu là boss (type = 2)
                        ((MON_Boss) monster).takeBombDamage(); // Trừ 1 máu
                    } else {
                        monster.dying = true; // Quái thường chết ngay
                    }
                    gp.playSE(6);
                }
            }
        }
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