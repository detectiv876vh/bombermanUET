    package manager;

    import Main.gamePanel;
    import entity.Entity;
    import entity.Player;
    import java.awt.*;

    public class ChemManager {
        private final gamePanel gp;
        private final Player player;
        private int attackRange;
        public Rectangle attackArea;

        public ChemManager(gamePanel gp, Player player) {
            this.gp = gp;
            this.player = player;
            this.attackRange = gp.tileSize;
            this.attackArea = new Rectangle();
        }

        public void handleChem() {
            if (player.attackCooldown <= 0 && gp.kH.qPressed
            ) {
                player.attacking = true;
                player.attackCooldown = player.attackCooldownMax;
                checkCollision(); // Cập nhật attackArea
                gp.kH.qPressed = false; // Reset phím

                // Tấn công quái sau khi cập nhật attackArea
                for (Entity monster : gp.monster) {
                    if (monster != null && monster.getHitbox().intersects(attackArea)) {
                        monster.life -= 1;
                        monster.invincible = true;
                        monster.invincibleCounter = 0;
                    }
                }
            }
            if (!player.moving) {
                player.worldX = (player.worldX / gp.tileSize) * gp.tileSize;
                player.worldY = (player.worldY / gp.tileSize) * gp.tileSize;
            }

        }

        private void checkCollision() {
            // Tính toán tile hiện tại của player (dựa trên hitbox)
            int playerTileX = (player.worldX + player.solidArea.x) / gp.tileSize;
            int playerTileY = (player.worldY + player.solidArea.y) / gp.tileSize;

            // Điều chỉnh tile dựa trên hướng
            switch (player.direction) {
                case "up" -> playerTileY -= 1;
                case "down" -> playerTileY += 1;
                case "left" -> playerTileX -= 1;
                case "right" -> playerTileX += 1;
            }

            // Chuyển tile index thành tọa độ world
            int attackWorldX = playerTileX * gp.tileSize;
            int attackWorldY = playerTileY * gp.tileSize;

            // Đặt Attack Area
            attackArea.setBounds(
                    attackWorldX,
                    attackWorldY,
                    gp.tileSize,
                    gp.tileSize
            );

            // Kiểm tra va chạm với quái
            for (Entity monster : gp.monster) {
                if (monster != null && monster.getHitbox().intersects(attackArea)) {
                    monster.life -= 1;
                    monster.invincible = true;
                }
            }

            System.out.println("Player Position: " + player.worldX + ", " + player.worldY);
            System.out.println("Attack Area: " + attackArea);
        }
    }