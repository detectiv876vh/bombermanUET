package manager;

import Main.gamePanel;
import entity.Entity;
import entity.Player;
import object.Bomb;
import object.Fire;

public class BombManager {

    private final gamePanel gp;
    private final Player player;

    public BombManager(gamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }

    public void update() {
        // Không xử lý bomb ở đây nữa nếu đã để Bomb tự quản lý lifecycle
    }

    public void handleBombPlacement() {
        if (gp.kH.spacePressed && canPlaceBomb() && player.teleportCooldown <= 0&& player.maxBombs > 0) {
            int bombXpos = (player.worldX + gp.tileSize / 2) - ((player.worldX + gp.tileSize / 2) % gp.tileSize);
            int bombYpos = (player.worldY + gp.tileSize / 2) - ((player.worldY + gp.tileSize / 2) % gp.tileSize);

            Bomb newBomb = new Bomb(gp);
            newBomb.mapIndex = gp.currentMap;
            newBomb.set(bombXpos, bombYpos, "down", true, player);
            gp.projectileList[gp.currentMap].add(newBomb);
                player.maxBombs -=1;

        }
    }

    private boolean canPlaceBomb() {
        // Chỉ kiểm tra vị trí đặt bomb có trống không
        int bombX = (player.worldX + gp.tileSize/2) / gp.tileSize * gp.tileSize;
        int bombY = (player.worldY + gp.tileSize/2) / gp.tileSize * gp.tileSize;

        // Kiểm tra không có bomb nào ở vị trí này
        for (Entity e : gp.projectileList[gp.currentMap]) {
            if (e instanceof Bomb && e.worldX == bombX && e.worldY == bombY) {
                return false;
            }
        }
        return (player.maxBombs > 0 );
    }

    public void triggerExplosion(int x, int y, Entity user) {
        int mapIndex = gp.currentMap;
        Fire up = new Fire(gp); up.set(x, y, "up", true, user);
        Fire down = new Fire(gp); down.set(x, y, "down", true, user);
        Fire left = new Fire(gp); left.set(x, y, "left", true, user);
        Fire right = new Fire(gp); right.set(x, y, "right", true, user);

        gp.projectileList[mapIndex].add(up);
        gp.projectileList[mapIndex].add(down);
        gp.projectileList[mapIndex].add(left);
        gp.projectileList[mapIndex].add(right);

        player.maxBombs +=1;
    }
}
