package Main;

import entity.Entity;
import entity.Player;
import manager.BombManager;
import manager.DrawManager;
import manager.TileManager;
import object.Bomb;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class gamePanel extends JPanel implements Runnable {

    // TILE STATE
    public MouseHandler mouseH = new MouseHandler();

    //SCREEN SETTINGS
    public final int originalTileSize = 16;
    public final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile  (1 ô gạch)
    public final int maxScreenCol = 16;     // Chiều dài (đơn vị số block) theo phương Ox của screen
    public final int maxScreenRow = 12;     // Chiều rộng (đơn vị số block) theo phương Oy của
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels: Chiều dài (đơn vị pixel)
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels: Chiều rộng (đơn vị pixel)

    // WORLD SETTINGS
    public final int maxWorldCol = 30;
    public final int maxWorldRow = 30;
    public int WIDTH = (tileSize * scale) * maxWorldCol;
    public int HEIGHT = (tileSize * scale) * maxWorldRow;
    public final int worldWidth = tileSize * maxWorldCol;   // Chiều dài bản đồ
    public final int worldHeight = tileSize * maxWorldRow;  // Chiều rộng bản đồ
    public final int maxMap = 10; // Tổng số map
    public int currentMap = 0;

    //FPS
    public int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler kH = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound();
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
    public BombManager bombManager = new BombManager(this, player);
    public DrawManager drawManager = new DrawManager(this);

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    private Graphics g;
    public Graphics2D g2d;

    public gamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
    }

    public void setupGame() {
        gameState = titleState;
        aSetter.setObject();
        playMusic(0);
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
            bombManager.handleBombPlacement();
            bombManager.update();


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

            // Cập nhật bomb - THÊM PHẦN NÀY
            for (int i = 0; i < bombManager.bombList[currentMap].size(); i++) {
                Bomb bomb = bombManager.bombList[currentMap].get(i);
                if (bomb != null && bomb.alive) {
                    bomb.update();
                } else {
                    bombManager.bombList[currentMap].remove(i);
                    i--;
                }
            }
        }

        if(gameState == pauseState) {}


    }

    public void paintComponent(Graphics g) {
        this.g = g;

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(gameState == pauseState) {
            tileM.draw(g2);
            player.draw(g2);

            entityList.add(player);
            for (int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            for (Entity bomb : bombManager.bombList[currentMap]) {
                if (bomb != null && bomb.alive) {
                    bomb.draw(g2);
                }
            }

            ui.draw(g2);
        }

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

            for (Entity bomb : bombManager.bombList[currentMap]) {
                if (bomb != null && bomb.alive) {
                    bomb.draw(g2);
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

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {

        se.setFile(i);
        se.play();
    }

    public class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if(gameState == titleState) {
                int x = e.getX();   // tọa độ con chuột click.
                int y = e.getY();

                int menuY = tileSize * 7;
                int menuItemHeight = tileSize;

                if(y >= menuY && y < menuY + menuItemHeight) {
                    ui.commandNum=0;
                    gameState = playState;
                }
                else if(y >= menuY + menuItemHeight && y < menuY + menuItemHeight*2) {
                    ui.commandNum=1;
                }
                else if(y >= menuY + menuItemHeight*2 && y < menuY + menuItemHeight*3) {
                    ui.commandNum=2;
                    System.exit(0);
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            if(gameState == titleState) {
                int y = e.getY();   // tọa độ con trỏ chuột đang ở.

                int menuY = tileSize * 7;
                int menuItemHeight = tileSize;
                int newHover = -1;

                if(y >= menuY && y < menuY + menuItemHeight) {
                    newHover = 0;
                }
                else if(y >= menuY + menuItemHeight && y < menuY + menuItemHeight*2) {
                    newHover = 1;
                }
                else if(y >= menuY + menuItemHeight*2 && y < menuY + menuItemHeight*3) {
                    newHover = 2;
                }

                if (newHover != -1 && newHover != ui.lastHovered) {
                    ui.lastHovered = newHover;
                    playSE(4);
                }

                if (newHover != -1) {
                    ui.commandNum = newHover;
                }
            }
        }
    }
}
