package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX,worldY;     // worldX, worldY
    public int speed;

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4;
    public BufferedImage right1, right2,right3,right4, left1, left2, left3, left4;

    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public boolean collisionOn = false;

}
