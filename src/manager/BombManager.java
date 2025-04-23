package manager;

import Main.gamePanel;
import entity.Entity;
import entity.Player;
import object.Bomb;
import object.Fire;

import java.util.List;

public class BombManager {

    private final gamePanel gp;
    private final Player player;

    public BombManager(gamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }

    public void update() {
        // Không cần xử lý bomb ở đây nữa nếu đã chuyển toàn bộ logic sang Bomb.java
        // Tránh quản lý lifeFrame hoặc exploded ở đây để tránh trùng lặp.
    }

    public void handleBombPlacement() {
        if (gp.kH.spacePressed && canPlaceBomb()) {
            int bombXpos = (player.worldX + gp.tileSize / 2) - ((player.worldX + gp.tileSize / 2) % gp.tileSize);
            int bombYpos = (player.worldY + gp.tileSize / 2) - ((player.worldY + gp.tileSize / 2) % gp.tileSize);

            Bomb newBomb = new Bomb(gp);
            newBomb.set(bombXpos, bombYpos, "down", true, player);

            gp.projectileList.add(newBomb);
        }
    }

    private boolean canPlaceBomb() {
        // Chỉ cho đặt 1 bomb chưa nổ
        for (Entity e : gp.projectileList) {
            if (e instanceof Bomb && !((Bomb) e).exploded) {
                return false;
            }
        }
        return true;
    }

    public void triggerExplosion(int x, int y, Entity user) {
        Fire up = new Fire(gp); up.set(x, y, "up", true, user);
        Fire down = new Fire(gp); down.set(x, y, "down", true, user);
        Fire left = new Fire(gp); left.set(x, y, "left", true, user);
        Fire right = new Fire(gp); right.set(x, y, "right", true, user);

        gp.projectileList.add(up);
        gp.projectileList.add(down);
        gp.projectileList.add(left);
        gp.projectileList.add(right);
    }
}
