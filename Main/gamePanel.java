package Main;

import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class gamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS:

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler kH = new KeyHandler();
    Thread gameThread;

    //Set player's default position.
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public gamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
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
        if(kH.upPressed == true) {
            playerY -= playerSpeed;
        }
        else if(kH.downPressed == true) {
            playerY += playerSpeed;
        }
        else if(kH.leftPressed == true) {
            playerX -= playerSpeed;
        }
        else if(kH.rightPressed == true){
            playerX += playerSpeed;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        g2.setColor(Color.WHITE);

        g2.fillRect(playerX, playerY, tileSize, tileSize);

        g2.dispose();
    }
}
