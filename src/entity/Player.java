package entity;

import Main.KeyHandler;
import Main.gamePanel;
import object.Bomb;
import object.Fire;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;

public class Player extends Entity {

    KeyHandler kH;
    public final int screenX;
    public final int screenY;
    private Graphics2D g2d;

    public Player(gamePanel gp, KeyHandler kH ) {
        super(gp);
        this.kH = kH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(15,20, 20, 20);

        setDefaultValues();
        getPlayerImage();
        bomb = new Bomb(gp);
        projectileUp = new Fire(gp);
        projectileDown = new Fire(gp);
        projectileLeft = new Fire(gp);
        projectileRight = new Fire(gp);

    }
    public void setDefaultValues() {

        worldX = gp.tileSize;
        worldY = gp.tileSize;
        speed = 7;
        direction = "down";

    }

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
        if (kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed){
            if (kH.upPressed) {
                direction = "up";
            }
            else if (kH.downPressed) {
                direction = "down";
            }
            else if (kH.leftPressed) {
                direction = "left";
            }
            else if (kH.rightPressed){
                direction = "right";
            }

            //Check tile collision.
            collisionOn = false;
            gp.checker.checkTile(this);

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
        }

        if(gp.kH.spacePressed && !projectileUp.alive && !projectileDown.alive
                && !projectileLeft.alive && !projectileRight.alive) {

            bombXpos = worldX;
            bombYpos = worldY;

            bomb.set(bombXpos, bombYpos, "down", true,this);
            gp.projectileList.add(bomb);

            projectileUp.set(bombXpos, bombYpos, "up",true, this);
            projectileDown.set(bombXpos, bombYpos, "down",true, this);
            projectileLeft.set(bombXpos, bombYpos, "left", true,this);
            projectileRight.set(bombXpos, bombYpos, "right", true, this);

            new Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    // them vao danh sach cac projectile
                    gp.projectileList.add(projectileUp);
                    gp.projectileList.add(projectileDown);
                    gp.projectileList.add(projectileLeft);
                    gp.projectileList.add(projectileRight);

                }
                },
                    (bomb.maxLife / gp.FPS) * 1000);
        }

        if(shotAvailableCounter < 60) {
            shotAvailableCounter++;
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
        g2d.drawImage(image, worldX, worldY, null);
    }

}
