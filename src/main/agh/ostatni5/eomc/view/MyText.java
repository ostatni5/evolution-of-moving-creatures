package agh.ostatni5.eomc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class MyText extends JComponent {
    private String text;
    public MyText(String text) {
        this.text= text;
    }
    private void drawString(Graphics g, String text, int x, int y) {
        int lineHeight = g.getFontMetrics().getHeight();
        for (String line : text.split("\n"))
            g.drawString(line, x, y += lineHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawString(g,text, 10, 10);
        Ellipse2D circle = new Ellipse2D.Double(10, 10, 380, 380);
    }
}
