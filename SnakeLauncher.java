import javax.swing.*;

public class SnakeLauncher {
    public static void main(String[] args) {
        int gameWidth = 600;
        int gameHeight = 600;
        
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        SnakeGamePanel gamePanel = new SnakeGamePanel(gameWidth, gameHeight);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
