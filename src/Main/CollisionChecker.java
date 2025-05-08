package Main;

import entity.Entity;
import entity.Player;
import object.Bomb;

import java.awt.*;
import java.net.SocketOption;
import java.util.ArrayList;

public class CollisionChecker {
    public boolean contactPlayer = false;                        //loi
    gamePanel gp;

    public CollisionChecker(gamePanel gp) {
        this.gp = gp;
    }

    public boolean checkPlayer(Entity entity) {//loi
        contactPlayer = false;
        entity.solidArea.x = entity.worldX + entity.solidArea.x;                // x = (worldX); y = (worldY)
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch(entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        if(entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
    //Kiem tra va cham voi tuong
    public void checkTile(Entity entity) {

        int entityLeftX = entity.worldX + entity.solidArea.x;                            // x = (worldX); y = (worldY)
        int entityRightX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.worldY + entity.solidArea.y;
        int entityBottomY = entity.worldY + entity.solidArea.y + entity.solidArea.height;


        // Kiểm tra xem có ra khỏi map không
        if (entityLeftX < 0 || entityRightX >= gp.worldWidth ||
                entityTopY < 0 || entityBottomY >= gp.worldHeight) {
            entity.collisionOn = true;
            return;
        }

        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol   ][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }
        if (entity instanceof Player && ((Player)entity).isXuyenMode()) {
            entity.collisionOn = false;
        }


    }

    public int checkObject(Entity entity,boolean player) {

        int index = 999;
        for(int i = 0 ; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] != null) {
                // lay vi tri solidarea cua entity
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // lay vi tri solidarea cua object
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                            break;
                }

                if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    if (gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if (player == true) {
                        index = i;
                    }
                }
                //khong cho x va y tang lien tuc
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public void checkBomb(Entity entity) {
        ArrayList<Bomb> bombs = gp.bombManager.bombList[gp.currentMap];

        for (Bomb bomb : bombs) {
            if (!bomb.alive || bomb.exploded) continue;

            Rectangle entityRect = new Rectangle(
                    entity.worldX + entity.solidArea.x,
                    entity.worldY + entity.solidArea.y,
                    entity.solidArea.width,
                    entity.solidArea.height
            );

            Rectangle bombRect = new Rectangle(
                    bomb.worldX + bomb.solidArea.x,
                    bomb.worldY + bomb.solidArea.y,
                    bomb.solidArea.width,
                    bomb.solidArea.height
            );

            // Kiểm tra Player có nằm hoàn toàn trong Bomb không
            if (bombRect.contains(entityRect)) {
                bomb.collision = false; // Tắt collision của Bomb
            } else {
                bomb.collision = true; // Bật collision của Bomb
            }

            // Kiểm tra va chạm thực tế (chỉ khi collision của Bomb đang bật)
            if (bomb.collision) {
                switch (entity.direction) {
                    case "up":
                        entityRect.y -= entity.speed;
                        break;
                    case "down":
                        entityRect.y += entity.speed;
                        break;
                    case "left":
                        entityRect.x -= entity.speed;
                        break;
                    case "right":
                        entityRect.x += entity.speed;
                        break;
                }

                if (entityRect.intersects(bombRect)) {
                    entity.collisionOn = true;
                }
            }
        }
    }
    public int checkEntity(Entity entity, Entity[][] target) {
        if (entity instanceof Player && ((Player) entity).isXuyenMode()) {
            entity.collisionOn = false;
        } else {
            int index = 999;
            for (int i = 0; i < target[gp.currentMap].length; i++) {
                if (target[gp.currentMap][i] != null) {
                    // lay vi tri solidarea cua entity
                    entity.solidArea.x = entity.worldX + entity.solidArea.x;
                    entity.solidArea.y = entity.worldY + entity.solidArea.y;
                    // lay vi tri solidarea cua object
                    target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                    target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;


                    switch (entity.direction) {
                        case "up":
                            entity.solidArea.y -= entity.speed;
                            break;
                        case "down":
                            entity.solidArea.y += entity.speed;
                            break;
                        case "left":
                            entity.solidArea.x -= entity.speed;
                            break;
                        case "right":
                            entity.solidArea.x += entity.speed;
                            break;
                    }
                    if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) { //kiem tra va cham
                        if (target[gp.currentMap][i] != entity) {
                            entity.collisionOn = true;
                            index = i;
                        }
                    }
                    //khong cho x va y tang lien tuc
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                    target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
                }
            }
            return index;
        }
        return 999;
    }
}
