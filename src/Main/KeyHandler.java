package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    gamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

    public KeyHandler(gamePanel gp) {
        this.gp=gp;
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
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }

        if(code == KeyEvent.VK_S){
            downPressed = true;
        }

        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }

        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }

        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }

        if(code == KeyEvent.VK_S){
            downPressed = false;
        }

        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }

        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }
    }
}
