    package entity;

    import Main.UtilityTool;
    import Main.gamePanel;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.util.UUID;

    public class Entity {
        //STATE
        public gamePanel gp;
        public int worldX, worldY;
        public int speed;
        boolean attacking = false;


        //LOAD IMAGE
        public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4;
        public BufferedImage right1, right2, right3, right4, left1, left2, left3, left4;
        public BufferedImage attackUp1,attackUp2,attackDown1,attackDown2,attackLeft1,attackLeft2,
                attcackRight1,attcackRight2;
        public BufferedImage image, image2, image3;
        public String direction = "down";
        public String positionId;
        //COUNTER
        protected int moveCounter = 0;
        public int spriteCounter = 0;
        public int shotAvailableCounter = 0;
        public int spriteNum = 1;
        public int dyingCounter = 0;
        public int hpBarCounter = 0;
        public int actionLockCounter = 0;
        public int invincibleCounter = 0;
        public static final int type_consumable = 6;
        public boolean stackable = false;
        // =============SHIELD==================
        public boolean shieldActive = false;
        public int shieldCounter = 0;
        public final int shieldDuration = 300;
        public boolean moving = false;
        public int pixelCounter = 0;


        //HITBOX:
        public Rectangle solidArea; //cho all entity

        public boolean collisionOn = false;

        public String name;
        public boolean collision = false;
        public int type;         // player =0;;; npc =1.,,,2 = monster


        //Character status
        public int maxLife;
        public int life;
        public boolean invincible = false; //giu nguoi choi mien nhiem sau khi nhan sat thuong


        //OBJECTS
        public Projectile projectileUp, projectileDown, projectileLeft, projectileRight, bomb;

        // ENTITY STATUS
        public boolean hpBarOn = false;
        public boolean alive = true;
        public boolean dying = false;
        public int bombCount;
        public int bombXpos, bombYpos;
        public int solidAreaDefaultX;
        public int solidAreaDefaultY;

        public Entity(gamePanel gp) {
            this.gp = gp;
            this.positionId = UUID.randomUUID().toString();
            //so-called hitbox:
            solidArea = new Rectangle(0, 0, 48, 48);

        }

        public void setAction() {
        }

        //Kiểm tra va chạm với tường, quái, vật thể:
        public void checkCollision() {
    //        collision = false;
            gp.checker.checkTile(this);
        }

        //UPDATE FPS
        public void update() {

            setAction();
            collision = false;
            //truoc di chuyen
            int oldX = worldX;
            int oldY = worldY;
            // Kiểm tra va chạm theo thứ tự ưu tiên
            gp.checker.checkTile(this);
            gp.checker.checkEntity(this, gp.npc);
            gp.checker.checkEntity(this, gp.monster);
            boolean contactPlayer = gp.checker.checkPlayer(this);
            checkCollision();
            if (moving) {
                pixelCounter += speed;
                if (pixelCounter >= 48) {
                    moving = false;
                    pixelCounter = 0;
                }
            }

            if(this.type == 2 && contactPlayer) {
                if(gp.player.invincible == false) {             //loi
                    //can give dame
                    gp.player.life -=1;
                    gp.player.invincible = true;
                }
            }

            //neu ko co gi chan thi di tiep
            if (!collisionOn) {
                switch (direction) {
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
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 60) { // Giả sử thời gian invincible là 60 frame (1 giây)
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
        }

        public void draw(Graphics2D g2) {

            BufferedImage image = null;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // STOP MOVING CAMERA
            if (gp.player.worldX < gp.player.screenX) {
                screenX = worldX;
            }
            if (gp.player.worldY < gp.player.screenY) {
                screenY = worldY;
            }
            int rightOffset = gp.screenWidth - gp.player.screenX;
            if (rightOffset > gp.worldWidth - gp.player.worldX) {
                screenX = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - gp.player.screenY;
            if (bottomOffset > gp.worldHeight - gp.player.worldY) {
                screenY = gp.screenHeight - (gp.worldHeight - worldY);
            }

            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            if (invincible == true) {
                hpBarOn = true;
                hpBarCounter = 0;
                changAlpha(g2, 0.4f);
            }else {
                changAlpha(g2, 1.0f);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            if (dying == true) {
                dyingAnimation(g2);
            }

            //MONSTRE HP BAR
            if (type == 2 && hpBarOn) {

                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);   //(int)hpBarValue se thay cho gp.tileSize

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            // If player is around the edge, draw everything
            else if (gp.player.worldX < gp.player.screenX ||
                    gp.player.worldY < gp.player.screenY ||
                    rightOffset > gp.worldWidth - gp.player.worldX ||
                    bottomOffset > gp.worldHeight - gp.player.worldY) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
        //ANIMATION LUC MONSTER CHET
        public void dyingAnimation(Graphics2D g2) {
            dyingCounter++;
            int i=5;

            if(dyingCounter <= i) {changAlpha(g2,0f);}

            if(dyingCounter > i && dyingCounter <= 2*i) {changAlpha(g2,1f);}

            if(dyingCounter > 2*i && dyingCounter <= 3*i) {changAlpha(g2,0f);}

            if(dyingCounter > 3*i && dyingCounter <= 4*i) {changAlpha(g2,1f);}

            if(dyingCounter > 4*i && dyingCounter <= 5*i) {changAlpha(g2,0f);}

            if(dyingCounter > 5*i && dyingCounter <= 6*i) {changAlpha(g2,1f);}

            if(dyingCounter > 6*i && dyingCounter <= 7*i) {changAlpha(g2,0f);}

            if(dyingCounter > 7*i && dyingCounter <= 8*i) {changAlpha(g2,1f);}

            if(dyingCounter > 8*i) {
                dying = false;
                alive = false;
            }
        }

        public void changAlpha(Graphics2D g2, float alphaValue) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
        }
        public BufferedImage setup(String imagePath) {

            UtilityTool uTool = new UtilityTool();
            BufferedImage image = null;

            try {
                image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
                image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
        public Rectangle getHitbox() {
            return new Rectangle(
                    worldX + solidArea.x,
                    worldY + solidArea.y,
                    solidArea.width,
                    solidArea.height
            );
        }
        protected void damageReaction() {
        }
    }
