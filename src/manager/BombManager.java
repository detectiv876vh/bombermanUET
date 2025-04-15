package manager;

import Main.gamePanel;
import entity.Player;
import object.Bomb;
import object.Fire;

import java.util.Timer;
import java.util.TimerTask;

public class BombManager {

    private final gamePanel gp;
    private final Player player;

    private Fire projectileUp;
    private Fire projectileDown;
    private Fire projectileLeft;
    private Fire projectileRight;
    private Bomb bomb;

    public BombManager(gamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;

        bomb = new Bomb(gp);
        projectileUp = new Fire(gp);
        projectileDown = new Fire(gp);
        projectileLeft = new Fire(gp);
        projectileRight = new Fire(gp);
    }

    public void handleBombPlacement() {
        if(gp.kH.spacePressed &&
                !projectileUp.alive &&
                !projectileDown.alive &&
                !projectileLeft.alive &&
                !projectileRight.alive) {

            player.shotAvailableCounter = 0;

            int bombXpos = (player.worldX + gp.tileSize / 2) - ((player.worldX + gp.tileSize / 2) % gp.tileSize);
            int bombYpos = (player.worldY + gp.tileSize / 2) - ((player.worldY + gp.tileSize / 2) % gp.tileSize);

            bomb.set(bombXpos, bombYpos, "down", true, player);
            gp.projectileList.add(bomb);

            projectileUp.set(bombXpos, bombYpos, "up", true, player);
            projectileDown.set(bombXpos, bombYpos, "down", true, player);
            projectileLeft.set(bombXpos, bombYpos, "left", true, player);
            projectileRight.set(bombXpos, bombYpos, "right", true, player);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    gp.projectileList.add(projectileUp);
                    gp.projectileList.add(projectileDown);
                    gp.projectileList.add(projectileLeft);
                    gp.projectileList.add(projectileRight);
                }
            }, (bomb.maxLife / gp.FPS) * 1000);
        }
    }
}
