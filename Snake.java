import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Snake{
    public final List<Point> snakeSegments = new ArrayList<>(); //List that hold all snake segments
    private int parts = 0; // snake parts starting length
    private int startX = 0, startY = 0; // Starting X and Y positions of the snake
    

    public Snake(int parts, int startX, int startY) {
        this.parts = parts;
        this.startX = startX;
        this.startY = startY;
        for (int section = 0; section < parts; section++) snakeSegments.add(new Point(startX + section, startY)); //Adding the starting snake to the list
    }

    public void drawSnake(Graphics2D g2d, int blockSize) {
        g2d.setColor(Color.BLUE);
        for (Point segment : snakeSegments) {
            g2d.fillRect(segment.x * blockSize, segment.y * blockSize, blockSize, blockSize);
        }
    }

    public Point getSnakeHead() {
        return snakeSegments.get(snakeSegments.size() - 1);
    }

    public Point getNextHeadPosition(int ndx, int ndy) {
        Point head = getSnakeHead();
        return new Point(head.x + ndx, head.y + ndy);
    }

    public void addSegment(Point segment) {
        snakeSegments.add(segment);
    }

    public void removeTail() {
        snakeSegments.remove(0);
    }

    public void clearSnake() {
        snakeSegments.clear();
    }

    public void resetSnake(){
        snakeSegments.clear();
        for (int section = 0; section < parts; section++) snakeSegments.add(new Point(this.startX + section, this.startY)); //Adding the starting snake to the list
    }
}