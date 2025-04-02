package entity;

import Main.gamePanel;

public class Projectile extends Entity{

    protected Entity user;

    public Projectile(gamePanel gp) {
        super(gp);
        solidArea.x = gp.tileSize/8;
        solidArea.y = gp.tileSize/8;
        solidArea.width = gp.tileSize -8 ;
        solidArea.height = gp.tileSize -8;
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

    }

    public void update() {

        if (user == gp.player) {
            gp.checker.checkTileProjectile(this);
        }

        if (user != gp.player) {
        }

        if (name == "Fire") {

            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        life--;
        if (life <= 0) {
            alive = false;
        }

        spriteCounter++;
        if (spriteCounter > 24) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
            }
    }
}
