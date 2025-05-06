package Main;

import entity.Entity;
import object.OBJ_Heart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static javax.swing.plaf.basic.BasicGraphicsUtils.drawString;

public class UI {

    gamePanel gp;
    Graphics2D g2;

    BufferedImage heart_full, heart_half, heart_blank;

    // Standby Screen
    BufferedImage background;
    Image backgroundFixed;
    BufferedImage playButton;
    Image playButtonPress;
    BufferedImage quitButton;
    Image quitButtonPress;

    public int commandNum = 0;
    Font theleahFat;
    public int subState= 0;
    public int lastHovered = -1; // Lưu trạng thái hover trước đó

    // MAP TRANSITION
    public boolean showTransition = false;
    public int transitionTimer = 0;
    String transitionText = "";
    final int TRANSITION_DURATION = 120;

    public UI (gamePanel gp) {

        this.gp = gp;
        theleahFat = new Font("Arial", Font.PLAIN, 40);

        try {
            InputStream is = getClass().getResourceAsStream("/font/ThaleahFat.ttf");
            theleahFat = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load hình từ object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

        // BACKGROUND
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/standbyscreen/background.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        backgroundFixed = background.getScaledInstance(gp.screenWidth, gp.screenHeight, Image.SCALE_SMOOTH);

        // BUTTON
        try {
            playButton = ImageIO.read(getClass().getResourceAsStream("/standbyscreen/play.png"));
            playButtonPress = ImageIO.read(getClass().getResourceAsStream("/standbyscreen/playPress.png"));

            quitButton = ImageIO.read(getClass().getResourceAsStream("/standbyscreen/quit.png"));
            quitButtonPress = ImageIO.read(getClass().getResourceAsStream("/standbyscreen/quitPress.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw (Graphics2D g2) {
        this.g2 = g2;
        //SET FONT
        g2.setFont(theleahFat);
        g2.setColor(Color.white);
//        g2.drawString("Key = " + gp.player.hasKey, 50,50);       //viet so key tren map o 50 50

        // MAP TRANSITION
        if (showTransition) {
            drawMapTransition();
            return;
        }

        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
//        //draw bombs
//        //GAME OVERSTATE
//        if(gp.gameState == gp.gameOverState) {
//            drawGameOverScreen();
//        }

        //PLAYER STATE
        if(gp.gameState == gp.playState) {
            drawPlayerLife();
//            drawMapTransition();
        }
        //ATTACK STATE
        if(gp.gameState == gp.chemState) {
        }

        // GAME OVER STATE
        if (gp.gameState == gp.gameOverState) {
            drawMapTransition();
        }

        //PAUSE STATE
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    public void drawTitleScreen() {

//        //BACKGROUND COLOR
//        g2.setColor(Color.BLACK);
//        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // BACKGROUND IMAGE
        g2.drawImage(backgroundFixed, 0, 0, null);

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

        // NAME GAME COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

//        //MENU
//        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
//
//        text = "NEW GAME";
//        x = getXforCenteredText(text);
//        y += gp.tileSize * 4;
//
//        if(commandNum == 0) {
//            g2.setColor(Color.yellow);
//            g2.drawString(">", x-gp.tileSize, y);
//        } else {
//            g2.setColor(Color.white);
//        }
//        g2.drawString(text, x, y);  // viết text ở vị trí x worldY.
//
//        text = "CONTINUE GAME";
//        x = getXforCenteredText(text);
//        y += gp.tileSize;
//
//        if(commandNum == 1) {
//            g2.setColor(Color.yellow);
//            g2.drawString(">", x-gp.tileSize, y);
//        } else {
//            g2.setColor(Color.white);
//        }
//        g2.drawString(text, x, y);
//
//        text = "QUIT";
//        x = getXforCenteredText(text);
//        y += gp.tileSize;
//
//        if(commandNum == 2) {
//            g2.setColor(Color.yellow);
//            g2.drawString(">", x-gp.tileSize, y);
//        } else {
//            g2.setColor(Color.white);
//        }
//        g2.drawString(text, x, y);
        int buttonWidth = playButton.getWidth();
        int buttonHeight = playButton.getHeight();
        int spacing = gp.tileSize - 20; // khoảng cách giữa các nút.

// Tính tổng chiều cao của cả menu.
        int totalHeight = buttonHeight * 2 + spacing;
        int yStart = gp.screenHeight / 2 - totalHeight / 2 + 80;
        x = gp.screenWidth / 2 - buttonWidth / 2;
        y = yStart;

// PLAY - sử dụng mouseHandler để kiểm tra trạng thái nhấn.
        if (gp.mouseH.playButtonPressed) {
            g2.drawImage(playButtonPress, x, y, null);
        } else {
            g2.drawImage(playButton, x, y, null);
        }

// QUIT
        y += buttonHeight + spacing;
        if (gp.mouseH.quitButtonPressed) {
            g2.drawImage(quitButtonPress, x, y, null);
        } else {
            g2.drawImage(quitButton, x, y, null);
        }
    }

    public void drawPlayerLife() {
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // draw blank heart
        while(i<gp.player.maxLife/2) {
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x += gp.tileSize;
        }
        // reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;
        //  draw current life
        while(i < gp.player.life) {
            g2.drawImage(heart_half,x,y,null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

//    public void drawGameOverScreen() {
//
//    }

    public void drawPauseScreen() {

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110F));

        // SHADOW
        text = "Pause";
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        // MAIN
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // CONTINUE GAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
        text = "CONTINUE GAME";
        x = getXforCenteredText(text);
        int leftAlignedX = x;
        y += (int) (gp.tileSize * 2.5);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        // MUSIC CONTROL
        text = "MUSIC";
        int labelX = leftAlignedX;
        y += (int) (gp.tileSize * 1.5);
        g2.drawString(text, labelX, y);
        if (commandNum == 1) {
            g2.drawString(">", labelX - 40, y);
        }

        // MUSIC VOLUME BOX
        int boxX = labelX + 200;
        g2.drawRect(boxX, y - 40, 120, 40);     // 120 / 5 = 24;
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(boxX, y - 40, volumeWidth, 40);

        // SOUND EFFECT CONTROL
        text = "SE";
        labelX = leftAlignedX;
        y += (int) (gp.tileSize * 1.5);
        g2.drawString(text, labelX, y);
        if (commandNum == 2) {
            g2.drawString(">", labelX-40, y);
        }

        // SE VOLUME BOX
        boxX = labelX + 200;
        g2.drawRect(boxX, y - 40, 120, 40);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(boxX, y - 40, volumeWidth, 40);

        // BACK TO THE TITLE SCREEN
        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if(commandNum == 3) {
            g2.drawString(">", x-40, y);
        }


        switch (subState) {
            case 0: break;
            case 1: break;
            case 2: break;
        }
    }

    public void drawMapTransition() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(theleahFat.deriveFont(Font.BOLD,48F));
        g2.setColor(Color.white);

        transitionText = "Level " + (gp.currentMap + 1);
        int x = getXforCenteredText(transitionText);
        int y = gp.screenHeight / 2;
        g2.drawString(transitionText, x, y);

        transitionTimer++;
        if(transitionTimer >= TRANSITION_DURATION) {
            showTransition = false;
            transitionTimer = 0;

            if (gp.gameState == gp.gameOverState) {
                if (gp.currentMap + 1 < gp.maxMap) {
                    gp.changeMap(gp.currentMap + 1);
                }
//                else {
//                    // Nếu hết level, quay lại màn hình title
//                    gp.gameState = gp.titleState;
//                    gp.ui.commandNum = 0;
//                }
            } else {
                // Sau khi hiển thị level, chuyển sang trạng thái chơi
                gp.gameState = gp.playState;
            }
        }

    }

    // căn giữa văn bản theo chiều ngang trong phương thức.
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();  // tính toán độ rộng của văn bản text.
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    public void startMapTransition(String mapName) {
        this.showTransition = true;
        this.transitionTimer = 0;
        this.transitionText = mapName;
    }

}
