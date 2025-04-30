package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(gamePanel gp){
        super(gp);
        name = "Boots";
        down1 = setup("/objects/boots");
    }
}
