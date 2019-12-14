package agh.ostatni5.eomc;

import agh.ostatni5.eomc.view.GameCanvas;
import agh.ostatni5.eomc.view.MyFrame;
import agh.ostatni5.eomc.view.MyLabel;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

public class SimulationTest {

    @Test
    public void main1() {
        WorldMap worldMap = new WorldMap(9,9,1,1,5,5);
        String text = worldMap.toString();
        System.out.print(worldMap.toString());
        for (int i = 0; i < 10000 && worldMap.creatureCount >0 ; i++) {
            worldMap.nextDay();
            System.out.print(worldMap.toString());
            System.out.println("G/: "+worldMap.grassCount+" C/: "+worldMap.creatureCount+" CC/: "+worldMap.creatureCount2 + " I/: "+ i);
            assertEquals(worldMap.creatureCount,worldMap.creatureCount2);
        }
        System.out.println("Zwierzaki");
        for (Object o : worldMap.creaturesMap.entrySet().toArray()) {
            Map.Entry<Vector2d, PriorityQueue<Creature>> pair = (Map.Entry<Vector2d, PriorityQueue<Creature>>) o;
            Creature[] ts1 = pair.getValue().toArray(new Creature[0]);
            LinkedList<Creature> ts = new LinkedList<Creature>(Arrays.asList(ts1));
            for (Creature c : ts.toArray(new Creature[0])) { System.out.print(c.getPosition() +":"+c.ID );
            }
        }

    }
    @Test
    public void main2() throws IOException {
        WorldMap worldMap = new WorldMap(20,20,8,4,10,10);
        String text = worldMap.toString();
        System.out.print(worldMap.toString());
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        WriteFile logger = new WriteFile("log.txt");
        logger.writeToFile(timestamp.toString(),false);
        for (int i = 0; i < 10000 && worldMap.creatureCount >0 ; i++) {
            worldMap.nextDay();
            System.out.print(worldMap.toString());
            System.out.println("G/: "+worldMap.grassCount+" C/: "+worldMap.creatureCount+" CC/: "+worldMap.creatureCount2 + " I/: "+ i);
            System.out.println("AVG children/: "+worldMap.childrenAvg+" L/: "+worldMap.lifespanAvg+" E/: "+worldMap.energyAvg);
            assertEquals(worldMap.creatureCount,worldMap.creatureCount2);
            logger.writeToFile( " I/: "+ i+" G/: "+worldMap.grassCount+" C/: "+worldMap.creatureCount+" CC/: "+worldMap.creatureCount2+" AVG children/: "+worldMap.childrenAvg+" L/: "+worldMap.lifespanAvg+" E/: "+worldMap.energyAvg +"  "+Arrays.toString(worldMap.genCount), true);

        }
        System.out.println("Zwierzaki");
        for (Object o : worldMap.creaturesMap.entrySet().toArray()) {
            Map.Entry<Vector2d, PriorityQueue<Creature>> pair = (Map.Entry<Vector2d, PriorityQueue<Creature>>) o;
            Creature[] ts1 = pair.getValue().toArray(new Creature[0]);
            LinkedList<Creature> ts = new LinkedList<Creature>(Arrays.asList(ts1));
            for (Creature c : ts.toArray(new Creature[0])) { System.out.print(c.getPosition() +":"+c.ID );
            }
        }

    }

    @Test
    public void mainWithCanvasSwing() throws InterruptedException {
        WorldMap worldMap = new WorldMap(20,20,8,4,10,10);
        WorldMap worldMap2 = new WorldMap(20,20,8,4,10,10);
        String text = worldMap.toString();
        MyFrame myFrame = new MyFrame();
        GameCanvas gameCanvas = new GameCanvas(worldMap);
        GameCanvas gameCanvas2 = new GameCanvas(worldMap2);
        myFrame.add(gameCanvas);
        myFrame.add(gameCanvas2);

        MyLabel jLabel= new MyLabel("Stats");
        MyLabel jLabel2= new MyLabel("Stats2");
        myFrame.add(jLabel);
        myFrame.add(jLabel2);

        myFrame.setSize((int) (gameCanvas.getWidth()*2+gameCanvas.tileSize),(int)(gameCanvas.getHeight()*2+gameCanvas.tileSize*2));
        for (int i = 0; i < 1000 && worldMap.creatureCount >0 ; i++) {
            worldMap.nextDay();
            worldMap2.nextDay();
            jLabel.setHtml(worldMap.statistics.toHtml());
            jLabel2.setHtml(worldMap2.statistics.toHtml());

            assertEquals(worldMap.creatureCount,worldMap.creatureCount2);
            assertEquals(worldMap2.creatureCount,worldMap2.creatureCount2);
            myFrame.repaint();
            Thread.sleep(1000/20);
        }
        Thread.sleep(1000);
    }

    @Test
    public void mainWithCanvasLayout() throws InterruptedException {
        WorldMap worldMap = new WorldMap(20,20,8,4,10,10);
        WorldMap worldMap2 = new WorldMap(9,9,8,4,10,10);
        String text = worldMap.toString();
        MyFrame myFrame = new MyFrame();
        myFrame.setLayout( new BoxLayout(myFrame.getContentPane(), BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        GameCanvas gameCanvas = new GameCanvas(worldMap);
        MyLabel jLabel= new MyLabel("Stats");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(gameCanvas);
        panel.add(jLabel);
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        myFrame.add(panel);

        GameCanvas gameCanvas2 = new GameCanvas(worldMap2);
        MyLabel jLabel2= new MyLabel("Stats2");
        JPanel panel2 = new JPanel();
        panel2.setLayout( new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.add(gameCanvas2);
        panel2.add(jLabel2);
        panel2.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        myFrame.add(panel2);


       myFrame.pack();
        for (int i = 0; i < 1000 && worldMap.creatureCount >0 ; i++) {
            worldMap.nextDay();
            worldMap2.nextDay();
            jLabel.setHtml(worldMap.statistics.toHtml());
            jLabel2.setHtml(worldMap2.statistics.toHtml());

            assertEquals(worldMap.creatureCount,worldMap.creatureCount2);
            assertEquals(worldMap2.creatureCount,worldMap2.creatureCount2);
            myFrame.repaint();
            Thread.sleep(1000/20);
        }
        Thread.sleep(1000);
    }
}