package tile;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;    // đại diện và xử lý hình ảnh trong bộ nhớ đệm
    public boolean collision = false;   // kiểm tra va chạm với  khi di chuyển nhân vật
    public boolean breakable = false;
}
