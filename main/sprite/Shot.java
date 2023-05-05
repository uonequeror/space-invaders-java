package main.sprite;
import main.Commons;
import javax.swing.*;

public class Shot extends Sprite {
    private static int speed = Commons.SHOOTING_SPEED;
    public Shot() {}
    public Shot(int x, int y, int ammo) {
        initShot(x, y, ammo);
    }
    public void initShot(int x, int y, int ammo){
        changeAmmo(ammo);
        int H_SPACE = 6;
        setX(x + H_SPACE);
        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
    public void changeAmmo(int ammo){
        switch (ammo) {
            case 1 -> {
                var defaultImg = "src/images/shot.png";
                var standard = new ImageIcon(defaultImg);
                setImage(standard.getImage());
            }
            case 2 -> {
                var shotgunImg = "src/images/one bullet.png";
                var shotgun = new ImageIcon(shotgunImg);
                setImage(shotgun.getImage());
            }
            case 3 -> {
                var miniGunImg = "src/images/minigun bullet.png";
                var minigun = new ImageIcon(miniGunImg);
                setImage(minigun.getImage());
            }
            default -> System.out.println("ammo  error");
        }
    }
    public void increaseSpeed(int amount) {
        speed += amount;
    }
    public int getSpeed(){
        return speed;
    }
    public void setSpeed(int shotSpeed) {
        speed = shotSpeed;
    }
}
