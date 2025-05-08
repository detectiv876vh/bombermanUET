package object;

import Main.gamePanel;
import entity.Entity;

public class OBJ_Invisible extends Entity {

    gamePanel gp;

    public OBJ_Invisible(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Invisible";
        stackable = true;

        down1 = setup_obj("/objects/xuyen");

        // Các thuộc tính của item
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public boolean use(Entity entity) {
        if(entity == gp.player) {
            // Nếu đã đang ở chế độ xuyên tường, thì reset thời gian
            if(gp.player.isXuyenMode()) {
                gp.player.setXuyenMode(true); // Reset timer
            } else {
                gp.player.setXuyenMode(true);
            }
            return true;
        }
        return false;
    }
}
