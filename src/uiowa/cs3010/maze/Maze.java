package uiowa.cs3010.maze;

import java.util.*;

public class Maze {
    private int[][] grid;
    private int width, height;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[width][height];
        generateSolvableMaze();
    }

    private void generateSolvableMaze() {
        do {
            generateMaze();
        } while (!isSolvable());
    }

    private void generateMaze() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = (Math.random() < 0.3) ? 1 : 0; // Randomly place walls
            }
        }
        grid[0][0] = 0; // Ensure start is open
        grid[width - 1][0] = 0; // Ensure exit is open
    }

    private boolean isSolvable() {
        return bfs(new Node(0, 0), new Node(width - 1, 0));
    }

    private boolean bfs(Node start, Node target) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(target)) {
                return true;
            }

            for (Node neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && grid[neighbor.getX()][neighbor.getY()] == 0) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        return false;
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (int[] dir : directions) {
            int newX = node.getX() + dir[0];
            int newY = node.getY() + dir[1];
            if (newX >= 0 && newY >= 0 && newX < width && newY < height) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height && grid[x][y] == 0;
    }

    public Node getExitNode() {
        return new Node(width - 1, 0);
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
