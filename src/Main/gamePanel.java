package Main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class gamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile  (1 ô gạch)
    public final int maxScreenCol = 16;     // Chiều dài (đơn vị số block) theo phương Ox của screen
    public final int maxScreenRow = 12;     // Chiều rộng (đơn vị số block) theo phương Oy của
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels: Chiều dài (đơn vị pixel)
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels: Chiều rộng (đơn vị pixel)

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;   // Chiều dài bản đồ
    public final int worldHeight = tileSize * maxWorldRow;  // Chiều rộng bản đồ
    public final int maxMap = 10; // Tổng số map
    public int currentMap = 1;

    //FPS
    public int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler kH = new KeyHandler(this);
    public UI ui = new UI(this);
    Thread gameThread;
    public AssetSetter aSetter = new AssetSetter(this);
    public CollisionChecker checker  = new CollisionChecker(this);
    public EventHandler eHandler = new EventHandler(this);

    //ENTITIES AND OBJECTS
    public Player player = new Player(this, kH);
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public Entity obj[][] = new Entity[maxMap][100];   // so item co the xuat hien tai o do

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    private Graphics g;

    public gamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void setupGame() {
        gameState = titleState;
        aSetter.setObject();
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
        if (gameState == playState) {

            player.update();

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    } else {
                        projectileList.remove(i);
                        i--;
                    }
                }
            }

        }

        if(gameState == pauseState) {}


    }

    public void paintComponent(Graphics g) {
        this.g = g;

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        }
        // OTHERS
        else if(gameState == playState) {
            // TILE
            tileM.draw(g2);
            //OBJECT
            for(int i = 0; i < obj.length; i++) {
                if(obj[currentMap][i] != null) {
                    obj[currentMap][i].draw(g2);
                }
            }
            // PLAYER
            player.draw(g2);//xoa cai tren thay bang cai nay

            entityList.add(player);
            for (int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            // empty the list
            entityList.clear();
        }

        ui.draw(g2);
        g2.dispose();

    }
}
