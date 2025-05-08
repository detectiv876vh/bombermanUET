package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    gamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

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
        if (gp.gameState == gp.titleState) {

            if (code == KeyEvent.VK_W) {
                if (gp.ui.commandNum == 1) {
                    gp.ui.commandNum = 0;
                    gp.playSE(4); // Chỉ phát âm thanh khi có thay đổi từ 1 -> 0.
                }
            }
            if (code == KeyEvent.VK_S) {
                if (gp.ui.commandNum == 0) {
                    gp.ui.commandNum = 1;
                    gp.playSE(4); // Chỉ phát âm thanh khi có thay đổi từ 0 -> 1.
                }
            }
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.transitionState;
                    gp.ui.startMapTransition("Level 1");
                } else if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }

        // ESC: Toggle pause
        if (code == KeyEvent.VK_ESCAPE) {
            gp.playSE(4);
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
            return;
        }

        //PAUSE STATE
        else if (gp.gameState == gp.pauseState) {

            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                gp.playSE(4);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                gp.playSE(4);
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 3;
                }
            }

            if (code == KeyEvent.VK_A) {
                if (gp.ui.subState == 0) {
                    if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                        gp.music.volumeScale--;
                        gp.music.checkVolume();
                        gp.playSE(4);
                    }
                    if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                        gp.se.volumeScale--;
                        gp.playSE(4);
                    }
                }
            }

            if (code == KeyEvent.VK_D) {
                if (gp.ui.subState == 0) {
                    if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                        gp.music.volumeScale++;
                        gp.music.checkVolume();
                        gp.playSE(4);
                    }
                    if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                        gp.se.volumeScale++;
                        gp.playSE(4);
                    }
                }
            }

            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNum == 3) {
                    System.exit(0);
                }
            }
        }

        //PLAY STATE
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }

            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }

            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }

            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }

            if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }

            if (code == KeyEvent.VK_R) {
                switch (gp.currentMap) {
                    case 0:
                        gp.tileM.loadMap("/maps/map00.txt", 0);
                        break;
                    case 1:
                        gp.tileM.loadMap("/maps/map01.txt", 1);
                        break;
                }

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}
