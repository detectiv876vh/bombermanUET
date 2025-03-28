package Main;

import entity.Player;
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
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;
    TileManager tileM = new TileManager(this);
    KeyHandler kH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker checker  = new CollisionChecker(this);
    Player player = new Player(this, kH );// sfsfsfsfsfsfsdfs ddaay nay


    public gamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
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

        Graphics2D g2d = (Graphics2D) g;

        //da xoa cai nay//  g2d.setColor(Color.WHITE);/////can xoa

        //da xoa cai nay//  g2d.fillRect(playerX, playerY, tileSize, tileSize);/////can xoa

        tileM.drawMap(g2d);

        player.draw(g2d);//xoa cai tren thay bang cai nay


        g2d.dispose();
    }
}
