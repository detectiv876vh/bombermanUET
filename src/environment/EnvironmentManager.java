package environment;

import Main.gamePanel;
import java.awt.*;

public class EnvironmentManager {

    gamePanel gp;
    Lighting lighting;
    private int lightRadius = 450; // bán kính ô sáng.

    public EnvironmentManager(gamePanel gp) {
        this.gp = gp;
        setup();
    }

    public void setup() {
        lighting = new Lighting(gp, lightRadius);
    }

    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}