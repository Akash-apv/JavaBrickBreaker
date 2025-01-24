import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BrickBreaker extends JFrame {
    
    public BrickBreaker() {
        setTitle("Brick Breaker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BrickBreaker game = new BrickBreaker();
            game.setVisible(true);
        });
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLUMNS = 8;

    private int paddleX;
    private int paddleY;
    private int ballX;
    private int ballY;
    private int ballDX = 2;
    private int ballDY = -2;
    private boolean[][] bricks;
    private boolean gameOver = false;
    private int score = 0;
    private Timer timer;

    public GamePanel() {
        paddleX = 350;
        paddleY = 550;
        ballX = 400;
        ballY = 530;
        bricks = new boolean[BRICK_ROWS][BRICK_COLUMNS];

        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                bricks[i][j] = true;
            }
        }

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(5, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("Game Over! Score: " + score, 250, 300);
        } else {
            // Draw paddle
            g.setColor(Color.BLUE);
            g.fillRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

            // Draw ball
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

            // Draw bricks
            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_COLUMNS; j++) {
                    if (bricks[i][j]) {
                        g.setColor(Color.GREEN);
                        g.fillRect(j * 100 + 50, i * 30 + 50, 80, 20);
                    }
                }
            }

            // Draw score
            g.setColor(Color.BLACK);
            g.drawString("Score: " + score, 10, 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        // Move ball
        ballX += ballDX;
        ballY += ballDY;

        // Ball collision with walls
        if (ballX <= 0 || ballX >= getWidth() - BALL_SIZE) {
            ballDX = -ballDX;
        }
        if (ballY <= 0) {
            ballDY = -ballDY;
        }

        // Ball collision with paddle
        if (ballY + BALL_SIZE >= paddleY && ballX + BALL_SIZE >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballDY = -ballDY;
        }

        // Ball collision with bricks
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                if (bricks[i][j]) {
                    int brickX = j * 100 + 50;
                    int brickY = i * 30 + 50;
                    if (ballX + BALL_SIZE > brickX && ballX < brickX + 80 && ballY + BALL_SIZE > brickY && ballY < brickY + 20) {
                        bricks[i][j] = false;
                        ballDY = -ballDY;
                        score += 10;
                    }
                }
            }
        }

        // Ball falls off screen
        if (ballY >= getHeight()) {
            gameOver = true;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (paddleX > 0) {
                paddleX -= 15;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (paddleX < getWidth() - PADDLE_WIDTH) {
                paddleX += 15;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}


