import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class SnakeGame extends JPanel {

    private final List<Point> snakeSegments = new ArrayList<>(); //List that hold all snake segments
    private final int BLOCK_SIZE = 40; // Size of each block in pixels
    private int dx = 1, dy = 0; //The directions of the snake movement
    private final Timer timer = new Timer(200, e -> tick()); //Timer that controls game loop (game ticks)
    private int rows = 18; // Number of rows of blocks + wall
    private int cols = 18; // Number of columns of blocks + wall
    private int parts = 4; // snake parts starting lenght
    private int appleX = 13; // X position of the apple
    private int appleY = 8; // Y position of the apple
    private boolean gameStarted = false;

    public SnakeGame() {
        setPreferredSize(new Dimension(BLOCK_SIZE * cols, BLOCK_SIZE * rows)); //Setting the size of the window

        for (int section = 0; section < parts; section++) {
            snakeSegments.add(new Point(5 + section, 8));
        } //Adding the starting snake to the list

        setFocusable(true); //Focuses on window to listen for key events
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    timer.start();
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> gameStarted = false;
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

        // Draw a grid of blocks
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * BLOCK_SIZE;
                int y = row * BLOCK_SIZE;

                if ((row == 0 || row == rows - 1 || col == 0 || col == cols - 1)) {
                    g2d.setColor(new Color(0, 128, 0)); // Wall color

                } else if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(80, 200, 120));
                } else {
                    g2d.setColor(new Color(152, 255, 152));
                }

                // Fill the block
                if (col == 0 && row == 0) {
                    g2d.fillRect(x + BLOCK_SIZE / 2, y + BLOCK_SIZE / 2, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
                } else if (col == cols - 1 && row == 0) {
                    g2d.fillRect(x, y + BLOCK_SIZE / 2, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
                } else if (col == 0 && row == rows - 1) {
                    g2d.fillRect(x + BLOCK_SIZE / 2, y, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
                } else if (col == cols - 1 && row == rows - 1) {
                    g2d.fillRect(x, y, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
                } else if (col == 0) {
                    g2d.fillRect(x + BLOCK_SIZE / 2, y, BLOCK_SIZE / 2, BLOCK_SIZE);
                } else if (col == cols - 1) {
                    g2d.fillRect(x, y, BLOCK_SIZE / 2, BLOCK_SIZE);
                } else if (row == 0) {
                    g2d.fillRect(x, y + BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE / 2);
                } else if (row == rows - 1) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE / 2);
                } else {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        drawApple(g2d, appleX, appleY);
        drawSnake(g2d);
        drawMenu(g2d);
    }

    private void drawMenu(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(BLOCK_SIZE * cols / 2 - 150, BLOCK_SIZE * rows / 2 - 50, 300, 100);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Snake Game", BLOCK_SIZE * cols / 2 - 70, BLOCK_SIZE * rows / 2 - 20);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Press any key to start", BLOCK_SIZE * cols / 2 - 100, BLOCK_SIZE * rows / 2 + 20);
    }

    private void drawSnake(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        for (Point segment : snakeSegments) {
            g2d.fillRect(segment.x * BLOCK_SIZE, segment.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
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
        g2d.fillRect(appleX * BLOCK_SIZE+5, appleY * BLOCK_SIZE+5, BLOCK_SIZE-10, BLOCK_SIZE-10);
    }

    private void tick() {
        if (!gameStarted) {
            timer.stop();
        } else {
            timer.start();
        }

        Point head = snakeSegments.get(snakeSegments.size() - 1);
        Point next = new Point(head.x + dx, head.y + dy);

        boolean hitWall = next.x < 1 || next.x >= cols - 1 || next.y < 1 || next.y >= rows - 1;
        if (hitWall || snakeSegments.contains(next)) {
            timer.stop(); // game over
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

        snakeSegments.add(next); // new head
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            SnakeGame panel = new SnakeGame();
            JButton button = new JButton("Click Me");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            // frame.add(button);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow();
        });
    }
}
