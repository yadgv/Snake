import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class SnakeGame extends JPanel {

    private final List<Point> snakeSegments = new ArrayList<>(); //List that hold all snake segments
    private final int blockSize = 40; // Size of each block in pixels
    private int dx = 1, dy = 0, ndx = 1, ndy = 0; // The directions of the snake movement
    private final Timer timer = new Timer(150, e -> tick()); // Timer that controls game loop (game ticks)
    private int size = 1; // size of map
    private final int rows = switch(size){case 0 -> 11; case 1 -> 17; case 2 -> 23; default -> 17;}; // Number of rows of blocks + wall
    private final int cols = switch(size){case 0 -> 12; case 1 -> 19; case 2 -> 26; default -> 19;}; // Number of columns of blocks + wall
    private final int parts = (int) ((cols-2)*0.30); // snake parts starting lenght
    private final int startx = cols/2 -parts-1; // Starting X position of the snake
    private final BufferedImage board = new BufferedImage(blockSize * cols, blockSize * rows, BufferedImage.TYPE_INT_RGB);
    private int menuX = blockSize * cols / 2 - 150, menuY = blockSize * rows / 2 - 50; // Position of the menu
    private int appleX = cols/2 + parts+1; // X position of the apple
    private int appleY = rows/2; // Y position of the apple
    private boolean gameStarted = false, gameOver = false; // checks if game has started
    private int score = 0; // score (duh)


    public SnakeGame() {
        setPreferredSize(new Dimension(blockSize * cols, blockSize * rows)); //Setting the size of the window
        Graphics2D g2d = board.createGraphics();
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

                if ((right || left || top || bottom)) g2d.setColor(new Color(0, 128, 0));
                else if ((row + col) % 2 == 0) g2d.setColor(new Color(80, 200, 120));
                else g2d.setColor(new Color(152, 255, 152));
                
                g2d.fillRect(rx, ry, rw, rh);
            }
        }
        g2d.dispose();

        for (int section = 0; section < parts; section++) snakeSegments.add(new Point(startx + section, rows/2)); //Adding the starting snake to the list

        setFocusable(true); //Focuses on window to listen for key events
        addKeyListener(new KeyAdapter() {
            @Override // overriding the kepressed method
            public void keyPressed(KeyEvent e) {
                if (!gameStarted || gameOver) startGame(); // Check if game is over and started a new game if any key is pressed
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> {gameStarted = false;} // if esc is pressed game pauses
                    case KeyEvent.VK_UP -> {if (dy == 0) {ndx = 0; ndy = -1;}} // check if moving in y direction, if not change direction to up
                    case KeyEvent.VK_DOWN -> {if (dy == 0) {ndx = 0; ndy = 1;}} 
                    case KeyEvent.VK_LEFT -> {if (dx == 0) {ndx = -1; ndy = 0;}} // check if moving in x direction, if not change direction to left
                    case KeyEvent.VK_RIGHT -> {if (dx == 0) {ndx = 1; ndy = 0;}}
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
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawBoard(Graphics2D g2d) {
        g2d.drawImage(board, 0, 0, null);
    }

    private void startGame() {
        gameStarted = true; gameOver = false;
        score = 0;
        snakeSegments.clear();
        for (int section = 0; section < parts; section++) 
            snakeSegments.add(new Point(startx + section, rows / 2));
        appleX = cols / 2 + parts + 1; appleY = rows / 2;
        dx = 1; dy = 0; ndx = 1; ndy = 0;
        timer.start();
    }

    private void drawMenu(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(menuX, menuY, 300, 100);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + score, 20, 20);
        g2d.drawString("Snake Game", menuX + 80, menuY + 30);
        g2d.drawString("Press any key to start", menuX + 50, menuY + 70);
    }

    private void drawSnake(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        for (Point segment : snakeSegments) {
            g2d.fillRect(segment.x * blockSize, segment.y * blockSize, blockSize, blockSize);
        }
    }

    public void drawApple(Graphics2D g2d, int appleX, int appleY) {
        g2d.setColor(Color.RED);
        g2d.fillOval(appleX * blockSize+3, appleY * blockSize+3, blockSize-6, blockSize-6);
    }

    private void tick() {
        System.out.println(timer.getDelay());
        if (!gameStarted || gameOver) {
            timer.stop();
            menuX = blockSize * cols / 2 - 150;
            repaint();
            return;
        } else {
            menuX = blockSize * cols + 200;
            gameOver = false;
            timer.start();
        }

        Point head = snakeSegments.get(snakeSegments.size() - 1);
        Point next = new Point(head.x + ndx, head.y + ndy);

        boolean hitWall = next.x < 1 || next.x >= cols - 1 || next.y < 1 || next.y >= rows - 1;
        if (hitWall || snakeSegments.contains(next)) {
            gameOver = true;
            return;
        }

        snakeSegments.add(next);
        if (next.equals(new Point(appleX, appleY))) {
            score++;
            do {
                appleX = (int) (Math.random() * (cols - 2)) + 1;
                appleY = (int) (Math.random() * (rows - 2)) + 1;
            } while (snakeSegments.contains(new Point(appleX, appleY)));
        } else {
            snakeSegments.remove(0); // drop tail if not eating food
        }
        dx = ndx; dy = ndy;
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
