package main.sprite;
import main.Commons;
import javax.swing.*;
import java.awt.event.KeyEvent;
public class Player extends Sprite {
    private int width;
    private static int speed = Commons.PLAYER_MOVEMENT_SPEED;
    public Player() {
        initPlayer();
    }
    private void initPlayer(){
        setDefaultImage();
        int START_X = 182;
        setX(START_X);
        int START_Y = 340;
        setY(START_Y);
    }
    public void act(){
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if ((x + width) >= Commons.BOARD_WIDTH) {
            x = Commons.BOARD_WIDTH -  width;
        }
    }
    public void setMinigunImage(){
        var playerMinigunImg = "src/images/player/player minigun.png";
        var ii = new ImageIcon(playerMinigunImg);
        setImage(ii.getImage());
    }
    public void setDefaultImage(){
        var playerImg = "src/images/player/player.png";
        var ii = new ImageIcon(playerImg);
        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT | key == KeyEvent.VK_A) {
            dx = -speed;
        }
        if (key == KeyEvent.VK_RIGHT | key == KeyEvent.VK_D) {
            dx = speed;
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT | key == KeyEvent.VK_A) {
            dx = 0;
        }
        if (key == KeyEvent.VK_RIGHT | key == KeyEvent.VK_D) {
            dx = 0;
        }
    }
    public void addSpeed (){
        speed += 1;
    }
    public void setSpeed(int playerSpeed) {
        speed = playerSpeed;
    }
}
