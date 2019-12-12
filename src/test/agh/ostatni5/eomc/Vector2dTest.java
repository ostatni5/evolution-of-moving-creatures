package agh.ostatni5.eomc;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class Vector2dTest {

    @Test
    public void testHashCode() {
        Vector2d v = new Vector2d(1,4);
        Vector2d v1 = new Vector2d(1,4);
        Vector2d v2 = new Vector2d(16,5);
        assertNotEquals(v,v2);
        assertNotEquals(v.hashCode(),v2.hashCode());
        assertEquals(v,v1);
        assertEquals(v.hashCode(),v1.hashCode());

    }
}