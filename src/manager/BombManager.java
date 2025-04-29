package manager;

import Main.gamePanel;
import entity.Entity;
import object.Bomb;

public class BombManager {

    private final gamePanel gp;
    private final entity.Player player;

    public BombManager(gamePanel gp, entity.Player player) {
        this.gp = gp;
        this.player = player;
    }

    public void update() {
        // Không còn xử lý bomb logic ở đây (Bomb tự xử lý nổ bên trong)
    }

    // Trong BombManager.handleBombPlacement()
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
            newBomb.radius = 1;
            newBomb.life = 30;
            newBomb.mapTileNum = gp.tileM.mapTileNum;

            gp.projectileList.add(newBomb);
            gp.kH.spacePressed = false;
        }
    }

    private boolean canPlaceBomb() {
        for (Entity e : gp.projectileList) {
            if (e instanceof Bomb && !((Bomb) e).exploded) {
                return false;
            }
        }
        return true;
    }
}
