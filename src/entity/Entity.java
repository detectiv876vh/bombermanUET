package entity;

import Main.UtilityTool;
import Main.gamePanel;
import manager.DrawManager;
import object.Bomb;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.util.UUID;

public class Entity {
    //STATE
    public gamePanel gp;
    public int worldX, worldY;
    public int speed;
    boolean attacking = false;

    //LOAD IMAGE
    public BufferedImage up1, up2, up3, up4,up5, up6, down1, down2, down3, down4, down5, down6;
    public BufferedImage right1, right2, right3, right4,right5, right6, left1, left2, left3, left4, left5, left6;
    public BufferedImage[] dyingSprites;
    public BufferedImage image, image2, image3;
    public String direction = "down";

    //COUNTER
    public int actionLockCounter = 0;
    public int spriteCounter = 0;
    public int shotAvailableCounter = 0;
    public int pixelCounter = 0;

    public int spriteNum = 1;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;
//    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public static final int type_consumable = 6;
    public boolean stackable = false;
    // =============SHIELD==================
    public boolean shieldActive = false;
    public int shieldCounter = 0;
    public boolean moving = false;
    public final int shieldDuration = 300;


    public String positionId;
    //COUNTER
    protected int moveCounter = 0;


    //HITBOX:
    public Rectangle solidArea; //cho all entity


    //===========Boss======
    public int width;  // Mặc định bằng kích thước tile
    public int height; // Mặc định bằng kích thước tile
    public int screenX; // Sẽ được tính trong draw()
    public int screenY; // Sẽ được tính trong draw()

    public int solidAreaDefauftX, solidAreaDefauftY;
    public boolean collisionOn = false;

    public String name;
    public boolean collision = false;
    public int type;         // player =0;;; npc =1.,,,2 = monster

    //Character status
    public int maxLife;
    public int life;
    public boolean invincible = false; //giu nguoi choi mien nhiem sau khi nhan sat thuong


    //OBJECTS
    public Projectile projectileUp, projectileDown, projectileLeft, projectileRight, bomb;

    // ENTITY STATUS
    public boolean hpBarOn = false;
    public boolean alive = true;
    public boolean dying = false;
    public int bombCount;
    public int bombXpos, bombYpos;
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;

    //AI
    public boolean onPath = false;


        public Entity(gamePanel gp) {
            this.gp = gp;
            this.positionId = UUID.randomUUID().toString();
            this.width = gp.tileSize;
            this.height = gp.tileSize;
            solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
            dyingSprites = new BufferedImage[4]; // ví dụ: 4 frame chết
            this.positionId = UUID.randomUUID().toString();
        }

        public void setAction() {
        }

    //Kiểm tra va chạm với tường, quái, vật thể:
    public void checkCollision() {
        collision = false;
        gp.checker.checkTile(this);
        gp.checker.checkBomb(this);
        gp.checker.checkEntity(this, gp.npc);
        gp.checker.checkEntity(this, gp.monster);
    }

        //UPDATE FPS
        public void update() {

            setAction();

            collisionOn = false;
            checkCollision();

            boolean contactPlayer = gp.checker.checkPlayer(this);
            if (this.type == 2 && contactPlayer) {
                if (gp.player.invincible == false) {
                    //can give dame
                    gp.player.life = 0;
                    gp.player.invincible = true;
                }
            }

            if (moving) {
                pixelCounter += speed;
                if (pixelCounter >= gp.tileSize) {
                    moving = false;
                    pixelCounter = 0;
                }
            }

            //neu ko co gi chan thi di tiep
            if (!collisionOn) {
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

            // Cập nhật animation và trạng thái
            updateSpriteCounter();
            updateInvincibility();
            updateDyingState();
        }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // STOP MOVING CAMERA
        if (gp.player.worldX < gp.player.screenX) {
            screenX = worldX;
        }
        if (gp.player.worldY < gp.player.screenY) {
            screenY = worldY;
        }
        int rightOffset = gp.screenWidth - gp.player.screenX;
        if (rightOffset > gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        int bottomOffset = gp.screenHeight - gp.player.screenY;
        if (bottomOffset > gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - worldY);
        }

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                if (spriteNum == 3) {
                    image = up3;
                }
                if (spriteNum == 4) {
                    image = up4;
                }
                if (spriteNum == 5) {
                    image = up5;
                }
                if (spriteNum == 6) {
                    image = up6;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down3;
                }
                if (spriteNum == 4) {
                    image = down4;
                }
                if (spriteNum == 5) {
                    image = down5;
                }
                if (spriteNum == 6) {
                    image = down6;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                if (spriteNum == 4) {
                    image = left4;
                }
                if (spriteNum == 5) {
                    image = left5;
                }
                if (spriteNum == 6) {
                    image = left6;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }
                if (spriteNum == 4) {
                    image = right4;
                }
                if (spriteNum == 5) {
                    image = right5;
                }
                if (spriteNum == 6) {
                    image = right6;
                }
                break;
        }

        if (dying) {
            g2.drawImage(this.image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            return;
        }


        if (invincible) {
            hpBarOn = true;
            hpBarCounter = 0;
            changAlpha(g2, 0.4f);
        }else {
            changAlpha(g2, 1.0f);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        if (dying) {
            int frameIndex = dyingCounter / 12;
            if (frameIndex >= dyingSprites.length) {
                frameIndex = dyingSprites.length - 1;
            }
            g2.drawImage(dyingSprites[frameIndex], screenX, screenY, gp.tileSize, gp.tileSize, null);
            return; // Không vẽ gì khác nếu đang chết
        }


        //MONSTRE HP BAR
        if (type == 2 && hpBarOn) {

            double oneScale = (double) gp.tileSize / maxLife;
            double hpBarValue = oneScale * life;

            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);   //(int)hpBarValue se thay cho gp.tileSize

            hpBarCounter++;

            if (hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX, screenY, width, height, null);
        }
        // If player is around the edge, draw everything
        else if (gp.player.worldX < gp.player.screenX ||
                gp.player.worldY < gp.player.screenY ||
                rightOffset > gp.worldWidth - gp.player.worldX ||
                bottomOffset > gp.worldHeight - gp.player.worldY) {
//            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            g2.drawImage(image, screenX, screenY,width,height , null);
        }
    }

    public void changAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }




    public BufferedImage setup_obj(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));

            // Cắt khoảng trống xung quanh nếu có
            image = uTool.cropToContent(image);

            // Scale lên đúng tileSize
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public BufferedImage setup96x96(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));

            // Cắt khoảng trống xung quanh nếu có
            image = uTool.cropToContent(image);

            // Scale lên đúng tileSize
            image = uTool.scaleImage(image, 96, 96);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Rectangle getHitbox() {
        return new Rectangle(
                worldX + solidArea.x,
                worldY + solidArea.y,
                solidArea.width,
                solidArea.height
        );
    }

    public void damageReaction() {
    }

    public void setDyingSprites() {
    }

    public void dyingAnimation() {
        dyingCounter++;
        int interval = 12;

        if (dyingCounter < interval) {
            image = dyingSprites[0];
        } else if (dyingCounter < interval * 2) {
            image = dyingSprites[1];
        } else if (dyingCounter < interval * 3) {
            image = dyingSprites[2];
        } else if (dyingCounter < interval * 4) {
            image = dyingSprites[3];
        } else {
            dying = false;
            alive = false;
        }
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x)/gp.tileSize;
        int startRow = (worldY + solidArea.y)/gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search()) {
            if(!gp.pFinder.pathList.isEmpty()) {
                int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
                int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

                // Tính toán hướng đi tối ưu
                if (worldY > nextY) {
                    direction = "up";
                } else if (worldY < nextY) {
                    direction = "down";
                } else if (worldX > nextX) {
                    direction = "left";
                } else if (worldX < nextX) {
                    direction = "right";
                }

                // Kiểm tra va chạm trước khi di chuyển
                checkCollision();
                if (collisionOn) {
                    // Nếu có va chạm, thử hướng khác
                    if (direction.equals("up") || direction.equals("down")) {
                        direction = (worldX > nextX) ? "left" : "right";
                    } else {
                        direction = (worldY > nextY) ? "up" : "down";
                    }
                    checkCollision();
                }
            }
        } else {
            // Nếu không tìm thấy đường đi, tạm dừng theo đuổi
            onPath = false;
        }
    }

    private void updateSpriteCounter() {
        spriteCounter++;
        if (spriteCounter > 5) {
            spriteNum = (spriteNum % 6) + 1;
            spriteCounter = 0;
        }
    }

    private void updateInvincibility() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void updateDyingState() {
        if (dying) {
            dyingAnimation();
        }
    }
}

