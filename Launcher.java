import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        int windowSize = 600;

        JFrame gameFrame = new JFrame("Snake Game");
        gameFrame.setSize(windowSize, windowSize);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);

        SnakeBoard snakeBoard = new SnakeBoard(windowSize, windowSize);
        gameFrame.add(snakeBoard);
        gameFrame.pack();
        snakeBoard.requestFocus();
    }
}
