package entity;

import Main.KeyHandler;
import Main.gamePanel;
import manager.BombManager;
import object.Bomb;
import object.Fire;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;

public class Player extends Entity {

    KeyHandler kH;
    private Graphics2D g2d;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0; // so key co duoc khi nhat tren map
    private BombManager bombManager;
    public boolean moving = false;
    public int pixelCounter = 0;
    int standCounter = 0;
    public int teleportCooldown = 0;


    public Player(gamePanel gp,KeyHandler kH ) {
        super(gp);
        this.kH = kH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(1,1, 46, 46);
        solidAreaDefauftX = solidArea.x;
        solidAreaDefauftY = solidArea.y;

        setDefaultValues();
        getPlayerImage();

        bombManager = new BombManager(gp, this);

    }

    //vị trí ban đầu của player.
    public void setDefaultValues() {

        worldX = gp.tileSize;
        worldY = gp.tileSize;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;               //sua lai sau khi test game
        life = maxLife - 3;

    }

    //gắn ảnh.
    public void getPlayerImage() {

        up1 = setup("/entities/boy_up_1");
        up2 = setup("/entities/boy_up_2");
        down1 = setup("/entities/boy_down_1");
        down2 = setup("/entities/boy_down_2");
        left1 = setup("/entities/boy_left_1");
        left2 = setup("/entities/boy_left_2");
        right1 = setup("/entities/boy_right_1");
        right2 = setup("/entities/boy_right_2");
    }

    public void update() {

        if(moving == false) {
            if (kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed) {
                if (kH.upPressed) {
                    direction = "up";
                } else if (kH.downPressed) {
                    direction = "down";
                } else if (kH.leftPressed) {
                    direction = "left";
                } else if (kH.rightPressed) {
                    direction = "right";
                }

                moving = true;

                //Check tile collision.
                collisionOn = false;
                gp.checker.checkTile(this);

                // Kiem tra va cham vat the // check object collision
                int objIndex = gp.checker.checkObject(this, true); //entity va boolean cua player
                pickUpObject(objIndex);

            }
            else {
                standCounter++;
                if(standCounter == 12) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }

        if(moving == true) {
            //false thi di chuyen duoc:
            if(!collisionOn){
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
                }

                spriteCounter = 0;
            }

            pixelCounter += speed;

            if(pixelCounter >= 48) {
                moving = false;
                pixelCounter = 0;
            }
        }

        // CHECK EVENT
        gp.eHandler.checkEvent();
        bombManager.handleBombPlacement();
    }
    public void pickUpObject(int i) {

        if(i != 999) {

            String objectName = gp.obj[gp.currentMap][i].name;

            switch (objectName) {
                case "Key" :
                    hasKey++;
                    gp.obj[gp.currentMap][i] = null;
                    System.out.println("Key: " + hasKey);
                    break;
                case "Door" :
                    if(hasKey > 0) {
                        gp.obj[gp.currentMap][i] = null;
                        hasKey--;
                    }
                    System.out.println("Key: " + hasKey);
                    break;
                case "Boots" :
                    break;
                case "Chest" :
                    break;
            }
        }
    }

    public void draw(Graphics2D g2d) {

        BufferedImage image = null;

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
