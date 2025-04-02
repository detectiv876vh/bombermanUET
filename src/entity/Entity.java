package entity;

import Main.UtilityTool;
import Main.gamePanel;
import object.Bomb;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public gamePanel gp;
    public int worldX, worldY;
    public int speed;

    //LOAD IMAGE
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4;
    public BufferedImage right1, right2,right3,right4, left1, left2, left3, left4;

    public String direction = "down";

    //COUNTER
    public int spriteCounter = 0;
    public int shotAvailableCounter = 0;

    public int spriteNum = 1;
    //HITBOX:
    public Rectangle solidArea;

    public boolean collisionOn = false;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public int type;

    //Character status
    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter;

    //OBJECTS
    public Projectile projectileUp, projectileDown, projectileLeft, projectileRight, bomb;

    // ENTITY STATUS
    public boolean alive = true;
    public int bombCount;
    public int bombXpos, bombYpos;

    public Entity(gamePanel gp) {
        this.gp = gp;

        //so-called hitbox:
        solidArea =  new Rectangle(8,16,30, 30);
    }

    public void setAction() {
    }

    //Kiểm tra va chạm với tường, quái, vật thể:
    public void checkCollision() {
        collision = false;
        gp.checker.checkTile(this);
    }

    //UPDATE FPS
    public void update() {

        setAction();

        checkCollision();

        if (collisionOn == false) {
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
            }/*else if(spriteNum == 3) {
                    spriteNum = 4;
                }else if(spriteNum == 4) {
                    spriteNum = 1;
                }*/
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1)
                    image = up1;
                if (spriteNum == 2)
                    image = up2;
                break;
            case "down":
                if (spriteNum == 1)
                    image = down1;
                if (spriteNum == 2)
                    image = down2;
                break;
            case "left":
                if (spriteNum == 1)
                    image = left1;
                if (spriteNum == 2)
                    image = left2;
                break;
            case "right":
                if (spriteNum == 1)
                    image = right1;
                if (spriteNum == 2)
                    image = right2;
                break;
        }

        g2.drawImage(image, worldX, worldY, null);
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
