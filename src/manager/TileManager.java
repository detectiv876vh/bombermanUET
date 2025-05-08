package manager;

import Main.UtilityTool;
import Main.gamePanel;
import entity.Entity;
import object.*;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class TileManager {
    gamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;

    public TileManager(gamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];  // ex: 10 loai wall grass ...
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/map00.txt", 0);
        loadMap("/maps/map01.txt", 1);
        loadMap("/maps/map02.txt", 2);
    }

    public void getTileImage() {
        //tải ảnh các tile (ô vuông) từ file PNG và gán vào mảng tile.
        setup(0, "wooden", false, false);
        setup(1, "crack_wall", true, false);
        setup(2, "tile5", true, true);
        setup(4,"tile4", false, false);
        setup(5, "tile5", true, false);


    }

    public void setup(int index, String imageName, boolean collision, boolean breakable) {

        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image,gp.tileSize,gp.tileSize);
            tile[index].collision = collision;
            tile[index].breakable = breakable;
        }catch(IOException e) {

        }
    }

    //đặt
    public void explodeTile(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= gp.maxWorldCol || tileY >= gp.maxWorldRow) {
            return;
        }

        int tileNum = mapTileNum[gp.currentMap][tileX][tileY];
        if (tile[tileNum].breakable) {
            // Thay đổi tùy theo map
            if (gp.currentMap == 0) {
                mapTileNum[gp.currentMap][tileX][tileY] = 0; // Gán về tile nền
            } else if (gp.currentMap == 1) {
                mapTileNum[gp.currentMap][tileX][tileY] = 4; // Gán về tile nền khác
            }

            //random tỉ lệ rơi items
            maybeDropItem(tileX, tileY);
        }
    }

    public void maybeDropItem(int tileX, int tileY) {
        Random rand = new Random();
        int chance = rand.nextInt(100); // 0 - 99

        // 25% cơ hội rơi item
        if (chance < 25) {
            Entity item = null;

            if (chance < 2) {
                item = new OBJ_HeartItem(gp); // (0-1]: 1%
            } else if (chance < 10) {
                item = new OBJ_Boost(gp);     // 1-9: 8%
            } else if (chance < 13) {
                item = new OBJ_Bomb(gp);      // 10-13: 3%
            } else if (chance < 15) {
                item = new OBJ_Shield(gp);    // 13-15: 2%
            } else if (chance < 16) {
                item = new OBJ_Invisible(gp); //15-16: 1%
            } else if (chance < 20) {
                item = new OBJ_Explosion(gp);
            }else if (chance < 25) {
                item = new OBJ_BombUP(gp);
            }

            if (item != null) {
                item.worldX = tileX * gp.tileSize;
                item.worldY = tileY * gp.tileSize;

                for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                    if (gp.obj[gp.currentMap][i] == null) {
                        gp.obj[gp.currentMap][i] = item;
                        break;
                    }
                }
            }
        }
    }


    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {

        }
    }

    public void draw(Graphics2D g2) {       //ve do hoa nang cao.
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            // world : vị trí ô trên map
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            //screen : vị trí ô trên màn hình hiển thị
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Dừng camera khi đến rìa
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

            // chỉ vẽ các ô nằm trong màn hình hiển thị (tránh phải vẽ tất cả map)
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            }
            else if(gp.player.screenX > gp.player.worldX||
                    gp.player.screenY > gp.player.worldY||
                    rightOffset > gp.worldWidth - gp.player.worldX ||
                    bottomOffset > gp.worldHeight - gp.player.worldY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;

            }

        }
    }
}
