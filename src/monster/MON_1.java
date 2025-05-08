package monster;

import Main.gamePanel;
import entity.Entity;
import java.util.Random;

public class MON_1 extends Entity {

    private Random random;

    public MON_1(gamePanel gp) {
        super(gp);

        // Thiết lập thuộc tính cơ bản
        name = "Simple Monster";
        speed = 1;
        maxLife = 1;
        life = maxLife;
        type = 1; // Loại monster thông thường

        // Thiết lập hitbox
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Khởi tạo random để di chuyển ngẫu nhiên
        random = new Random();

        // Chọn hướng di chuyển ngẫu nhiên ban đầu
        setRandomDirection();

        // Load hình ảnh
        getMonsterImage();
    }

    public void getMonsterImage() {
        // Load các sprites cho monster - sử dụng những sprites sẵn có
        up1 = setup("/monster/TEST1");
        up2 = setup("/monster/TEST2");
        down1 = setup("/monster/TEST1");
        down2 = setup("/monster/TEST2");
        left1 = setup("/monster/TEST1");
        left2 = setup("/monster/TEST2");
        right1 = setup("/monster/TEST1");
        right2 = setup("/monster/TEST2");
    }

    private void setRandomDirection() {
        // Chọn hướng di chuyển ngẫu nhiên
        int i = random.nextInt(100) + 1;

        if (i <= 25) {
            direction = "up";
        }
        else if (i > 25 && i <= 50) {
            direction = "down";
        }
        else if (i > 50 && i <= 75) {
            direction = "left";
        }
        else {
            direction = "right";
        }
    }

    @Override
    public void setAction() {
        // Đổi hướng ngẫu nhiên theo chu kỳ khi không va chạm
        actionLockCounter++;
        if (actionLockCounter == 60) { // Mỗi 60 frame (1 giây ở 60 FPS)
            setRandomDirection();
            actionLockCounter = 0;
        }
    }

    @Override
    public void update() {
        // Kiểm tra va chạm
        collisionOn = false;
        gp.checker.checkTile(this);
        gp.checker.checkObject(this, false);
        gp.checker.checkEntity(this, gp.npc);
        gp.checker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.checker.checkPlayer(this);

        // Nếu va chạm, đổi hướng ngay lập tức
        if (collisionOn) {
            setRandomDirection();
            actionLockCounter = 0;
        } else {
            // Nếu không va chạm, thực hiện setAction thông thường
            setAction();
        }

        // Gây sát thương nếu chạm player
        if (contactPlayer && !gp.player.invincible) {
            gp.player.life -= 1;
            gp.player.invincible = true;
        }

        // Di chuyển theo hướng hiện tại
        if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        // Animation
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    @Override
    public void damageReaction() {
        dying = true;
        alive = false;
        dyingCounter = 0;
        collisionOn = true; // Tắt va chạm khi đang chết
        invincible = true; // Không nhận sát thương thêm
        direction = gp.player.direction;
        hpBarOn = true;
        hpBarCounter = 0;
    }
}