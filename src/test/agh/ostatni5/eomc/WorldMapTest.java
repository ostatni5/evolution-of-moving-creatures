package agh.ostatni5.eomc;

import org.junit.Test;


import java.util.LinkedList;

import static org.junit.Assert.*;

public class WorldMapTest {
    Vector2d v0 = new Vector2d(0,0);

    @Test
    public void constructorTest(){
        WorldMap worldMap = new WorldMap(6,6,2,2);
        assertEquals(worldMap.jungleStartVector,new Vector2d(2,2));
        worldMap = new WorldMap(6,6,3,3);
        assertEquals(worldMap.jungleStartVector,new Vector2d(1,1));
        worldMap = new WorldMap(6,6,4,3);
        assertEquals(worldMap.jungleStartVector,new Vector2d(1,1));
    }


    @Test
    public void correctPos() {
        WorldMap worldMap = new WorldMap(6,6,2,2);
        assertEquals(worldMap.correctPos(new Vector2d(3,3)),new Vector2d(3,3));
        assertEquals(worldMap.correctPos(new Vector2d(7,7)),new Vector2d(0,0));
        assertEquals(worldMap.correctPos(new Vector2d(-1,-1)),new Vector2d(5,5));
        assertEquals(worldMap.correctPos(new Vector2d(0,-1)),new Vector2d(0,5));
    }

    @Test
    public void place() {
        WorldMap worldMap = new WorldMap(6,6,2,2);
        Vector2d v = new Vector2d(0,0);
        Creature c = new Creature(worldMap,v,worldMap.startEnergy);
        Creature cCOPY = new Creature(c);
        Creature cc = new Creature(worldMap,v,worldMap.startEnergy);
        Creature c2 = new Creature(worldMap,v,worldMap.startEnergy-1);
        Creature c3 = new Creature(worldMap,v,worldMap.startEnergy-2);
        assertEquals(c,c);
        worldMap.place(c);
        assertEquals(worldMap.creaturesAt(v).size(),1);

        System.out.println(worldMap.creaturesAt(v).peek()+" "+c);
        System.out.println(worldMap.creaturesAt(v).peek().equals(c)+" "+c);


        assertEquals(worldMap.creaturesAt(v).size(),1);
        assertEquals(worldMap.creaturesAt(v).peek(),c);
        System.out.println("C2");
        worldMap.place(c2);
        assertEquals(worldMap.creaturesAt(v).size(),2);
        assertEquals(worldMap.creaturesAt(v).peek(),c);
        worldMap.place(c3);
        assertEquals(worldMap.creaturesAt(v).size(),3);
        assertEquals(worldMap.creaturesAt(v).peek(),c);
        worldMap.place(cc);
        assertEquals(worldMap.creaturesAt(v).size(),4);


        System.out.println("Masasowka");
        LinkedList<Creature> linkedList = new LinkedList<>();
        worldMap = new WorldMap(6,6,2,2);
        for (int i = 1; i <= 5  ; i++) {
            Creature c4 = new Creature(worldMap,v,worldMap.startEnergy);
            linkedList.add(c4);
            worldMap.place(c4);
            assertEquals(i,linkedList.size());
        }
        worldMap.place(c);
        worldMap.place(c2);
        worldMap.place(c3);
        assertEquals(8,worldMap.creaturesAt(v).size());
        System.out.println(worldMap.toString());


    }

    @Test
    public void changedPosition() {
        WorldMap worldMap = new WorldMap(6,6,2,2);
        Vector2d v = new Vector2d(0,0);
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(3,3);
        Creature c = new Creature(worldMap,v,worldMap.startEnergy);
        Creature cc = new Creature(worldMap,v,worldMap.startEnergy);
        Creature c2 = new Creature(worldMap,v,worldMap.startEnergy-1);
        Creature c3 = new Creature(worldMap,v,worldMap.startEnergy-2);
        worldMap.place(c);
        worldMap.place(cc);
        worldMap.place(c2);
        worldMap.place(c3);

        c.setPosition(v1);
        worldMap.positionChanged(v,c);
        cc.setPosition(v1);
        worldMap.positionChanged(v,cc);
        assertEquals(worldMap.creaturesAt(v).size(),2);
        c.setPosition(v);
        worldMap.positionChanged(v1,c);
        assertEquals(worldMap.creaturesAt(v).size(),3);
        cc.setPosition(v2);
        worldMap.positionChanged(v1,cc);
        c.setPosition(v1);
        worldMap.positionChanged(v,c);
        c2.setPosition(v1);
        worldMap.positionChanged(v,c2);
        c3.setPosition(v1);
        worldMap.positionChanged(v,c3);
        assertEquals(worldMap.creaturesAt(v),null);

        c2.setPosition(v2);
        worldMap.positionChanged(v1,c2);
        c3.setPosition(v2);
        worldMap.positionChanged(v1,c3);
        cc.setPosition(v3);
        worldMap.positionChanged(v2,cc);
    }

}