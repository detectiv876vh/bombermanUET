package entity;

import Main.KeyHandler;
import Main.gamePanel;
import manager.BombManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    KeyHandler kH;
    private Graphics2D g2d;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0; // so key co duoc khi nhat tren map
    private BombManager bombManager;


    public Player(gamePanel gp,KeyHandler kH ) {
        super(gp);
        this.kH = kH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(15,20, 20, 20);
        solidAreaDefauftX = solidArea.x;
        solidAreaDefauftY = solidArea.y;

        setDefaultValues();
        getPlayerImage();

        bombManager = new BombManager(gp, this);

        //Bom.
//        bomb = new Bomb(gp);
//        projectileUp = new Fire(gp);
//        projectileDown = new Fire(gp);
//        projectileLeft = new Fire(gp);
//        projectileRight = new Fire(gp);

    }

    //vị trí ban đầu của player.
    public void setDefaultValues() {

        worldX = gp.tileSize;
        worldY = gp.tileSize;
        speed = 7;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;               //sua lai sau khi test game
        life = maxLife;

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
        if (kH.upPressed || kH.downPressed || kH.leftPressed || kH.rightPressed ){
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

        //ngoia cau lenh chinh giup khi nguoi choi dung im thi invincibleCountre van chay
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        bombManager.handleBombPlacement();
//        if(gp.kH.spacePressed == true
//                && projectileRight.alive == false
//                && projectileLeft.alive == false
//                && projectileDown.alive == false
//                && projectileUp.alive == false) {
//
//            shotAvailableCounter = 0;
//
//
//            bombXpos = (gp.player.worldX + gp.tileSize / 2) - ((gp.player.worldX + gp.tileSize / 2) % gp.tileSize);
//            bombYpos = (gp.player.worldY + gp.tileSize / 2) - ((gp.player.worldY + gp.tileSize / 2) % gp.tileSize);
//
//            bomb.set(bombXpos, bombYpos, "down", true,this);
//            gp.projectileList.add(bomb);
//
//            //Fire fireUp = new Fire(gp);
//            //Fire fireDown = new Fire(gp);
//            //Fire fireLeft = new Fire(gp);
//            //Fire fireRight = new Fire(gp);
//
//            projectileUp.set(bombXpos, bombYpos, "up",true, this);
//            projectileDown.set(bombXpos, bombYpos, "down",true, this);
//            projectileLeft.set(bombXpos, bombYpos, "left", true,this);
//            projectileRight.set(bombXpos, bombYpos, "right", true, this);
//
//            new Timer().schedule(new java.util.TimerTask() {
//                @Override
//                public void run() {
//                    // them vao danh sach cac projectile
//                    gp.projectileList.add(projectileUp);
//                    gp.projectileList.add(projectileDown);
//                    gp.projectileList.add(projectileLeft);
//                    gp.projectileList.add(projectileRight);
//
//                }
//                },
//                    (bomb.maxLife / gp.FPS) * 1000);
//        }
//
//        if(shotAvailableCounter < 60) {
//            shotAvailableCounter++;
//        }

    }
    public void pickUpObject(int i) {

        if(i != 999) {

            String objectName = gp.obj[i].name;

            switch (objectName) {
                case "Key":
                    hasKey++;
                    gp.obj[i] = null;
                    System.out.println("Key: " + hasKey);
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.obj[i] = null;
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

    public void contactMonster(int monsterIndex) {          // giong voi interac
        if(monsterIndex != 999) {
            if(invincible == false) {
                life -= 1;
                invincible = true;
            }
            System.out.println("tru " + life + "mau" + "  nho xoa cai test nay di");
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

        if(invincible) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));// ve trong suot
        }
        g2d.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));// ve trong suot

        //DEBUG
//        g2d.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2d.setColor(Color.WHITE);
//        g2d.drawString("Invincible: " + invincibleCounter + "(nho xoa)", 10, 400);
    }

}
