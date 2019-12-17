package agh.ostatni5.eomc.view;

import agh.ostatni5.eomc.Creature;
import agh.ostatni5.eomc.Vector2d;
import agh.ostatni5.eomc.WorldMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameCanvas extends JPanel {
    public int tileSize=20;
    WorldMap worldMap;
    Color grassColor = new Color(167, 219, 55);
    Color savannaColor = new Color(235, 189, 52);
    Color jungleColor = new Color(80, 148, 12);
    Color creatureColor = new Color(97, 53, 0);
    HSLColor hslColor = new HSLColor(creatureColor);
    public GameCanvas(WorldMap worldMap){
        this.worldMap=worldMap;
//        setSize(resize(worldMap.savanna.rectangle.width),resize(worldMap.savanna.rectangle.height));

    };
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(resize(worldMap.savanna.rectangle.width), resize(worldMap.savanna.rectangle.height));
    }
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(resize(worldMap.savanna.rectangle.width), resize(worldMap.savanna.rectangle.height));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphic2d = (Graphics2D) g;
        paintSavanna(g);
        paintJungle(g);
        for (Vector2d vector2d : worldMap.getAllGrassesPos()) {
            paintGrass(g,vector2d);
        }
        for (Creature creature : worldMap.getAllVisibleCreatures()) {
            paintCreature(g,creature);
        }
        paintXAxis(g);
        paintYAxis(g);
    }

    private void paintXAxis(Graphics g)
    {
        g.setColor(Color.black);
        for (int i = 0; i < worldMap.savanna.rectangle.width ; i++) {
            g.drawString(String.valueOf(i), resize(i), tileSize/2);
        }

    }
    private void paintYAxis(Graphics g)
    {
        g.setColor(Color.black);
        for (int i = 0; i < worldMap.savanna.rectangle.height ; i++) {
            g.drawString(String.valueOf(i), 0, resize(i)+tileSize/2);
        }

    }

    private void paintSavanna(Graphics g){
        g.setColor(savannaColor);
        g.fillRect(0,0,resize(worldMap.savanna.rectangle.width),resize(worldMap.savanna.rectangle.height));
    }

    private void paintJungle(Graphics g){
        g.setColor(jungleColor);
        g.fillRect(resize(worldMap.jungle.rectangle.corners[0].x),resize(worldMap.jungle.rectangle.corners[0].y),resize(worldMap.jungle.rectangle.width),resize(worldMap.jungle.rectangle.height));
    }

    private void paintGrass(Graphics g,Vector2d v){
        g.setColor(grassColor);
        g.fillRect(resize(v.x),resize(v.y),tileSize,tileSize);
    }
    private void paintCreature(Graphics g, Creature c)
    {
        HSLColor currentColor = new HSLColor(hslColor.getRGB());
        float lum = c.energy.valueToStart();
        lum = lum> 1 ? 1:lum;
        lum/=2;
        lum*=100;
        g.setColor( currentColor.adjustLuminance(hslColor.getLuminance()+lum));
        g.fillRect(resize(c.getPosition().x),resize(c.getPosition().y),tileSize,tileSize);
        g.setColor(Color.white);
        g.drawString(c.getRotation().toStringReverse(), resize(c.getPosition().x)+tileSize/4, resize(c.getPosition().y)+tileSize*3/4);
    }

    int resize(int a){
        return a*tileSize;
    }
}
