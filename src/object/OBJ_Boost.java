package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Boost extends Entity {

    public OBJ_Boost(gamePanel gp){
        super(gp);
        name = "Boost";
        down1 = setup_obj("/objects/speed");
    }
}
