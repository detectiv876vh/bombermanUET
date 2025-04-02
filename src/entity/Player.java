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

    public final int screenX;
    public final int screenY;

    public Player(gamePanel gp,KeyHandler kH ) {
        this.gp = gp;
        this.kH = kH;

        solidArea = new Rectangle(8,16, 32, 32);

        screenX = gp.screenWidth/2 ;// toa do tam ban do
        screenY = gp.screenHeight/2- (gp.tileSize/2);

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {    // vị trí spawn nhân vật + tốc độ chuyển động
    worldX = 50; // vi tri mac dinh khi xuat hien
    worldY = 50;
    speed = 4;
    direction = "down";
    }
    public void getPlayerImage(){

        try{
            //String path = "D:\\bombermanUET\\Resources\\anh";
            //xoa String path( ko duoc dung absolute path).

            up1 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_up_2.png"));
            //up3 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/sau3.png"));
            //up4 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/sau4.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_down_2.png"));
            //down3 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/truoc3.png"));
            //down4 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/truoc4.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_left_2.png"));
            //left3 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/trai3.png"));
            //left4 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/trai4.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/anh/boy_right_2.png"));
            //right3 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/phai3.png"));
            //right4 = ImageIO.read(getClass().getResourceAsStream("/Resources/anh/phai4.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            if(spriteCounter > 12) {
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

        int x = screenX;
        int y = screenY;

        if(screenX > worldX) {
            x= worldX;
        }
        if(screenY > worldY) {
            y = worldY;
        }
        int rightOffset = gp.screenWidth - screenX;
        if(rightOffset > gp.worldWidth - worldX) {
            x = gp.screenWidth - (gp.worldWidth - worldX);
        }
        int bottomOffset = gp.screenHeight - screenY;
        if(bottomOffset > gp.worldHeight - worldY) {
            y = gp.screenHeight - (gp.worldHeight - worldY);
        }

        g2d.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

}
