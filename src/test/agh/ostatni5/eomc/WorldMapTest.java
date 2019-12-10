package agh.ostatni5.eomc;

import org.junit.Test;



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
        assertEquals(worldMap.correctPos(new Vector2d(-1,-1)),new Vector2d(6,6));
    }
}