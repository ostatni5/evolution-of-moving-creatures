package agh.ostatni5.eomc.view;
import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public JTextArea jLabel;
    public String text;
    public MyFrame( String _text,Font font) {
        super("Evolution of moving creatures");
        UIManager.getLookAndFeelDefaults().put("defaultFont", font);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Font font1 = new Font(font.getName(), Font.PLAIN, 16);
        setLocation(50,50);
        setSize(300,300);
        text=_text;
        jLabel = new JTextArea(text);
        jLabel.setFont(font1);
        add(jLabel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


}