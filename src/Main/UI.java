package Main;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;

import static javax.swing.plaf.basic.BasicGraphicsUtils.drawString;

public class UI {

    gamePanel gp;
    Graphics2D g2;
    BufferedImage heart_full, heart_half, heart_blank;
    public int commandNum = 0;
    Font arial_40;



    public UI (gamePanel gp) {

        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        // Load hình từ object
        OBJ_Heart heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    public void draw (Graphics2D g2) {
        this.g2 = g2;
        //PLAYER STATE
        if(gp.gameState == gp.playState) {
            drawPlayerLife();
        }
        //SET FONT
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawString("Key = " + gp.player.hasKey, 50,50);       //viet so key tren map o 50 50
        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        //CREATE HUD OBJECT
        SuperObject heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
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



        //PAUSE STATE
        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        //DIALOGUE STATE
//        if(gp.gameState == gp.dialogueState) {
//            drawPlayerLife();
//            drawDialogueScreen();
//        }
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
    public void drawPlayerLife() {
        int x = gp.tileSize*2;
        int y = gp.tileSize*2;
        int i = 0;

        while(i< gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }
    }
    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        g2.setColor(Color.white);

        String text = "PAUSE";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    // căn giữa văn bản theo chiều ngang trong phương thức.
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();  // tính toán độ rộng của văn bản text.
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

}
