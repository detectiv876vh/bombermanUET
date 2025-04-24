package entity;

import Main.KeyHandler;
import Main.gamePanel;
import manager.BombManager;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        life = maxLife - 4;

    }

    //gắn ảnh.
    public void getPlayerImage() {

        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
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

            // CHECK NPC COLLISION
            int npcIndex = gp.checker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            //CHECK MONSTER COLLISION
            int monsterIndex = gp.checker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);
            //false thi di chuyen duoc:


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

        //ngoia cau lenh chinh giup khi nguoi choi dung im thi invincibleCountre van chay
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if(life > maxLife) {
            life = maxLife;
        }

        if(life <= 0) {
            gp.gameState = gp.gameOverState;;
        }
        // CHECK EVENT
        gp.eHandler.checkEvent();
        bombManager.handleBombPlacement();
    }
    public void pickUpObject(int i) {

        if(i != 999) {

            String objectName = gp.obj[gp.currentMap][i].name;

            switch (objectName) {
                case "Key":
                    hasKey++;
                    gp.obj[gp.currentMap][i] = null;
                    System.out.println("Key: " + hasKey);
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.obj[gp.currentMap][i] = null;
                        hasKey--;
                    }
                    System.out.println("Key: " + hasKey);
                    break;
                case "Boots":
                    break;
                case "Chest":
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if(i != 999) {
            System.out.println("you are hitting an npc");
        }
    }

    public void contactMonster(int i) {          // giong voi interac
        if(i != 999) {
            if(invincible == false) {

                invincible = true;
                life -= 1;
            }
            System.out.println("tru " + 1 + "mau" + ", mau con "+life+"  nho xoa cai test nay di");

        }

    }

    public void damageMonter(int i) {
        if(i != 999) {
            if( gp.monster[i].invincible == false) {
                gp.monster[i].life -= 1;
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if(gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                }
            }
        }
    }



}
