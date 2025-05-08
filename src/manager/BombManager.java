package manager;

import Main.gamePanel;
import entity.Entity;
import entity.Player;
import object.Bomb;

import java.util.ArrayList;

public class BombManager {

    private final gamePanel gp;
    private final entity.Player player;
    public ArrayList<Bomb>[] bombList;
    public boolean globalBreakThrough = false; // Trạng thái toàn cục


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
        // Tính toán vị trí tile chính xác
        int bombTileX = (player.worldX + player.solidArea.x + player.solidArea.width/2) / gp.tileSize;
        int bombTileY = (player.worldY + player.solidArea.y + player.solidArea.height/2) / gp.tileSize;
        if (gp.kH.spacePressed
                && !gp.player.isXuyenMode()
                && canPlaceBomb(bombTileX, bombTileY)
                && player.teleportCooldown <= 0
                && player.hasBomb > 0) {

            Bomb newBomb = new Bomb(gp, gp.drawManager);

            newBomb.worldX = bombTileX * gp.tileSize; // Chuyển lại thành tọa độ thế giới
            newBomb.worldY = bombTileY * gp.tileSize;
            newBomb.bombXpos = bombTileX; // Lưu cả tọa độ tile
            newBomb.bombYpos = bombTileY;
            newBomb.radius = gp.player.bombRadius;
            newBomb.life = 30;
            newBomb.mapTileNum = gp.tileM.mapTileNum;
            newBomb.canBreakThrough = this.globalBreakThrough; // Áp dụng trạng thái


            bombList[gp.currentMap].add(newBomb);
            player.hasBomb--;
        }
    }

    private boolean canPlaceBomb(int tileX, int tileY) {
        for (Bomb bomb : bombList[gp.currentMap]) {
            if (bomb.bombXpos == tileX && bomb.bombYpos == tileY && !bomb.explosionAnimationComplete) {
                return false; // Có bomb tại tile này
            }
        }
        return true;
    }
}
