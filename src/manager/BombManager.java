package manager;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;

import java.util.ArrayList;

public class BombManager {

    private final gamePanel gp;
    private final entity.Player player;
    public ArrayList<Bomb>[] bombList;


    public BombManager(gamePanel gp, entity.Player player) {
        this.gp = gp;
        this.player = player;
        bombList = new ArrayList[gp.maxMap];
        for (int i = 0; i < gp.maxMap; i++) {
            bombList[i] = new ArrayList<>();
        }
    }

    public void update() {
        // Không còn xử lý bomb logic ở đây (Bomb tự xử lý nổ bên trong)
    }

    public void handleBombPlacement() {
        if (gp.kH.spacePressed && canPlaceBomb()) {
            // Tính toán vị trí tile chính xác
            int bombTileX = (player.worldX + player.solidArea.x + player.solidArea.width/2) / gp.tileSize;
            int bombTileY = (player.worldY + player.solidArea.y + player.solidArea.height/2) / gp.tileSize;

            Bomb newBomb = new Bomb(gp, gp.drawManager);

            newBomb.worldX = bombTileX * gp.tileSize; // Chuyển lại thành tọa độ thế giới
            newBomb.worldY = bombTileY * gp.tileSize;
            newBomb.bombXpos = bombTileX; // Lưu cả tọa độ tile
            newBomb.bombYpos = bombTileY;
            newBomb.radius = 3;
            newBomb.life = 30;
            newBomb.mapTileNum = gp.tileM.mapTileNum;

            bombList[gp.currentMap].add(newBomb);
            gp.kH.spacePressed = false;
        }
    }

    private boolean canPlaceBomb() {
        for (Entity e : bombList[gp.currentMap]) {
            if (e instanceof Bomb && !((Bomb) e).explosionAnimationComplete) {
                return false;
            }
        }
        return true;
    }
}
