package environment;

import Main.gamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {

    gamePanel gp;
    BufferedImage darknessFilter;
    private int lightRadius;

    public Lighting(gamePanel gp, int circleSize) {
        this.gp = gp;
        this.lightRadius = circleSize;

        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
    }

    public void draw(Graphics2D g2) {

        // Chỉ vẽ ở map 2.
        if (gp.currentMap == 1) {
            // vẽ lại khi player ở dìa map.
            updateDarknessFilter();

            g2.drawImage(darknessFilter, 0, 0, null);
        }
    }

    private void updateDarknessFilter() {

        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();

        int centerX = gp.player.screenX + (gp.tileSize / 2);
        int centerY = gp.player.screenY + (gp.tileSize / 2);

        // Điều chỉnh nếu player đang ở gần rìa bản đồ.
        if (gp.player.worldX < gp.player.screenX) {
            centerX = gp.player.worldX + (gp.tileSize / 2);
        }
        if (gp.player.worldY < gp.player.screenY) {
            centerY = gp.player.worldY + (gp.tileSize / 2);
        }

        int rightOffset = gp.screenWidth - gp.player.screenX;
        if (rightOffset > gp.worldWidth - gp.player.worldX) {
            centerX = gp.screenWidth - (gp.worldWidth - gp.player.worldX) + (gp.tileSize / 2);
        }

        int bottomOffset = gp.screenHeight - gp.player.screenY;
        if (bottomOffset > gp.worldHeight - gp.player.worldY) {
            centerY = gp.screenHeight - (gp.worldHeight - gp.player.worldY) + (gp.tileSize / 2);
        }

        // Chuyển màu tối dần theo từng fraction.
        Color[] color = new Color[12];

        // Tỷ lệ khoảng cách từ tâm đến rìa vòng tròn ánh sáng.
        float[] fraction = new float[12];

        for (int i = 0; i < 12; i++) {
            fraction[i] = i / 11.0f;
            float alpha = Math.min(255, i * 25);
            color[i] = new Color(0, 0, 0, (int)alpha);
        }

        RadialGradientPaint gPaint = new RadialGradientPaint(
                centerX,
                centerY,
                lightRadius / 2,
                fraction,
                color
        );

        g2.setPaint(gPaint);

        // Vẽ hình chữ nhật phủ toàn màn hình bằng hiệu ứng ánh sáng vừa tạo.
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Giải phóng đồ họa.
        g2.dispose();
    }
}