package tile;

import Main.UtilityTool;
import Main.gamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    gamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;

    public TileManager(gamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];  // ex: 10 loai wall grass ...
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/map01.txt", 0);
        loadMap("/maps/map02.txt", 1);
    }

    public void getTileImage() {
        //tải ảnh các tile (ô vuông) từ file PNG và gán vào mảng tile.
        setup(0, "tile3", false, false);
        setup(1, "New_wall", true, false);
        setup(2, "crack_wall", true, true);
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
    public void explodeTile(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow) {
            int tileIndex = mapTileNum[1][col][row];

            if (tile[tileIndex].breakable) {
                mapTileNum[1][col][row] = 4; // Gán về tile nền
            }
        }
    }


    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldCol) {
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
