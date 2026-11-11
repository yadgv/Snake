import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;



public class SnakeGame extends JPanel {

    private final List<Point> snakeSegments = new ArrayList<>();
    private final int BLOCK_SIZE = 40; // Size of each block in pixels
    int rows = 18; // Number of rows of blocks + wall
    int cols = 18; // Number of columns of blocks + wall
    int parts = 4;

    public SnakeGame() {
        setPreferredSize(new Dimension(BLOCK_SIZE * cols, BLOCK_SIZE * rows));
        
        for (int section = 0; section < parts; section++) {
            snakeSegments.add(new Point(5+section, 5));
        }
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

                // Alternate colors to look like blocks or tiles
                if ((row == 0 || row == rows - 1 || col == 0 || col == cols - 1)) {
                    g2d.setColor(new Color(0, 128, 0)); // Wall color
                    
                } else if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(80, 200, 120));
                } else {
                    g2d.setColor(new Color(152, 255, 152));
                }

                // Fill the block
                if (col == 0 && row == 0) {
                    g2d.fillRect(x+BLOCK_SIZE/2, y+BLOCK_SIZE/2, BLOCK_SIZE/2, BLOCK_SIZE/2);
                } else if (col == cols - 1 && row == 0) {
                    g2d.fillRect(x, y+BLOCK_SIZE/2, BLOCK_SIZE/2, BLOCK_SIZE/2);
                } else if (col == 0 && row == rows - 1) {
                    g2d.fillRect(x+BLOCK_SIZE/2, y, BLOCK_SIZE/2, BLOCK_SIZE/2);
                } else if (col == cols - 1 && row == rows - 1) {
                    g2d.fillRect(x, y, BLOCK_SIZE/2, BLOCK_SIZE/2);
                } else if (col == 0) {
                    g2d.fillRect(x+BLOCK_SIZE/2, y, BLOCK_SIZE/2, BLOCK_SIZE);
                } else if (col == cols - 1) {
                    g2d.fillRect(x, y, BLOCK_SIZE/2, BLOCK_SIZE);
                }
                else if (row == 0) {
                    g2d.fillRect(x, y+BLOCK_SIZE/2, BLOCK_SIZE, BLOCK_SIZE/2);
                } else if (row == rows - 1) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE/2);
                }
                else {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }

                // Draw a dark border around the block
                // g2d.setColor(Color.DARK_GRAY);
                // g2d.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
            }
        }

        // Draw the snake
        drawSnake(g2d);    
    }


    private void drawSnake(Graphics2D g2d) {
        g2d.setColor(Color.RED);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame panel = new SnakeGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}