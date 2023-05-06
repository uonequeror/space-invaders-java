package main;
import main.sprite.AirDrop;
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
    private Counter counter;
    private AirDrop airDrop;
    private  GameHelper gameHelper;
    private int direction = -1;
    private int kills = 0;
    private int InvisibleKillsCounter = 0;
    private int lives = 3;
    private boolean inGame = true;
    private String message = "Game Over";
    private String bonus = " - ";
    private int ammoType = 1;
    private Timer timer;
    public Board(){
        initBoard();
        gameInit();
    }
    private void initBoard(){
        addKeyListener(new KeyLogger());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);
        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
        gameInit();
    }
    private void gameInit(){
        alienInit();
        player = new Player();
        shot = new Shot();
        counter = new Counter();
        airDrop = new AirDrop();
        airDrop.setVisible(false);
        gameHelper = new GameHelper();
    }

    private void alienInit(){
        aliens = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 5; j++){
                var alien = new Alien(Commons.ALIEN_INIT_X + 50 * j, Commons.ALIEN_INIT_Y + 50 * i);
                aliens.add(alien);
            }
        }
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
    private void drawAirDrop (Graphics g){
        if (airDrop.isVisible()) {
            g.drawImage(airDrop.getImage(), airDrop.getX(), airDrop.getY(), this);
        }
    }
    private void drawText(Graphics g) {
        Font font = new Font("SansSerif", Font.BOLD, 17);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString("lives: " + lives, 287, 20);
        g.drawString("points: " + counter.getPoints(), 10, 20);
        g.drawString(bonus, 130, 20);
    }
    private void drawLines(Graphics g) {
        g.setColor(Color.cyan);
        g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
        g.drawLine(0, 30, Commons.BOARD_WIDTH, 30);
        g.drawLine(120, 0, 120, 30);
        g.drawLine(260, 0, 260, 30);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    private void doDrawing(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        if (inGame) {
            drawLines(g);
            drawAirDrop(g);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
            drawText(g);
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
    private void initAirDrop() {
            if (counter.getCount() >= Commons.POINTS_TO_INIT_AIRDROP) {
                airDrop.setVisible(true);
                int playerX = player.getX();
                int playerY = player.getY();
                int airX = airDrop.getX();
                int airY = airDrop.getY();
                if (gameHelper.checkAirDropCollision(playerX, playerY, airX, airY)) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(3) + 1;
                    switch (randomNumber) {
                        case 1 -> {
                            shot.increaseSpeed(5);
                            bonus = " reload - ";
                        }
                        case 2 -> {
                            player.addSpeed();
                            bonus = " move speed + ";
                        }
                        case 3 -> {
                            ammoType = 2;
                            shot.setSpeed(30);
                            player.setMinigunImage();
                            bonus = " minigun ";
                        }
                        default -> System.out.println("error in random methods");
                    }
                    counter.resetCount();
                    airDrop.setVisible(false);
                    airDrop.reset();
                }
            }
    }
    private void resetBonus(){
        shot.setSpeed(Commons.SHOOTING_SPEED);
        player.setSpeed(Commons.PLAYER_MOVEMENT_SPEED);
        ammoType = 1;
        player.setDefaultImage();
        bonus = " - ";
        System.out.println("reset");
    }
    private void update(){
//general
        if (lives == 0) {
            inGame = false;
            timer.stop();
            message = "Game Over!";
        }
        if (kills == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            inGame = true;
            alienInit();
            kills = 0;
        }
        if (InvisibleKillsCounter == Commons.KILLS_RESET_COUNTER){
            resetBonus();
            InvisibleKillsCounter = 0;
            System.out.println(InvisibleKillsCounter);
        }
//airdrop
        initAirDrop();
        if (airDrop.isVisible()){
            airDrop.move();
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
                    if (gameHelper.checkShotCollision(shotX, shotY, alienX, alienY)) {
                        var ii = new ImageIcon(explosionImg);
                        alien.setImage(ii.getImage());
                        alien.setX(shotX);
                        alien.setY(shotY-20);
                        alien.setDying(true);
                        kills++;
                        InvisibleKillsCounter++;
                        counter.addPoints(100);
                        counter.increaseCount(100);
                        shot.die();
                    }
                }
            }
            int y = shot.getY();
            y -= shot.getSpeed();
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
                    a2.setY(a2.getY() + Commons.ALIENS_GO_DOWN);
                }
            }
            if (x <= Commons.BORDER_LEFT && direction != 1) {
                direction = 1;
                for (Alien a : aliens) {
                    a.setY(a.getY() + Commons.ALIENS_GO_DOWN);
                }
            }
        }
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                int y = alien.getY();
                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    alien.die();
                    inGame = false;
                    message = "Invasion!";
                }
                alien.act(direction);
            }
        }
// bombs
        var generator = new Random();
        for (Alien alien : aliens) {
            int random = generator.nextInt(1000);
            Alien.Bomb bomb = alien.getBomb();
            if (random == Commons.CHANCE_ALIEN_TO_BOMB && alien.isVisible() && bomb.isDestroyed()) {
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }
            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            if (player.isVisible() && !bomb.isDestroyed()) {
                if (gameHelper.checkBombCollision(bombX, bombY, playerX, playerY)) {
                    if (lives > 0){
                        lives--;
                    } else {
                        var ii = new ImageIcon(explosionImg);
                        player.setImage(ii.getImage());
                        player.setDying(true);
                    }
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
    private class KeyLogger extends KeyAdapter {
        @Override
        public void keyReleased (KeyEvent e) {
            player.keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int x = player.getX();
            int y = player.getY();
            if (ammoType == 1) {
                x += 11;
            }  else if (ammoType == 2) {
                x += 4;
                y += 15;
            }
            else {System.out.println("error");}
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y, ammoType);
                    }
                }
            }
        }
    }
}
