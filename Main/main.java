package Main;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Bomberman");

        gamePanel GamePanel = new gamePanel();
        window.add(GamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        GamePanel.startGameThread();
    }
}
