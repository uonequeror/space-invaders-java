package main.sprite;
import javax.swing.*;
public class Shot extends Sprite {
    public Shot() {
    }
    public Shot(int x, int y) {
        initShot(x, y);
    }
    public void initShot(int x, int y){
        var shotImg = "src/images/shot.png";
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());
        int H_SPACE = 6;
        setX(x + H_SPACE);
        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}
