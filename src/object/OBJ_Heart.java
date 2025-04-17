package object;

import Main.gamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends SuperObject {

    public OBJ_Heart(gamePanel gp) {
        name = "Heart";
        image = setup("/objects/heartfull");
        image2 = setup("/objects/hearthalf");
        image3 = setup("/objects/heartblank");
    }
}
