package object;

import Main.UtilityTool;
import Main.gamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SuperObject {

    public BufferedImage image,image2,image3;
    public String name;
    public boolean collision;
    public int worldX,worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefauftX = 0,solidAreaDefauftY = 0;
    private Object gp;                                     //day nai dung oi

    public void draw(Graphics2D g2, gamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Kiểm tra nếu camera đã chạm rìa bản đồ
        if (gp.player.screenX > gp.player.worldX) {
            screenX = worldX;
        }
        if (gp.player.screenY > gp.player.worldY) {
            screenY = worldY;
        }
        int rightOffset = gp.screenWidth + gp.player.screenX;
        if (rightOffset > gp.worldWidth + gp.player.worldX) {
            screenX = gp.screenWidth + (gp.worldWidth + worldX);
        }
        int bottomOffset = gp.screenHeight + gp.player.screenY;
        if (bottomOffset > gp.worldHeight + gp.player.worldY) {
            screenY = gp.screenHeight + (gp.worldHeight + worldY);
        }

        if(     worldX + gp.tileSize > gp.player.worldX - gp.player.screenX    &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX     &&
                worldY + gp.tileSize > gp.player.worldY -gp.player.screenY      &&
                worldY - gp.tileSize < gp.player.worldY +gp.player.screenY) {

            g2.drawImage(image, screenX, screenY,gp.tileSize,gp.tileSize, null);

        }
        else if(gp.player.screenX > gp.player.worldX||
                gp.player.screenY > gp.player.worldY||
                rightOffset > gp.worldWidth - gp.player.worldX ||
                bottomOffset > gp.worldHeight - gp.player.worldY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    // THEM TRINH DOC ANH
    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}