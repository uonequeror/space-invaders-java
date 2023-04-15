package main;
import java.awt.*;
import javax.swing.*;
public class SpaceInvaders extends JFrame {
    public SpaceInvaders() {
        initUI();
    }
    public void initUI(){
        add(new Board());
        setTitle("space invaders");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    public static void main (String [] args) {
        EventQueue.invokeLater(() -> {
            var ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }
}
