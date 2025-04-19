import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class SnakeGamePanel extends JPanel implements ActionListener, KeyListener {
    private static class Segment {
        int col, row;
        Segment(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

    // Game settings
    private final int GRID_SIZE = 25;
    private final int COLS, ROWS;
    
    // Game objects
    private Segment head;
    private LinkedList<Segment> body;
    private Segment apple;
    private final Random rand = new Random();
    
    // Game state
    private int dirX = 1, dirY = 0; // Initialize direction to right
    private boolean isGameOver;
    private Timer gameTimer;
    private final int DELAY = 150; // Fixed speed

    public SnakeGamePanel(int width, int height) {
        this.COLS = width / GRID_SIZE;
        this.ROWS = height / GRID_SIZE;
        
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.DARK_GRAY);
        addKeyListener(this);
        setFocusable(true);
        
        initializeGame();
    }

    private void initializeGame() {
        // Reset all game state
        head = new Segment(COLS / 2, ROWS / 2);
        body = new LinkedList<>();
        spawnApple();
        
        dirX = 1; // Reset direction to right
        dirY = 0;
        isGameOver = false;
        
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(DELAY, this); // Fixed speed on restart
        gameTimer.start();
    }

    private void spawnApple() {
        apple = new Segment(rand.nextInt(COLS), rand.nextInt(ROWS));
        while (isPositionOccupied(apple.col, apple.row)) {
            apple = new Segment(rand.nextInt(COLS), rand.nextInt(ROWS));
        }
    }

    private boolean isPositionOccupied(int col, int row) {
        if (head.col == col && head.row == row) return true;
        for (Segment seg : body) {
            if (seg.col == col && seg.row == row) return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw apple
        g.setColor(Color.RED);
        g.fillOval(apple.col * GRID_SIZE, apple.row * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        
        // Draw snake
        g.setColor(Color.GREEN);
        g.fillRect(head.col * GRID_SIZE, head.row * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        
        for (Segment seg : body) {
            g.fillRect(seg.col * GRID_SIZE, seg.row * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        }
        
        // Game over text
        if (isGameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.BOLD, 20));
            String msg = "Game Over! Score: " + body.size();
            g.drawString(msg, 50, 50);
            g.drawString("Press R to restart", 50, 80);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.PLAIN, 16));
            g.drawString("Score: " + body.size(), 10, 20);
        }
    }

    private void updateGame() {
        if (isGameOver) return;
        
        // Move body segments (add new segment first if apple eaten)
        if (!body.isEmpty()) {
            body.addFirst(new Segment(head.col, head.row));
            if (!checkAppleCollision()) { // Only remove tail if no apple was eaten
                body.removeLast();
            }
        } else if (checkAppleCollision()) {
            body.addFirst(new Segment(head.col, head.row));
        }
        
        // Move head
        head.col += dirX;
        head.row += dirY;
        
        // Check collisions
        checkWallCollision();
        checkSelfCollision();
    }

    private boolean checkAppleCollision() {
        if (head.col == apple.col && head.row == apple.row) {
            spawnApple();
            return true;
        }
        return false;
    }

    private void checkWallCollision() {
        if (head.col < 0 || head.col >= COLS || head.row < 0 || head.row >= ROWS) {
            isGameOver = true;
        }
    }

    private void checkSelfCollision() {
        for (Segment seg : body) {
            if (head.col == seg.col && head.row == seg.row) {
                isGameOver = true;
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (dirY != 1) { dirX = 0; dirY = -1; }
                break;
            case KeyEvent.VK_DOWN:
                if (dirY != -1) { dirX = 0; dirY = 1; }
                break;
            case KeyEvent.VK_LEFT:
                if (dirX != 1) { dirX = -1; dirY = 0; }
                break;
            case KeyEvent.VK_RIGHT:
                if (dirX != -1) { dirX = 1; dirY = 0; }
                break;
            case KeyEvent.VK_R:
                if (isGameOver) initializeGame();
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
