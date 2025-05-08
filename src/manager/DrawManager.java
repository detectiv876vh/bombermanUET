package manager;

import Main.gamePanel;
import object.Bomb;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DrawManager {

    gamePanel gp;
    public BufferedImage[] bombAnim;
    BufferedImage[] fontExplosion, rightExplosion, leftExplosion, upExplosion, downExplosion;
    public BufferedImage[] rightMiddleExplosion, leftMiddleExplosion, upMiddleExplosion, downMiddleExplosion;
    public int indexAnimExplosion = 0;
    BufferedImage view;

    public DrawManager(gamePanel gp) {
        this.gp = gp;
        setup();
    }

    public void setup() {
        try {
            view = new BufferedImage(gp.WIDTH, gp.HEIGHT, BufferedImage.TYPE_INT_RGB);

            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/objects/sheets.png"));

            bombAnim = new BufferedImage[3];
            fontExplosion = new BufferedImage[4];
            rightExplosion = new BufferedImage[4];
            leftExplosion = new BufferedImage[4];
            upExplosion = new BufferedImage[4];
            downExplosion = new BufferedImage[4];

            // Center end explosion
            fontExplosion[0] = spriteSheet.getSubimage(2 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            fontExplosion[1] = spriteSheet.getSubimage(7 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            fontExplosion[2] = spriteSheet.getSubimage(2 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            fontExplosion[3] = spriteSheet.getSubimage(7 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Right end explosion
            rightExplosion[0] = spriteSheet.getSubimage(4 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            rightExplosion[1] = spriteSheet.getSubimage(9 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            rightExplosion[2] = spriteSheet.getSubimage(4 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            rightExplosion[3] = spriteSheet.getSubimage(9 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Left end explosion
            leftExplosion[0] = spriteSheet.getSubimage(0, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            leftExplosion[1] = spriteSheet.getSubimage(5 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            leftExplosion[2] = spriteSheet.getSubimage(0, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            leftExplosion[3] = spriteSheet.getSubimage(5 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Up end explosion
            upExplosion[0] = spriteSheet.getSubimage(2 * gp.originalTileSize, 4 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            upExplosion[1] = spriteSheet.getSubimage(7 * gp.originalTileSize, 4 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            upExplosion[2] = spriteSheet.getSubimage(2 * gp.originalTileSize, 9 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            upExplosion[3] = spriteSheet.getSubimage(7 * gp.originalTileSize, 9 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Down explosion
            downExplosion[0] = spriteSheet.getSubimage(2 * gp.originalTileSize, 8 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            downExplosion[1] = spriteSheet.getSubimage(7 * gp.originalTileSize, 8 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            downExplosion[2] = spriteSheet.getSubimage(2 * gp.originalTileSize, 13 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            downExplosion[3] = spriteSheet.getSubimage(7 * gp.originalTileSize, 13 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Right middle explosion (phần thân hướng phải)
            rightMiddleExplosion = new BufferedImage[4];
            rightMiddleExplosion[0] = spriteSheet.getSubimage(3 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            rightMiddleExplosion[1] = spriteSheet.getSubimage(8 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            rightMiddleExplosion[2] = spriteSheet.getSubimage(3 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            rightMiddleExplosion[3] = spriteSheet.getSubimage(8 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Left middle explosion (phần thân hướng trái)
            leftMiddleExplosion = new BufferedImage[4];
            leftMiddleExplosion[0] = spriteSheet.getSubimage(1 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            leftMiddleExplosion[1] = spriteSheet.getSubimage(6 * gp.originalTileSize, 6 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            leftMiddleExplosion[2] = spriteSheet.getSubimage(1 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            leftMiddleExplosion[3] = spriteSheet.getSubimage(6 * gp.originalTileSize, 11 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Up middle explosion (phần thân hướng lên)
            upMiddleExplosion = new BufferedImage[4];
            upMiddleExplosion[0] = spriteSheet.getSubimage(2 * gp.originalTileSize, 5 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            upMiddleExplosion[1] = spriteSheet.getSubimage(7 * gp.originalTileSize, 5 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            upMiddleExplosion[2] = spriteSheet.getSubimage(2 * gp.originalTileSize, 10 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            upMiddleExplosion[3] = spriteSheet.getSubimage(7 * gp.originalTileSize, 10 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);

            // Down middle explosion (phần thân hướng xuống)
            downMiddleExplosion = new BufferedImage[4];
            downMiddleExplosion[0] = spriteSheet.getSubimage(2 * gp.originalTileSize, 7 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            downMiddleExplosion[1] = spriteSheet.getSubimage(7 * gp.originalTileSize, 7 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            downMiddleExplosion[2] = spriteSheet.getSubimage(2 * gp.originalTileSize, 12 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            downMiddleExplosion[3] = spriteSheet.getSubimage(7 * gp.originalTileSize, 12 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            // Bomb anim
            for (int i = 0; i < 3; i++) {
                bombAnim[i] = spriteSheet.getSubimage(i * gp.originalTileSize, 3 * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawBomb(Graphics2D g2, Bomb bomb) {
        if (bomb == null) return;

        int tileSize = gp.tileSize;
        int screenX = bomb.worldX - gp.player.worldX + gp.player.screenX;
        int screenY = bomb.worldY - gp.player.worldY + gp.player.screenY;

        // Xử lý camera edges
        if(gp.player.screenX > gp.player.worldX) {
            screenX = bomb.worldX;
        }
        if(gp.player.screenY > gp.player.worldY) {
            screenY = bomb.worldY;
        }
        int rightOffset = gp.screenWidth - gp.player.screenX;
        if(rightOffset > gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - bomb.worldX);
        }
        int bottomOffset = gp.screenHeight - gp.player.screenY;
        if(bottomOffset > gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - bomb.worldY);
        }

        if (bomb.exploded) {
            // Vẽ center nổ
            g2.drawImage(fontExplosion[indexAnimExplosion], screenX, screenY, tileSize, tileSize, null);
            // Vẽ 4 hướng với cả sprite giữa và đuôi
            drawExplosionDirection(g2, bomb, 1, 0, rightMiddleExplosion, rightExplosion);  // right
            drawExplosionDirection(g2, bomb, -1, 0, leftMiddleExplosion, leftExplosion);    // left
            drawExplosionDirection(g2, bomb, 0, -1, upMiddleExplosion, upExplosion);       // up
            drawExplosionDirection(g2, bomb, 0, 1, downMiddleExplosion, downExplosion);    // down

            // Vẽ 4 hướng...
        } else {
            g2.drawImage(bombAnim[bomb.currentFrame], screenX, screenY, tileSize, tileSize, null);
        }
    }

    private void drawExplosionDirection(Graphics2D g2, Bomb bomb, int deltaX, int deltaY,
                                        BufferedImage[] middleSprites, BufferedImage[] endSprites) {
        int tileSize = gp.tileSize;
        int baseTileX = bomb.bombXpos;
        int baseTileY = bomb.bombYpos;

        for (int i = 1; i <= bomb.radius; i++) {
            int targetTileX = baseTileX + deltaX * i;
            int targetTileY = baseTileY + deltaY * i;

            // Kiểm tra biên
            if (targetTileX < 0 || targetTileX >= gp.maxWorldCol ||
                    targetTileY < 0 || targetTileY >= gp.maxWorldRow) {
                break;
            }

            // Kiểm tra va chạm
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][targetTileX][targetTileY];
            boolean isCollision = gp.tileM.tile[tileNum].collision;
            boolean isBreakable = gp.tileM.tile[tileNum].breakable;

            if (isCollision /*&& !isBreakable*/) {
                break;
            }

            // Tính toán vị trí vẽ (theo tọa độ thế giới)
            int worldX = targetTileX * tileSize;
            int worldY = targetTileY * tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Xử lý camera edges
            if(gp.player.screenX > gp.player.worldX) {
                screenX = worldX;
            }
            if(gp.player.screenY > gp.player.worldY) {
                screenY = worldY;
            }
            int rightOffset = gp.screenWidth - gp.player.screenX;
            if(rightOffset > gp.worldWidth - gp.player.worldX) {
                screenX = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - gp.player.screenY;
            if(bottomOffset > gp.worldHeight - gp.player.worldY) {
                screenY = gp.screenHeight - (gp.worldHeight - worldY);
            }

            // Quyết định sprite nào sẽ vẽ
            BufferedImage[] currentSprites;
            if (i == bomb.radius || (isCollision && !isBreakable) ||
                    (isBreakable && i > 1)) {
                currentSprites = endSprites; // Dùng sprite đuôi
            } else {
                currentSprites = middleSprites; // Dùng sprite giữa
            }

            // Vẽ hiệu ứng nổ
            g2.drawImage(currentSprites[indexAnimExplosion], screenX, screenY, tileSize, tileSize, null);

            // Dừng nếu gặp vật cản
            if (isCollision) {
                break;
            }
        }
    }
}
