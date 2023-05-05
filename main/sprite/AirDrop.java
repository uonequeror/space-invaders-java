package main.sprite;
import javax.swing.*;
import java.util.Random;
public class AirDrop extends Sprite{
    public AirDrop() {
        initAirDrop();
    }
    private void initAirDrop(){
        var airdropImg = "src/images/airdrop.png";
        var ii = new ImageIcon(airdropImg);
        setImage(ii.getImage());
        reset();
    }
    public void reset(){
        Random rand = new Random();
        int randomX = 30 + rand.nextInt(340 - 30 + 1);
        setX(randomX);
        int START_Y = 25;
        setY(START_Y);
    }
    public void move(){
        y += 1;
    }
}
