package uiowa.cs3010.maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends JFrame {
    private static final int CELL_SIZE = 40;
    private static final int TIME_LIMIT_SECONDS = 15;
    private int cellSize;
    private Maze maze;
    private Player player;
    private boolean isGameRunning = true;
    private Timer gameTimer;
    private int timeRemaining = TIME_LIMIT_SECONDS;
    private JLabel timerLabel;
    private MazePanel mazePanel;

    public GameController() {
        maze = new Maze(30, 30);
        player = new Player(maze);
        setTitle("SPEEDRUN");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxCellSizeWidth = (screenSize.width - 100) / maze.getWidth();
        int maxCellSizeHeight = (screenSize.height - 100) / maze.getHeight();
        cellSize = Math.min(CELL_SIZE, Math.min(maxCellSizeWidth, maxCellSizeHeight));

        setSize(maze.getWidth() * cellSize, maze.getHeight() * cellSize + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mazePanel = new MazePanel();
        add(mazePanel, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isGameRunning) {
                    handlePlayerMovement(e);
                }
            }
        });

        timerLabel = new JLabel("Time: " + timeRemaining + " seconds");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isGameRunning) {
                    timeRemaining--;
                    timerLabel.setText("Time: " + timeRemaining + " seconds");

                    if (timeRemaining <= 0) {
                        endGame("Time's up! Game Over!");
                        dispose();
                    }
                }
            }
        });
        gameTimer.start();
    }

    private void handlePlayerMovement(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: player.move("up"); break;
            case KeyEvent.VK_S: player.move("down"); break;
            case KeyEvent.VK_A: player.move("left"); break;
            case KeyEvent.VK_D: player.move("right"); break;
        }

        mazePanel.repaint();
        checkForExit();
    }

    private void checkForExit() {
        Node exitNode = maze.getExitNode();
        if (player.getX() == exitNode.getX() && player.getY() == exitNode.getY()) {
            endGame("You escaped! You win!");
            dispose();
        }
    }

    private void endGame(String message) {
        isGameRunning = false;
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, message);
    }

    private class MazePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMaze(g);
            drawPlayer(g);
        }

        private void drawMaze(Graphics g) {
            Node exitNode = maze.getExitNode();

            for (int x = 0; x < maze.getWidth(); x++) {
                for (int y = 0; y < maze.getHeight(); y++) {
                    if (x == exitNode.getX() && y == exitNode.getY()) {
                        g.setColor(Color.BLUE);
                    } else if (maze.getGrid()[x][y] == 1) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        private void drawPlayer(Graphics g) {
            g.setColor(Color.BLUE);
            int playerX = player.getX() * cellSize + cellSize / 2;
            int playerY = player.getY() * cellSize + cellSize / 2;
            g.fillOval(playerX - cellSize / 4, playerY - cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController game = new GameController();
            game.setVisible(true);
        });
    }
}
