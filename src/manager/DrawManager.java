package manager;

import Main.gamePanel;
import entity.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DrawManager {

    gamePanel gp;

    public DrawManager(gamePanel gp, Entity entity) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2, BufferedImage image, int worldX, int worldY) {
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

        // Kiểm tra xem đối tượng có nằm trong vùng hiển thị không
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        } else if (gp.player.screenX > gp.player.worldX ||
                gp.player.screenY > gp.player.worldY ||
                rightOffset > gp.worldWidth - gp.player.worldX ||
                bottomOffset > gp.worldHeight - gp.player.worldY) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
