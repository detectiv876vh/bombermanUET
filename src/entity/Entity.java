package entity;

import Main.UtilityTool;
import Main.gamePanel;
import manager.DrawManager;
import object.Bomb;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    //STATE
    public gamePanel gp;
    public int worldX, worldY;
    public int speed;

    //LOAD IMAGE
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4;
    public BufferedImage right1, right2, right3, right4, left1, left2, left3, left4;

    public String direction = "down";

    //COUNTER
    public int spriteCounter = 0;
    public int shotAvailableCounter = 0;

    public int spriteNum = 1;
    int dyingCounter = 0;
    int hpBarCounter = 0;

    //HITBOX:
    public Rectangle solidArea;

    public int solidAreaDefauftX, solidAreaDefauftY;
    public boolean collisionOn = false;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public int type;         // player =0;;; npc =1.,,,2 = monster
    public int actionLockCounter = 0;

    //Character status
    public int maxLife;
    public int life;
    public boolean invincible = false; //giu nguoi choi mien nhiem sau khi nhan sat thuong
    public int invincibleCounter = 0;

    //OBJECTS
    public Projectile projectileUp, projectileDown, projectileLeft, projectileRight, bomb;

    // ENTITY STATUS
    public boolean hpBarOn = false;
    public boolean alive = true;
    public boolean dying = false;
    public int bombCount;
    public int bombXpos, bombYpos;

    public Entity(gamePanel gp) {
        this.gp = gp;

        //so-called hitbox:
        solidArea = new Rectangle(8, 16, 30, 30);

    }

    public void setAction() {
    }

    public void damegeReaction() {

    }

    //Kiểm tra va chạm với tường, quái, vật thể:
    public void checkCollision() {
        collision = false;
        gp.checker.checkTile(this);
    }

    //UPDATE FPS
    public void update() {

        setAction();
        collision = false;
        gp.checker.checkTile(this);
        gp.checker.checkEntity(this, gp.npc);
        gp.checker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.checker.checkPlayer(this);
        checkCollision();

        if(this.type == 2 && contactPlayer) {
            if(gp.player.invincible == false) {             //loi
                //can give dame
                gp.player.life -=1;
                gp.player.invincible = true;
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

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        DrawManager drawManager = new DrawManager(gp, this);
        //them cai nay o trong superobj
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Kiểm tra nếu camera đã chạm rìa bản đồ
        if (gp.player.screenX > gp.player.worldX) {
            screenX = worldX;
        }
        if (gp.player.screenY > gp.player.worldY) {
            screenY = worldY;
        }
        int rightOffset = gp.screenWidth + gp.player.screenX;
        if (rightOffset > gp.worldWidth + gp.player.worldX) {
            screenX = gp.screenWidth + (gp.worldWidth + worldX);
        }
        int bottomOffset = gp.screenHeight + gp.player.screenY;
        if (bottomOffset > gp.worldHeight + gp.player.worldY) {
            screenY = gp.screenHeight + (gp.worldHeight + worldY);
        }

        if(     worldX + gp.tileSize > gp.player.worldX - gp.player.screenX    &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX     &&
                worldY + gp.tileSize > gp.player.worldY -gp.player.screenY      &&
                worldY - gp.tileSize < gp.player.worldY +gp.player.screenY) {
            //lay tu player
            switch(direction) {
                case "up":
                    if(spriteNum == 1) {
                        image = up1;
                    }
                    if(spriteNum == 2) {
                        image = up2;
                    }

                    break;
                case "down":
                    if(spriteNum == 1) {
                        image = down1;
                    }
                    if(spriteNum == 2) {
                        image = down2;
                    }

                    break;
                case "left":
                    if(spriteNum == 1) {
                        image = left1;
                    }
                    if(spriteNum == 2) {
                        image = left2;
                    }

                    break;
                case "right":
                    if(spriteNum == 1) {
                        image = right1;
                    }
                    if(spriteNum == 2) {
                        image = right2;
                    }

                    break;
            }

            g2.drawImage(image, screenX, screenY,gp.tileSize,gp.tileSize, null);

        }
        else if(gp.player.screenX > gp.player.worldX||
                gp.player.screenY > gp.player.worldY||
                rightOffset > gp.worldWidth - gp.player.worldX ||
                bottomOffset > gp.worldHeight - gp.player.worldY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }


        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        if(invincible == true ) {
            hpBarOn = true;
            hpBarCounter = 0;
            changAlpha(g2,0.4f);
        }
        if(dying == true) {
            dyingAnimation(g2);
        }

        //MONSTRE HP BAR
        if(type == 2  && hpBarOn) {

            double oneScale = (double)gp.tileSize / maxLife;
            double hpBarValue = oneScale * life;

            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(screenX -1, screenY - 16, gp.tileSize + 2, 12);

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(screenX, screenY - 15,(int)hpBarValue, 10);   //(int)hpBarValue se thay cho gp.tileSize

            hpBarCounter++;

            if(hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }

        drawManager.draw(g2, image, worldX, worldY);

    }
    //ANIMATION LUC MONSTER CHET
    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i=5;

        if(dyingCounter <= i) {changAlpha(g2,0f);}

        if(dyingCounter > i && dyingCounter <= 2*i) {changAlpha(g2,1f);}

        if(dyingCounter > 2*i && dyingCounter <= 3*i) {changAlpha(g2,0f);}

        if(dyingCounter > 3*i && dyingCounter <= 4*i) {changAlpha(g2,1f);}

        if(dyingCounter > 4*i && dyingCounter <= 5*i) {changAlpha(g2,0f);}

        if(dyingCounter > 5*i && dyingCounter <= 6*i) {changAlpha(g2,1f);}

        if(dyingCounter > 6*i && dyingCounter <= 7*i) {changAlpha(g2,0f);}

        if(dyingCounter > 7*i && dyingCounter <= 8*i) {changAlpha(g2,1f);}

        if(dyingCounter > 8*i) {
            dying = false;
            alive = false;
        }
    }

    public void changAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
    public BufferedImage setup(String imagePath) {

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
}
