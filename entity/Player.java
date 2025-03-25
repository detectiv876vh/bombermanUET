package entity;

import Main.KeyHandler;
import Main.gamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    gamePanel gp;
    KeyHandler kH;
    private Graphics2D g2d;

    public Player(gamePanel gp,KeyHandler kH ) {
        this.gp = gp;
        this.kH = kH;
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
    x=100;
    y=100;
    speed=4;
    direction="down";
    }
    public void getPlayerImage(){

        try{
            String path = "C:/Users/dungbui/oop/bombermanUET/Resources/anh/";

            up1 = ImageIO.read(new File(path + "sau1.png"));
            up2 = ImageIO.read(new File(path + "sau2.png"));
            down1 = ImageIO.read(new File(path + "truoc1.png"));
            down2 = ImageIO.read(new File(path + "truoc2.png"));
            down3 = ImageIO.read(new File(path + "truoc3.png"));
            down4 = ImageIO.read(new File(path + "truoc4.png"));
            left1 = ImageIO.read(new File(path + "trai1.png"));
            left2 = ImageIO.read(new File(path + "trai2.png"));
            left3 = ImageIO.read(new File(path + "trai3.png"));
            left4 = ImageIO.read(new File(path + "trai4.png"));
            right1 = ImageIO.read(new File(path + "phai1.png"));
            right2 = ImageIO.read(new File(path + "phai2.png"));
            right3 = ImageIO.read(new File(path + "phai3.png"));
            right4 = ImageIO.read(new File(path + "phai4.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public void update() {
        if(kH.upPressed == true|| kH.downPressed == true|| kH.leftPressed == true|| kH.rightPressed == true){
            if(kH.upPressed == true) {
                direction = "up";
                y -= speed;
            }
            else if(kH.downPressed == true) {
                direction = "down";
                y += speed;
            }
            else if(kH.leftPressed == true) {
                direction = "left";
                x -= speed;
            }
            else if(kH.rightPressed == true){
                direction = "right";
                x += speed;
            }

            spriteCounter++;
            if(spriteCounter > 23) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }else if(spriteNum == 2) {
                    spriteNum = 3;
                }else if(spriteNum == 3) {
                    spriteNum = 4;
                }else if(spriteNum == 4) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2d) {

//        g2d.setColor(Color.WHITE);
//
//        g2d.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;
        System.out.println("spriteCounter: " + spriteCounter);


        switch(direction) {
            case "up":
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                }
                if(spriteNum == 3) {
                    image = up3;
                }
                if(spriteNum == 4) {
                    image = up4;
                }
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
                if(spriteNum == 3) {
                    image = down3;
                }
                if(spriteNum == 4) {
                    image = down4;
                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
                if(spriteNum == 3) {
                    image = left3;
                }
                if(spriteNum == 4) {
                    image = left4;
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
                }
                if(spriteNum == 3) {
                    image = right3;
                }
                if(spriteNum == 4) {
                    image = right4;
                }
                break;
        }
        g2d.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

}
