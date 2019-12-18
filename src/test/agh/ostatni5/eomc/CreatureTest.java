package agh.ostatni5.eomc;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreatureTest {


    @Test
    public void testEquals() {
        WorldMap worldMap = new WorldMap(6,6,2,2);
        Vector2d v = new Vector2d(0,0);
        Creature c = new Creature(worldMap,v,200);
        Creature c2 = new Creature(worldMap,v,200-1);
        Creature c3 = new Creature(c);
        assertEquals(c,c);
        assertTrue(c.equals(c));
        assertEquals(c,c3);
        assertTrue(c.equals(c3));
        assertNotEquals(c,c2);
       c3.setPosition(3,4);
        assertEquals(c,c3);
        assertTrue(c.equals(c3));
    }
}