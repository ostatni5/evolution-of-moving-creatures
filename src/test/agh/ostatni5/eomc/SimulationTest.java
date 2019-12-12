package agh.ostatni5.eomc;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SimulationTest {

    @Test
    public void main1() {
        WorldMap worldMap = new WorldMap(9,9,0,0,5,5);
        String text = worldMap.toString();
        System.out.print(worldMap.toString());
        for (int i = 0; i < 10000 && worldMap.creatureCount >0 ; i++) {
            worldMap.nextDay();
            System.out.print(worldMap.toString());
            System.out.println("G/: "+worldMap.grassCount+" C/: "+worldMap.creatureCount+" CC/: "+worldMap.cc + " I/: "+ i);
            assertEquals(worldMap.creatureCount,worldMap.cc);
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
    public void main2() {
        WorldMap worldMap = new WorldMap(20,20,8,4,10,10);
        String text = worldMap.toString();
        System.out.print(worldMap.toString());
        for (int i = 0; i < 10000 && worldMap.creatureCount >0 ; i++) {
            worldMap.nextDay();
            System.out.print(worldMap.toString());
            System.out.println("G/: "+worldMap.grassCount+" C/: "+worldMap.creatureCount+" CC/: "+worldMap.cc + " I/: "+ i);
            assertEquals(worldMap.creatureCount,worldMap.cc);
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
}