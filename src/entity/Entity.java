package entity;

import Main.UtilityTool;
import Main.gamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public gamePanel gp;
    public int x,y;
    public int speed;

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4;
    public BufferedImage right1, right2,right3,right4, left1, left2, left3, left4;

    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public boolean collisionOn = false;

    public String name;

    //Character status
    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter;

    // ENTITY STATUS
    public boolean alive = true;
    public int bombCount;
    public int bombXpos, bombYpos;

    public Entity(gamePanel gp) {
        this.gp = gp;
        solidArea =  new Rectangle(0,0,gp.tileSize,gp.tileSize);
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
