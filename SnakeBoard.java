import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeBoard extends JPanel implements ActionListener, KeyListener {

    private class Segment {
        int xCoord;
        int yCoord;

        Segment(int x, int y) {
            this.xCoord = x;
            this.yCoord = y;
        }
    }

    private int panelWidth, panelHeight;
    private final int gridSize = 25;

    private Segment snakeHead;
    private ArrayList<Segment> snakeTail;

    private Segment foodItem;
    private Random rand;

    private Timer timer;
    private int moveX, moveY;
    private boolean isGameOver = false;

    public SnakeBoard(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Segment(0, 0);
        snakeTail = new ArrayList<>();
        foodItem = new Segment(0, 0);
        rand = new Random();

        spawnFood();
        spawnHead();

        moveX = 0;
        moveY = 0;

        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnHead() {
        snakeHead.xCoord = rand.nextInt(panelWidth / gridSize);
        snakeHead.yCoord = rand.nextInt(panelHeight / gridSize);
    }

    private void spawnFood() {
        foodItem.xCoord = rand.nextInt(panelWidth / gridSize);
        foodItem.yCoord = rand.nextInt(panelHeight / gridSize);
    }

    private void restartGame() {
        snakeHead = new Segment(5, 5);
        snakeTail.clear();
        moveX = 0;
        moveY = 0;
        isGameOver = false;
        spawnFood();
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        g.setColor(Color.RED);
        g.fill3DRect(foodItem.xCoord * gridSize, foodItem.yCoord * gridSize, gridSize, gridSize, true);

        g.setColor(Color.GREEN);
        g.fill3DRect(snakeHead.xCoord * gridSize, snakeHead.yCoord * gridSize, gridSize, gridSize, true);

        for (Segment part : snakeTail) {
            g.fill3DRect(part.xCoord * gridSize, part.yCoord * gridSize, gridSize, gridSize, true);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));

        if (isGameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over: " + snakeTail.size(), gridSize - 16, gridSize);
            g.drawString("Press [R] to Restart", gridSize + 100, gridSize);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Score: " + snakeTail.size(), gridSize - 16, gridSize);
        }
    }

    private boolean isColliding(Segment a, Segment b) {
        return a.xCoord == b.xCoord && a.yCoord == b.yCoord;
    }

    private void moveSnake(int dx, int dy) {
        if (isColliding(snakeHead, foodItem)) {
            snakeTail.add(new Segment(foodItem.xCoord, foodItem.yCoord));
            spawnFood();
        }

        for (int i = snakeTail.size() - 1; i >= 0; i--) {
            Segment part = snakeTail.get(i);
            if (i == 0) {
                part.xCoord = snakeHead.xCoord;
                part.yCoord = snakeHead.yCoord;
            } else {
                Segment prev = snakeTail.get(i - 1);
                part.xCoord = prev.xCoord;
                part.yCoord = prev.yCoord;
            }
        }

        snakeHead.xCoord += dx;
        snakeHead.yCoord += dy;

        for (Segment part : snakeTail) {
            if (isColliding(snakeHead, part)) {
                isGameOver = true;
            }
        }

        if (snakeHead.xCoord < 0) snakeHead.xCoord = (panelWidth / gridSize) - 1;
        if (snakeHead.xCoord >= (panelWidth / gridSize)) snakeHead.xCoord = 0;
        if (snakeHead.yCoord < 0) snakeHead.yCoord = (panelHeight / gridSize) - 1;
        if (snakeHead.yCoord >= (panelHeight / gridSize)) snakeHead.yCoord = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            moveSnake(moveX, moveY);
            repaint();
        } else {
            timer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (moveY != 1) { moveX = 0; moveY = -1; }
                break;
            case KeyEvent.VK_DOWN:
                if (moveY != -1) { moveX = 0; moveY = 1; }
                break;
            case KeyEvent.VK_LEFT:
                if (moveX != 1) { moveX = -1; moveY = 0; }
                break;
            case KeyEvent.VK_RIGHT:
                if (moveX != -1) { moveX = 1; moveY = 0; }
                break;
            case KeyEvent.VK_R:
                if (isGameOver) restartGame();
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
