package Main;

import entity.Entity;
import entity.Player;
import entity.Projectile;
import manager.BombManager;
import manager.ChemManager;
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
    public int currentMap = 0;

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
    public ArrayList<Entity>[] projectileList = new ArrayList[maxMap];
    public Entity monster[] = new Entity[20];
    public Entity npc[] = new Entity[10];           // so  npc co the co
    public Entity obj[][] = new Entity[maxMap][100];   // so item co the xuat hien tai o do
    public BombManager bombManager = new BombManager(this, player);
    public ChemManager chemManager = new ChemManager(this, player);

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int chemState = 3;
    public final int gameOverState = 6;
    public Graphics g;


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

        for (int i = 0; i < maxMap; i++) {
            if (projectileList[i] == null) {
                projectileList[i] = new ArrayList<>();
            }
        }
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
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
            chemManager.handleChem();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) npc[i].update();
            }
            // UPDATE BOMB
            for (int i = 0; i < obj[currentMap].length; i++) {
                if (obj[currentMap][i] != null && obj[currentMap][i].name.equals("bombItem")) {
                    obj[currentMap][i].update();
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    } else if (!monster[i].alive) {
                        monster[i] = null;
                    }
                }
            }

            for (int i = 0; i < projectileList[currentMap].size(); i++) {
                Entity p = projectileList[currentMap].get(i);
                if (p != null) {
                    if (p.alive) {
                        p.update();
                        // THÊM CODE KIỂM TRA ĐẠN TRÚNG QUÁI VẬT TẠI ĐÂY
                        for (int j = 0; j < monster.length; j++) {
                            if (monster[j] != null && monster[j].alive) {
                                if (p.getHitbox().intersects(monster[j].getHitbox())) {
                                    player.damageMonster(j);
                                    p.alive = false; // Hủy đạn sau khi trúng
                                }
                            }
                        }
                    } else {
                        projectileList[currentMap].remove(i);
                        i--;
                    }
                }
            }
        }
        player.attackCooldown = Math.max(0, player.attackCooldown - 1);

        if(gameState == pauseState) {}

    }
    public void paintComponent(Graphics g) {
        this.g = g;

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            ui.draw(g2);
        } else if (gameState == playState) {
            tileM.draw(g2);

            for (int i = 0; i < obj[currentMap].length; i++) {
                if (obj[currentMap][i] != null) {
                    obj[currentMap][i].draw(g2);
                }
            }

            player.draw(g2);

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            entityList.add(player);

            for (int i = 0; i < projectileList[currentMap].size(); i++) {
                if (projectileList[currentMap].get(i) != null) {
                    entityList.add(projectileList[currentMap].get(i));
                }
            }

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) entityList.add(npc[i]);
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) entityList.add(monster[i]);
            }
            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            entityList.clear();
        }

        ui.draw(g2);
        g2.dispose();
    }
}
