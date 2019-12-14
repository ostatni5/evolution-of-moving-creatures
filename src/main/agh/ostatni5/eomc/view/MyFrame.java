package agh.ostatni5.eomc.view;
import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        super("Evolution of moving creatures");
//        GridLayout experimentLayout = new GridLayout(2,2);
//        setLayout(experimentLayout);
        setSize(600,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}