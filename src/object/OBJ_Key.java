package object;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class OBJ_Key extends SuperObject {
    public OBJ_Key() {
        name = "Key";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
//            uTool.scaleImage(image,gp.tileSize,gp.tileSize);
        }catch(IOException e) {
            e.printStackTrace();
        }
        solidArea.x = 5;
    }
}