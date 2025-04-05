package tile;

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
    public int mapTileNum[][];

    public TileManager(gamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];  // ex: 10 loai wall grass ...
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {        //tải ảnh các tile (ô vuông) từ file PNG và gán vào mảng tile.
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall.jpg"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/cracked-wall.png"));
            tile[2].collision = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap() {     // Đọc dữ liệu map từ file rồi lưu vào ma trận
        try {
            InputStream is = getClass().getResourceAsStream("/res/maps/map01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldCol) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
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

            int tileNum = mapTileNum[worldCol][worldRow];

            // world : vị trí ô trên map
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            //screen : vị trí ô trên màn hình hiển thị
            int screenX = worldX - gp.player.x + gp.player.screenX;
            int screenY = worldY - gp.player.y + gp.player.screenY;

            // Dừng camera khi đến rìa
            if(gp.player.screenX > gp.player.x) { // tiến về rìa trái (x giảm dần)
                screenX = worldX;
            }

            if(gp.player.screenY > gp.player.y) { // tiến về rìa bên trên (y giảm dần)
                screenY = worldY;
            }
            int rightOffset = gp.screenWidth - gp.player.screenX;
            if(rightOffset > gp.worldWidth - gp.player.x) {
                screenX = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - gp.player.screenY;
            if(bottomOffset > gp.worldHeight - gp.player.y) {
                screenY = gp.screenHeight - (gp.worldHeight - worldY);
            }

            // chỉ vẽ các ô nằm trong màn hình hiển thị (tránh phải vẽ tất cả map)
            if(worldX + gp.tileSize > gp.player.x - gp.player.screenX &&    // xét khi đi sang trái màn hình
                    worldX - gp.tileSize < gp.player.x + gp.player.screenX &&   // xét khi đi sang phải màn hình
                    worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.y + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            else if(gp.player.screenX > gp.player.x ||
                    gp.player.screenY > gp.player.y ||
                    rightOffset > gp.worldWidth - gp.player.x ||
                    bottomOffset > gp.worldHeight - gp.player.y) {
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
