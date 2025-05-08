package Main;

import AI.PathFinder;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import environment.Lighting;
import manager.BombManager;
import manager.DrawManager;
import manager.TileManager;
import object.Bomb;
//import manager.ChemManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Arrays;

public class gamePanel extends JPanel implements Runnable {

    // TILE STATE (MOUSE)
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
    public final int maxWorldRow = 25;
    public int WIDTH = (tileSize * scale) * maxWorldCol;
    public int HEIGHT = (tileSize * scale) * maxWorldRow;
    public final int worldWidth = tileSize * maxWorldCol;   // Chiều dài bản đồ
    public final int worldHeight = tileSize * maxWorldRow;  // Chiều rộng bản đồ
    public final int maxMap = 10; // Tổng số map
    public int currentMap = 0;
    public boolean[] doorSpawned = new boolean[maxMap];


    //FPS
    public int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler kH = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound();
    public UI ui = new UI(this);
    public EnvironmentManager eManager = new EnvironmentManager(this);

    Thread gameThread;
    public AssetSetter aSetter = new AssetSetter(this);
    public CollisionChecker checker  = new CollisionChecker(this);
    public EventHandler eHandler = new EventHandler(this);
    public PathFinder pFinder = new PathFinder(this);

    //ENTITIES AND OBJECTS
    public Player player = new Player(this, kH);
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public Entity monster[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][10];           // so  npc co the co
    public Entity obj[][] = new Entity[maxMap][100];   // so item co the xuat hien tai o do
    public BombManager bombManager = new BombManager(this, player);
    public DrawManager drawManager = new DrawManager(this);
//    public ChemManager chemManager = new ChemManager(this, player);

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int chemState = 3;
    public final int transitionState = 5;
    public final int gameOverState = 6;
    public Graphics g;
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
        Arrays.fill(doorSpawned, false);

    }

    public void setupGame() {
        gameState = titleState;
        currentMap = 0;


        aSetter.setMonster00();
        aSetter.setMonster01();
        playMusic(0);
        eManager.setup();
//        aSetter.setBoss();
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

        if (ui.showTransition) {
            return;
        }

//        if (player.life <= 0) {
//            gameState = gameOverState;
//            ui.showTransition = true;
//            ui.transitionTimer = 0;
//            return;
//        }

        if (gameState == playState) {

            player.update();
            bombManager.handleBombPlacement();
            bombManager.update();
            checkDoorSpawnCondition();

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
            for (int i = 0; i < monster[currentMap].length; i++) {
                Entity m = monster[currentMap][i];
                if (m != null) {
                    // luôn cho phép update(), để dyingAnimation() có thể chạy
                    m.update();

                    // sau khi update, nếu đã kết thúc và alive==false thì mới remove
                    if (!m.alive) {
                        monster[currentMap][i] = null;
                    }
                }
            }



            for (int i = 0; i < bombManager.bombList[currentMap].size(); i++) {
                Bomb bomb = bombManager.bombList[currentMap].get(i);
                if (bomb != null && bomb.alive) {
                    bomb.update();
                } else {
                    bombManager.bombList[currentMap].remove(i);
                    player.hasBomb++;
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

        if (ui.showTransition) {
            ui.draw(g2);
            g2.dispose();
            return;
        }

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
            // UI
            ui.draw(g2);
        }

        // TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        }

        // GAMEOVER
        else if(gameState == gameOverState) {
            ui.draw(g2);
        }

        // OTHERS
        else if(gameState == playState) {
            // TILE
            tileM.draw(g2);
            //OBJECT
            for(int i = 0; i < obj[currentMap].length; i++) {
                if(obj[currentMap][i] != null) {
                    obj[currentMap][i].draw(g2);
                }
            }
            // PLAYER
            player.draw(g2);//xoa cai tren thay bang cai nay

            entityList.add(player);
            for (int i = 0; i < monster[currentMap].length; i++) {
                if (monster[currentMap][i] != null) entityList.add(monster[currentMap][i]);
            }

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
            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            // empty the list
            entityList.clear();
        }

        // ENVIRONMENT
        eManager.draw(g2);

        // UI
        ui.draw(g2);

        g2.dispose();

    }

    public void changeMap(int mapIndex) {
        gameState = transitionState;
        int targetMap = mapIndex;

        ui.showTransition = true;
        ui.transitionTimer = 0;
        ui.transitionText = "Level " + (targetMap + 1);

        loadMap(targetMap);
    }

    public void completeLevel() {
        int nextMap = currentMap + 1;

        if (nextMap < maxMap) {
            changeMap(nextMap);
        } else {
            gameState = gameOverState;
            ui.showTransition = true;
            ui.transitionTimer = 0;
            ui.commandNum = 0;
        }
    }

    public void loadMap(int mapIndex) {

        player.setDefaultValues();

        clearMapEntities();


        aSetter.setMonster00();
        aSetter.setMonster01();


    }

    public void clearMapEntities() {
        projectileList.clear();
        entityList.clear();

        if (bombManager.bombList[currentMap] != null) {
            bombManager.bombList[currentMap].clear();
        }
    }

    public boolean allMonstersDefeated(int mapNum) {
        for (int i = 0; i < monster[mapNum].length; i++) {
            if (monster[mapNum][i] != null && monster[mapNum][i].alive) {
                return false;
            }
        }
        return true;
    }

    public void checkDoorSpawnCondition() {
        for (int mapNum = 0; mapNum < monster.length; mapNum++) {
            if (!doorSpawned[mapNum] && allMonstersDefeated(mapNum)) {
                aSetter.setDoor(mapNum);
                doorSpawned[mapNum] = true;
            }
        }
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
        boolean playButtonPressed = false;
        boolean quitButtonPressed = false;

        public void mousePressed(MouseEvent e) {
            if (gameState == titleState) {
                int x = e.getX();
                int y = e.getY();

//                // Debug information
//                System.out.println("Mouse pressed at: " + x + ", " + y);

                // Kiểm tra giữ click vào nút Play.
                if (x >= 281 && x <= 487 && y >= 353 && y <= 430) {
                    System.out.println("Play button pressed!");

                    playButtonPressed = true;
                    ui.commandNum = 0;
                }

                // Kiểm tra click vào nút Quit.
                if (x >= 281 && x <= 487 && y >= 452 && y <= 525) {
                    System.out.println("Quit button pressed!");

                    quitButtonPressed = true;
                    ui.commandNum = 2;
                }
                repaint();  // Yêu cầu vẽ lại để hiển thị trạng thái nhấn.
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (gameState == titleState) {
                int x = e.getX();
                int y = e.getY();

                // Kiểm tra thả chuột trên nút Play.
                if (playButtonPressed && x >= 281 && x <= 487 && y >= 353 && y <= 430) {
                    gameState = transitionState;
                    ui.startMapTransition("Level 1");
                    playSE(4);
                }

                // Kiểm tra thả chuột trên nút Quit.
                if (quitButtonPressed && (x >= 281 && x <= 487 && y >= 452 && y <= 525)) {
                    playSE(4);
                    System.exit(0);
                }

                // Reset trạng thái nhấn.
                playButtonPressed = false;
                quitButtonPressed = false;
                repaint();
            }
        }
    }
}
