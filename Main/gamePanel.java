package Main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class gamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS:

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile  (1 ô gạch)
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;
    public TileManager tileM = new TileManager(this);
    KeyHandler kH = new KeyHandler(this);
    Thread gameThread;
    Player player = new Player(this, kH );

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;

    //UI
    public UI ui = new UI(this);

    public gamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void setupGame() {
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();

            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
        //if(kH.upPressed == true) {
        //            playerY -= playerSpeed;
        //        }
        //        else if(kH.downPressed == true) {
        //            playerY += playerSpeed;
        //        }
        //        else if(kH.leftPressed == true) {
        //            playerX -= playerSpeed;
        //        }
        //        else if(kH.rightPressed == true){
        //            playerX += playerSpeed;
        //        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        }
        // OTHERS
        else {
            // TILE
            tileM.draw(g2);

            // PLAYER
            player.draw(g2);//xoa cai tren thay bang cai nay
        }

        //da xoa cai nay//  g2d.setColor(Color.WHITE);/////can xoa

        //da xoa cai nay//  g2d.fillRect(playerX, playerY, tileSize, tileSize);/////can xoa


        g2.dispose();
    }
}
