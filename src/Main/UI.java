package Main;

import java.awt.*;

public class UI {

    gamePanel gp;
    Graphics2D g2;
    public int commandNum = 0;

    public UI (gamePanel gp) {

        this.gp = gp;
    }

    public void draw (Graphics2D g2) {
        this.g2 = g2;


        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
    }

    public void drawTitleScreen() {

        //BACKGROUND COLOR
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String text = "Bomberman";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x+5, y+5);

        // NAME GAME COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);  // viết text ở vị trí x worldY.
        if(commandNum == 0) {
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "CONTINUE GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-gp.tileSize, y);
        }
    }

    public void drawPauseScreen() {

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110F));

        // SHADOW
        text = "Pause";
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        // MAIN
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // CONTINUE GAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
        text = "CONTINUE GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        // BACK TO THE TITLE SCREEN
        text = "QUIT";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }

    // căn giữa văn bản theo chiều ngang trong phương thức.
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();  // tính toán độ rộng của văn bản text.
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

}
