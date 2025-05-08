package Main;

import java.awt.*;

public class EventHandler {

    gamePanel gp;
    EventRect eventRect[][][];
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(gamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;

                if(row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

    }

    public void checkEvent() {

        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            if(hit(0,2,2,"any") == true) {teleport(1,1,1);}
            else if(hit(1,15,4,"any") == true) {teleport(2,1,1);}

        }
    }

    public boolean hit(int map, int col, int row, String reqDirection) {

        boolean hit = false;


        if(map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col *gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row *gp.tileSize + eventRect[map][col][row].y;

            if(gp.player.solidArea.intersects(eventRect[map][col][row])) {
                if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

        }

        return hit;
    }

    public void teleport(int map, int col, int row) {
        gp.currentMap = map;
        gp.player.worldX = gp.tileSize * col;
        gp.player.worldY = gp.tileSize * row;

        previousEventX = gp.player.worldX;
        previousEventY = gp.player.worldY;
        canTouchEvent = false;
        gp.playSE(3);

        gp.player.moving = false;
        gp.player.pixelCounter = 0;
        gp.player.collisionOn = false;
        gp.player.collision = false;
        gp.player.spriteCounter = 0;
        gp.player.direction = "down";
        gp.player.teleportCooldown = 30;
        gp.kH.spacePressed = false;
        gp.player.hasBomb = gp.player.maxBombs;

//        gp.changeMap(map);

        // chuyển sang map mới sẽ hiện stt map đó.
        String transitionText = "Level " + (map + 1);
        gp.gameState = gp.transitionState;
        gp.ui.showTransition = true;
        gp.ui.transitionTimer = 0;
        gp.ui.transitionText = transitionText;

    }
}
