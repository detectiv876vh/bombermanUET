package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    gamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed,qPressed;

    public KeyHandler(gamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TILE STATE
        if(gp.gameState == gp.titleState) {

            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                if(gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 1) {
                    gp.gameState = gp.playState;    //Chỉ có 1 map nên sẽ vào map01.
                }
                if(gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        //PLAY STATE
        if(gp.gameState == gp.playState) {
            if(code == KeyEvent.VK_W){
                System.out.println("W");
                upPressed = true;
            }

            if(code == KeyEvent.VK_S){
                System.out.println("S");
                downPressed = true;
            }

            if(code == KeyEvent.VK_A){
                System.out.println("A");
                leftPressed = true;
            }

            if(code == KeyEvent.VK_D){
                System.out.println("D");
                rightPressed = true;
            }

            if(code == KeyEvent.VK_SPACE){
                spacePressed = true;
            }
            if(code == KeyEvent.VK_Q){
                System.out.println("Q");
                qPressed = true;
            }

            if(code == KeyEvent.VK_R) {
                switch(gp.currentMap) {
                    case 0: gp.tileM.loadMap("/maps/map01.txt", 0); break;
                    case 1: gp.tileM.loadMap("/maps/map02.txt", 1); break;
                }

            }
        }

//        if(code == KeyEvent.VK_ESCAPE) {
//            gp.gameState = gp.titleState;
//        }

        //PAUSE STATE
        if(code == KeyEvent.VK_ESCAPE) {
            if(gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            }
            else if(gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            System.out.println("W");
            upPressed = false;
        }

        if(code == KeyEvent.VK_S){
            System.out.println("S");
            downPressed = false;
        }

        if(code == KeyEvent.VK_A){
            System.out.println("A");
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D){
            System.out.println("D");
            rightPressed = false;
        }

        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }

        if(code == KeyEvent.VK_Q){
            System.out.println("Q");
            qPressed = false;
        }
    }
}
