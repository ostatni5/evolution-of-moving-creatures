package agh.ostatni5.eomc.view;

import agh.ostatni5.eomc.core.Creature;
import agh.ostatni5.eomc.core.Rectangle;
import agh.ostatni5.eomc.stats.StatisticsCreature;
import agh.ostatni5.eomc.core.Vector2d;
import agh.ostatni5.eomc.core.WorldMap;

import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JPanel {
    private int tileSize;
    private StatisticsCreature statisticsCreature;
    private WorldMap worldMap;
    private Color grassColor = new Color(167, 219, 55);
    private Color savannaColor = new Color(202, 154, 48);
    private Color jungleColor = new Color(80, 148, 12);
    private Color creatureColor = new Color(97, 12, 14);
    private HSLColor hslColor = new HSLColor(creatureColor);
    private boolean showDominant = false;

    public GameCanvas(WorldMap worldMap, int canvasSize) {
        this.worldMap = worldMap;
        this.tileSize = canvasSize / worldMap.savanna.rectangle.width;
    }

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
        g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (tileSize * 0.8)));
        paintSavanna(g);
        paintJungle(g);
        for (Vector2d vector2d : worldMap.getAllGrassesPos()) {
            paintGrass(g, vector2d);
        }
        for (Creature creature : worldMap.getAllVisibleCreatures()) {
            paintCreature(g, creature);
        }
        if (showDominant)
            for (Creature creature : worldMap.stats.getAllWithDominantGenotype()) {
                paintDominantGenotype(g, creature);
            }
        paintXAxis(g);
        paintYAxis(g);
        if (statisticsCreature != null)
            paintWatchedMarker(g, statisticsCreature.getCreature().getPosition());
    }

    private void paintXAxis(Graphics g) {
        g.setColor(Color.black);
        for (int i = 0; i < worldMap.savanna.rectangle.width; i++) {
            g.drawString(String.valueOf(i), resize(i), tileSize * 3 / 4);
        }

    }

    private void paintYAxis(Graphics g) {
        g.setColor(Color.black);
        for (int i = 0; i < worldMap.savanna.rectangle.height; i++) {
            g.drawString(String.valueOf(i), 0, resize(i) + tileSize * 3 / 4);
        }

    }

    private void paintSavanna(Graphics g) {
        g.setColor(savannaColor);
        fillRecResized(g,worldMap.savanna.rectangle);
    }

    private void paintJungle(Graphics g) {
        g.setColor(jungleColor);
        fillRecResized(g,worldMap.jungle.rectangle);
    }

    private void paintGrass(Graphics g, Vector2d v) {
        g.setColor(grassColor);
        g.fillRect(resize(v.x)+1, resize(v.y)+1, tileSize-2, tileSize-2);

    }

    private void paintCreature(Graphics g, Creature c) {
        HSLColor currentColor = new HSLColor(hslColor.getRGB());
        float lum = c.getEnergy().valueToStartRatio();
        lum = lum > 1 ? 1 : lum;
        lum /= 2;
        lum *= 100;
        g.setColor(currentColor.adjustLuminance(hslColor.getLuminance() + lum));
        g.fillRect(resize(c.getPosition().x), resize(c.getPosition().y), tileSize, tileSize);
        g.setColor(Color.white);

        g.drawString(c.getRotation().toStringReverse(), resize(c.getPosition().x) + tileSize / 4, resize(c.getPosition().y) + tileSize * 3 / 4);
    }

    private void paintWatchedMarker(Graphics g, Vector2d vector2d) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.RED);
        g2.drawOval(resize(vector2d.x), resize(vector2d.y), tileSize, tileSize);
    }

    private void paintDominantGenotype(Graphics g, Creature creature) {
        paintDominantGenotypeMarker(g, creature.getPosition());
    }

    private void paintDominantGenotypeMarker(Graphics g, Vector2d vector2d) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.BLUE);
        g2.drawOval(resize(vector2d.x), resize(vector2d.y), tileSize, tileSize);
    }

    private int resize(int a) {
        return a * tileSize;
    }

    private void fillRecResized(Graphics g,Rectangle rectangle)
    {
        g.fillRect(resize(rectangle.corners[0].x), resize(rectangle.corners[0].y), resize(rectangle.width), resize(rectangle.height));
    }

    public void setStatisticsCreature(StatisticsCreature statisticsCreature) {
        this.statisticsCreature = statisticsCreature;
    }

    public void clearStatisticsCreature() {
        setStatisticsCreature(null);
    }

    public void showDominant() {
        this.showDominant = true;
    }

    public void hideDominant() {
        this.showDominant = false;
    }
}
