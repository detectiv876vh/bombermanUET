package Main;

import manager.DrawManager;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Bomberman");

        gamePanel gamePanel = new gamePanel();
        DrawManager drawManager = new DrawManager(gamePanel);  // Truyền gp vào DrawManager
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
