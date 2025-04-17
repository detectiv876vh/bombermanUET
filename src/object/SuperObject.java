package object;

import Main.gamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefauftX = 0, solidAreaDefauftY = 0;

    public void draw(Graphics2D g2, gamePanel gp) {
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

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        }  else if(gp.player.worldX < gp.player.screenX ||
                gp.player.worldY < gp.player.screenY ||
                rightOffset > gp.worldWidth - gp.player.worldX ||
                bottomOffset > gp.worldHeight - gp.player.worldY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}