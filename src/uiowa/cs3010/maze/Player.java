package uiowa.cs3010.maze;

public class Player {
    private int x, y;
    private Maze maze;

    public Player(Maze maze) {
        this.maze = maze;
        this.x = 0;
        this.y = 0;
    }

    public void move(String direction) {
        int newX = x, newY = y;
        switch (direction.toLowerCase()) {
            case "up": newY--; break;
            case "down": newY++; break;
            case "left": newX--; break;
            case "right": newX++; break;
        }

        if (maze.isValidPosition(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
