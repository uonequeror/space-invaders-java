package main;
import main.sprite.Alien;
import main.sprite.Player;
import main.sprite.Shot;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
public class Board extends JPanel{
    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;
    private int direction = -1;
    private int deaths = 0;
    private boolean inGame = true;
    private String message = "Game Over";
    private Timer timer;
    public Board(){
        initBoard();
        gameInit();
    }
    private void initBoard(){
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);
        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
        gameInit();
    }
    private void gameInit(){
        aliens = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 5; j++){
                var alien = new Alien(Commons.ALIEN_INIT_X + 50 * j, Commons.ALIEN_INIT_Y + 50 * i);
                aliens.add(alien);
            }
        }
        player = new Player();
        shot = new Shot();
    }
    private void drawAliens(Graphics g){
        for (Alien alien : aliens){
            if (alien.isVisible()){
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
            if (alien.isDying()){
                alien.die();
            }
        }
    }
    private void drawPlayer (Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }
    private void drawShot (Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }
    private void drawBombing(Graphics g) {
        for (Alien a : aliens) {
            Alien.Bomb b = a.getBomb();
            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    private void doDrawing(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.cyan);
        if (inGame) {
            g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    private void gameOver (Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        if (message.equals("Game von!")) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.pink);
        }
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        var small = new Font("Helvetica", Font.BOLD, 16);
        var fontMetrics = this.getFontMetrics(small);
        g.setColor(Color.black);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2, Commons.BOARD_WIDTH / 2);
    }
    private void update(){
        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
            inGame = false;
            timer.stop();
            message = "Game von!";
        }
        //player
        player.act();
        //shot
        String explosionImg = "src/images/explosion.png";
        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();
            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();
                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {
                        var ii = new ImageIcon(explosionImg);
                        alien.setImage(ii.getImage());
                        alien.setX(shotX);
                        alien.setY(shotY-20);
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }
            int y = shot.getY();
            y -= 7;
            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }
        // aliens
        for (Alien alien : aliens) {
            int x = alien.getX();
            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
                direction = -1;
                for (Alien a2 : aliens) {
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }
            if (x <= Commons.BORDER_LEFT && direction != 1) {
                direction = 1;
                for (Alien a : aliens) {
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                int y = alien.getY();
                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }
                alien.act(direction);
            }
        }
       // bombs
        var generator = new Random();
        for (Alien alien : aliens) {
            int shot = generator.nextInt(1000);
            Alien.Bomb bomb = alien.getBomb();
            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }
            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            if (player.isVisible() && !bomb.isDestroyed()) {
                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {
                    var ii = new ImageIcon(explosionImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }
            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 1);
                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
    }
    private void doGameCycle () {
        update();
        repaint();
    }
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased (KeyEvent e) {
            player.keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int x = (player.getX() + 11);
            int y = player.getY();
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
    private class MAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int button = e.getButton();
            int x = (player.getX() + 11);
            int y = player.getY();
            if (button == MouseEvent.BUTTON1) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}
