import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class SnakeGame extends JPanel {

    private final List<Point> snakeSegments = new ArrayList<>(); //List that hold all snake segments
    private final int blockSize = 40; // Size of each block in pixels
    private int dx = 1, dy = 0; //The directions of the snake movement
    private final Timer timer = new Timer(200, e -> tick()); //Timer that controls game loop (game ticks)
    private final int rows = 18; // Number of rows of blocks + wall
    private final int cols = 18; // Number of columns of blocks + wall
    private final int parts = 4; // snake parts starting lenght
    private int appleX = 13; // X position of the apple
    private int appleY = 8; // Y position of the apple
    private boolean gameStarted = false;
    private boolean gameOver = false;
    int menuX = blockSize * cols / 2 - 150;
    int menuY = blockSize * rows / 2 - 50;

    public SnakeGame() {
        setPreferredSize(new Dimension(blockSize * cols, blockSize * rows)); //Setting the size of the window

        for (int section = 0; section < parts; section++) snakeSegments.add(new Point(4 + section, 8)); //Adding the starting snake to the list

        setFocusable(true); //Focuses on window to listen for key events
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameStarted || gameOver) {
                    gameStarted = true;
                    gameOver = false;
                    timer.start();
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> {gameStarted = false; gameOver = false;}
                    case KeyEvent.VK_UP -> {if (dy == 0) {dx = 0; dy = -1;}}
                    case KeyEvent.VK_DOWN -> {if (dy == 0) {dx = 0; dy = 1;}}
                    case KeyEvent.VK_LEFT -> {if (dx == 0) {dx = -1; dy = 0;}}
                    case KeyEvent.VK_RIGHT -> {if (dx == 0) {dx = 1; dy = 0;}}
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawBoard(g2d);
        drawApple(g2d, appleX, appleY);
        drawSnake(g2d);
        drawMenu(g2d);
    }

    private void drawBoard(Graphics2D g2d) {
         for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * blockSize, y = row * blockSize;
                int half = blockSize / 2;
                boolean right = col == cols - 1, left = col == 0;
                boolean top = row == 0, bottom = row == rows - 1;

                int rx = (left) ? x+half : x;
                int ry = (top) ? y+half : y;
                int rw = (right || left) ? half : blockSize;
                int rh = (top || bottom) ? half : blockSize;

                if ((right || left || top || bottom)) {
                    g2d.setColor(new Color(0, 128, 0));
                } else if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(80, 200, 120));
                } else {
                    g2d.setColor(new Color(152, 255, 152));
                }
                
                g2d.fillRect(rx, ry, rw, rh);
            }
        }
    }

    private void drawMenu(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(menuX, menuY, 300, 100);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Snake Game", menuX + 80, menuY + 30);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Press any key to start", menuX + 50, menuY + 70);
    }

    private void drawSnake(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        for (Point segment : snakeSegments) {
            g2d.fillRect(segment.x * blockSize, segment.y * blockSize, blockSize, blockSize);
        }
    }

    public void updateSnake(int[][] newSnake) {
        snakeSegments.clear();
        for (int[] segment : newSnake) {
            snakeSegments.add(new Point(segment[0], segment[1]));
        }
        repaint();
    }

    public void drawApple(Graphics2D g2d, int appleX, int appleY) {
        g2d.setColor(Color.RED);
        g2d.fillRect(appleX * blockSize+5, appleY * blockSize+5, blockSize-10, blockSize-10);
    }

    private void tick() {
        if (!gameStarted || gameOver) {
            timer.stop();
            snakeSegments.clear();
            for (int section = 0; section < parts; section++) snakeSegments.add(new Point(4 + section, 8));
            appleX = 13; appleY = 8;
            dx = 1; dy = 0;
            menuX = blockSize * cols / 2 - 150;
            repaint();
            return;
        } else {
            menuX = blockSize * cols + 200;
            gameOver = false;
            timer.start();
        }

        Point head = snakeSegments.get(snakeSegments.size() - 1);
        Point next = new Point(head.x + dx, head.y + dy);

        boolean hitWall = next.x < 1 || next.x >= cols - 1 || next.y < 1 || next.y >= rows - 1;
        if (hitWall || snakeSegments.contains(next)) {
            gameOver = true;
            return;
        }
        
        if (next.equals(new Point(appleX, appleY))) {
            int tempx = (int) (Math.random() * (cols - 2)) + 1;
            int tempy = (int) (Math.random() * (rows - 2)) + 1;
            while (snakeSegments.contains(new Point(tempx, tempy))) {
                tempx = (int) (Math.random() * (cols - 2)) + 1;
                tempy = (int) (Math.random() * (rows - 2)) + 1;
            }
            appleX = tempx;
            appleY = tempy;
        } else {
            snakeSegments.remove(0); // drop tail if not eating food
        }

        snakeSegments.add(next);
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            SnakeGame panel = new SnakeGame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow();
        });
    }
}
