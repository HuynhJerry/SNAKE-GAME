import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Snakegame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //gamelogic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    Snakegame(int boardWidth,int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(0,0);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(0,0);
        random = new Random();
        placeFood();
        placeHead();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }
    public void resetGame() {
        snakeHead = new Tile(5,5);
        snakeBody.clear();
        placeFood();
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        gameLoop.start();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        //Grid
        for(int i = 0;i < boardWidth/tileSize; i++) {
            //(x1,y1,x2,y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth,i*tileSize);
        }

        //food
        g.setColor(Color.red);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize,tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize,tileSize,true);
        //snake head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y *tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y *tileSize, tileSize, tileSize,true);

        //snake body
        for (int i = 0; i < snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize,true);

        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()),tileSize - 16, tileSize);
            g.drawString("Press num [0] to start over", tileSize + 100, tileSize);
        }
        else{
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }

        
    }

    public void placeHead(){
        snakeHead.x = random.nextInt(boardWidth/tileSize); //600/25 = 24
        snakeHead.y = random.nextInt(boardHeight/tileSize);
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize); //600/25 = 24
        food.y = random.nextInt(boardHeight/tileSize);

    }

    public void move(){
        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        //snakebody
        for(int i = snakeBody.size()-1; i >=0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //snakehead
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i< snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collide with the snake head
            if (collision(snakeHead, snakePart)){
                gameOver=true;
            }
        }
        // if (snakeHead.x*tileSize <0 || snakeHead.x*tileSize>boardWidth || 
        //     snakeHead.y*tileSize <0 || snakeHead.y*tileSize > boardHeight){
        //     gameOver = true;
        // }
        // if (snakeHead.x < 0) {
        //     snakeHead.x = boardWidth/tileSize;
        // }
        // if (snakeHead.x*tileSize > boardWidth) {
        //     snakeHead.x = 0;
        // }
        // if (snakeHead.y*tileSize < 0) {
        //     snakeHead.y = boardHeight/tileSize;
        // }
        // if (snakeHead.y*tileSize > boardHeight) {
        //     snakeHead.y = 0;
        // }
        if (snakeHead.x < 0) {
            snakeHead.x = (boardWidth / tileSize) - 1; // Wrap to the right edge
        }
        if (snakeHead.x >= (boardWidth / tileSize)) {
            snakeHead.x = 0; // Wrap to the left edge
        }
        if (snakeHead.y < 0) {
            snakeHead.y = (boardWidth / tileSize) - 1; // Wrap to the bottom edge
        }
        if (snakeHead.y >= (boardHeight / tileSize)) {
            snakeHead.y = 0; // Wrap to the top edge
        }
        
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (!gameOver) {
            move();
            repaint();
        } else {
            gameLoop.stop(); // Stop the game loop if game is over
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = +1;            
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = +1;
            velocityY = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != +1) {
            velocityX = -1;
            velocityY = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_0 && gameOver) {
            resetGame(); // Reset the game state
        }
    }

    //do not need
    @Override
    public void keyTyped(KeyEvent e) {}

    

    @Override
    public void keyReleased(KeyEvent e) {}
}
