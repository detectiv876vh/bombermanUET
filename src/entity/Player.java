package entity;

import Main.KeyHandler;
import Main.gamePanel;
import manager.BombManager;
import object.Bomb;
//import manager.ChemManager;
import object.OBJ_HeartItem;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Random;

public class Player extends Entity {

    KeyHandler kH;
    private Graphics2D g2d;
    public int maxBombs = 1;            //bom co ban =1

    public final int screenX;
    public final int screenY;
    public int hasKey = 0; // so key co duoc khi nhat tren map
    public int hasBoost = 0;
//    private ChemManager chemManager;
    public boolean moving = false;
    public int pixelCounter = 0;
    int standCounter = 0;
    public int teleportCooldown = 0;
    public boolean attacking = false;
    public int attackCooldown = 0;
    public final int attackCooldownMax = 30;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;

    public Player(gamePanel gp, KeyHandler kH) {
        super(gp);
        this.kH = kH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(1, 1, 46, 46);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();

    }

    //vị trí ban đầu của player.
    public void setDefaultValues() {

        worldX = gp.tileSize;
        worldY = gp.tileSize;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 20;               //sua lai sau khi test game
        life = maxLife - 4;

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

    public void getPlayerAttackImage() {
        attackUp1 = setup("/player/2");
        attackUp2 = setup("/player/3");
        attackDown1 = setup("/player/2");
        attackDown2 = setup("/player/3");
        attackLeft1 = setup("/player/2");
        attackLeft2 = setup("/player/3");
        attcackRight2 = setup("/player/2");
        attcackRight1 = setup("/player/3");
    }

    public void update() {

//        collisionOn = false;
//        gp.checker.checkTile(this);
//        gp.checker.checkObject(this, true);
//        gp.checker.checkEntity(this, gp.npc);
//        gp.checker.checkEntity(this, gp.monster);

        if (!moving) {
//            if (attacking) {
////                attacking();
//            } else
                if (!attacking && (kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed)) {
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

                gp.checker.checkBomb(this);


                // check object collision
                int objIndex = gp.checker.checkObject(this, true); //entity va boolean cua player
                pickUpObject(objIndex);

//                // CHECK NPC COLLISION
//                int npcIndex = gp.checker.checkEntity(this, gp.npc);
//                interactNPC(npcIndex);
                //CHECK MONSTER COLLISION
                int monsterIndex = gp.checker.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);

                    // Bắt đầu di chuyển nếu không có va chạm
                    if (!collisionOn) {
                        moving = true;
                    }
            } else {
                standCounter++;
                if (standCounter == 12) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }

        if (moving == true) {

            //false thi di chuyen duoc:
            if (!collisionOn && kH.qPressed==false) {
                switch (direction) {
                    case "up": worldY -= speed;
                        break;
                    case "down": worldY += speed;
                        break;
                    case "left": worldX -= speed;
                        break;
                    case "right": worldX += speed;
                        break;
                }
            }
            gp.kH.qPressed = false;

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }

                spriteCounter = 0;
            }

            pixelCounter += speed;

            if (pixelCounter >= 48) {
                moving = false;
                pixelCounter = 0;
            }
        }

        // CHECK EVENT
        gp.eHandler.checkEvent();
        if (kH.spacePressed) {
            gp.bombManager.handleBombPlacement();
        }

        if(attackCooldown > 0) {
            attackCooldown--;
        }

        if (kH.qPressed) {
            // Chỉ tấn công nếu KHÔNG đang di chuyển
            if (!moving) {
//                gp.chemManager.handleChem();
                attacking = true;
            }
            // Reset trạng thái di chuyển
            moving = false;
            pixelCounter = 0;
            spriteCounter = 0;
        }

        // Cập nhật animation tấn công
        if(attacking) {
            attacking();
        }


        if (teleportCooldown > 0) {
            teleportCooldown--;
        }
        //===========SHIELD============
        if (shieldActive) {
            shieldCounter++;
            if (shieldCounter >= shieldDuration) {
                shieldActive = false;
                shieldCounter = 0;
            }
        }

        //ngoia cau lenh chinh giup khi nguoi choi dung im thi invincibleCountre van chay
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (life > maxLife) {
            life = maxLife;
        }

        if (life <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    //=====================ATTACK======================
    public void attacking() {
        if(attacking) {
            spriteCounter++;
            if(spriteCounter > 25) {
                attacking = false;
                spriteCounter = 0;
            }
            else if(spriteCounter <= 5) spriteNum = 1;
            else spriteNum = 2;
        }
    }
        public void pickUpObject (int i) {

            if (i != 999) {

                String objectName = gp.obj[gp.currentMap][i].name;

            switch (objectName) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[gp.currentMap][i] = null;
                    System.out.println("Key: " + hasKey);
                    break;
                case "Door" :
                    if(hasKey > 0) {
                        gp.playSE(2);
                        gp.obj[gp.currentMap][i] = null;
                        hasKey--;
                    }
                    System.out.println("Key: " + hasKey);
                    break;
                case "Boost":
                    if(hasBoost <2 ) {
                        hasBoost++;
                        speed +=2;
                    }
                    gp.obj[gp.currentMap][i] = null;
                    break;
                case "Chest":
                    break;
                case "bombItem":
                    maxBombs ++;
                    gp.obj[gp.currentMap][i] = null;
                    break;
                case "heartItem" :
                    life += 1;
                    gp.obj[gp.currentMap][i] = null;
                    break;
                case "Shield":
                    shieldActive = true;
                    shieldCounter = 0;
                    gp.obj[gp.currentMap][i] = null;
                    break;
            }
        }
    }

        public void interactNPC ( int i){
            if (gp.kH.qPressed == true) {
                if (i != 999) {
                    System.out.println("you are hitting an npc");
                }
            }
        }

        public void contactMonster ( int i){          // giong voi interac
            if (i != 999) {
                if (invincible == false) {
                    invincible = true;
                    life -= 1;
                }
                System.out.println("tru " + 1 + "mau" + ", mau con " + life + "  nho xoa cai test nay di");
                if (!shieldActive) {
                    invincible = true;
                    life -= 1;
                    System.out.println("Took damage! Life: " + life);
                }
            }
    }
//        public void damageMonster (int i){
//            if (i != 999 && gp.monster[i] != null) {
//                if (gp.monster[i].invincible == false) {
//                    gp.monster[i].life -= 1;
//                    gp.monster[i].invincible = true;
//                    gp.monster[i].damageReaction();
//
//                    if (gp.monster[i].life <= 0) {
//                        gp.monster[i].dying = true;
//                    }
//                    if(gp.monster[i].life <= 0) {
//                        if (new Random().nextInt(100) < 20) {
//                            OBJ_HeartItem heart = new OBJ_HeartItem(gp);
//                            heart.worldX = gp.monster[i].worldX;
//                            heart.worldY = gp.monster[i].worldY;
//                            gp.obj[gp.currentMap][gp.obj[gp.currentMap].length - 1] = heart;
//                        }
//                    }
//                }
//            }
//        }

        public void draw (Graphics2D g2d) {

            BufferedImage image = null;
            int tempScreenX = screenX;
            int tempScreenY = screenY;


            if (attacking) {
                switch (direction) {
                    case "up":
                        tempScreenY -= gp.tileSize;
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                        break;
                    case "down":
                        tempScreenY += gp.tileSize;
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                        break;
                    case "left":
                        tempScreenX -= gp.tileSize;
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                        break;
                    case "right":
                        tempScreenX += gp.tileSize;
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                        break;
                }
            } else {
                switch (direction) {
                    case "up":
                        if (spriteNum == 1) image = up1;
                        if (spriteNum == 2) image = up2;
                        break;
                    case "down":
                        if (spriteNum == 1) image = down1;
                        if (spriteNum == 2) image = down2;

                        break;
                    case "left":
                        if (spriteNum == 1) image = left1;
                        if (spriteNum == 2) image = left2;
                        break;
                    case "right":
                        if (spriteNum == 1) image = right1;
                        if (spriteNum == 2) image = right2;

                        break;
                }
            }
            int x = tempScreenX;
            int y = tempScreenY;

            if (screenX > worldX) {
                x = worldX;
            }
            if (screenY > worldY) {
                y = worldY;
            }
            int rightOffset = gp.screenWidth - screenX;
            if (rightOffset > gp.worldWidth - worldX) {
                x = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - screenY;
            if (bottomOffset > gp.worldHeight - worldY) {
                y = gp.screenHeight - (gp.worldHeight - worldY);
            }

            if(invincible) {
                // Nhấp nháy mỗi 10 frames
                if(invincibleCounter % 10 >= 5) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            }

            g2d.drawImage(image, x, y, gp.tileSize,gp.tileSize, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));// ve trong suot

            //DEBUG
//        g2d.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2d.setColor(Color.WHITE);
//        g2d.drawString("Invincible: " + invincibleCounter + "(nho xoa)", 10, 400);
//            g2d.drawImage(image, tempScreenX, tempScreenY, gp.tileSize, gp.tileSize, null);
//            if(attacking) {
//                drawAttackEffect(g2d);
//            }
        }

//    private void drawAttackEffect(Graphics2D g2d) {
//        int attackScreenX = gp.chemManager.attackArea.x - worldX + screenX;
//        int attackScreenY = gp.chemManager.attackArea.y - worldY + screenY;
//
//        // Hiệu chỉnh khi ở mép màn hình
//        if (worldX < screenX) attackScreenX = gp.chemManager.attackArea.x;
//        if (worldY < screenY) attackScreenY = gp.chemManager.attackArea.y;
//
//        g2d.setColor(new Color(255, 0, 0, 100));
//        g2d.fillRect(attackScreenX, attackScreenY, gp.tileSize, gp.tileSize);
//    }

}