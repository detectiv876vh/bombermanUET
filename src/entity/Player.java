package entity;

import Main.KeyHandler;
import Main.gamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler kH;
    private Graphics2D g2d;

    public Player(gamePanel gp,KeyHandler kH ) {
        super(gp);
        this.kH = kH;

        solidArea = new Rectangle(8,16, 32, 32);

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
    x = 50;
    y = 50;
    speed = 4;
    direction = "down";
    }

    public void getPlayerImage() {

        up1 = setup("/anh/boy_up_1");
        up2 = setup("/anh/boy_up_2");
        down1 = setup("/anh/boy_down_1");
        down2 = setup("/anh/boy_down_2");
        left1 = setup("/anh/boy_left_1");
        left2 = setup("/anh/boy_left_2");
        right1 = setup("/anh/boy_right_1");
        right2 = setup("/anh/boy_right_2");
    }

    public void update() {
        if(kH.upPressed == true|| kH.downPressed == true|| kH.leftPressed == true|| kH.rightPressed == true){
            if(kH.upPressed == true) {
                direction = "up";
            }
            else if(kH.downPressed == true) {
                direction = "down";
            }
            else if(kH.leftPressed == true) {
                direction = "left";
            }
            else if(kH.rightPressed == true){
                direction = "right";
            }

            //Check tile collision.
            collisionOn = false;
            gp.checker.checkTile(this);

            //false thi di chuyen duoc:
            if(collisionOn == false){
                switch(direction){
                    case "up":
                        y -= speed;
                        break;
                    case "down":
                        y += speed;
                        break;
                    case "left":
                        x -= speed;
                        break;
                    case "right":
                        x += speed;
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 23) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }else if(spriteNum == 2) {
                    spriteNum = 1;
                }/*else if(spriteNum == 3) {
                    spriteNum = 4;
                }else if(spriteNum == 4) {
                    spriteNum = 1;
                }*/
                spriteCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2d) {

//        g2d.setColor(Color.WHITE);
//
//        g2d.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;
        //System.out.println("spriteCounter: " + spriteCounter);


        switch(direction) {
            case "up":
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                }
                /*if(spriteNum == 3) {
                    image = up3;
                }
                if(spriteNum == 4) {
                    image = up4;
                }*/
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
//                if(spriteNum == 3) {
//                    image = down3;
//                }
//                if(spriteNum == 4) {
//                    image = down4;
//                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
//                if(spriteNum == 3) {
//                    image = left3;
//                }
//                if(spriteNum == 4) {
//                    image = left4;
//                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
                }
//                if(spriteNum == 3) {
//                    image = right3;
//                }
//                if(spriteNum == 4) {
//                    image = right4;
//                }
                break;
        }
        g2d.drawImage(image, x, y, null);
    }

}
