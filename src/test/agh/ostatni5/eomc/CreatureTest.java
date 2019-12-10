package agh.ostatni5.eomc;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreatureTest {
    Creature creature = new Creature();


    @Test
    public void randomGenotype() {
        creature.randomGenotype();
        for (int i : creature.gens) {
            System.out.println(i);
        }
        assertEquals(true,creature.allGeneCountAboveZero(creature.countGenes()));
    }

    @Test
    public void breedingWith() {
        Creature creature2 = new Creature();
        Creature child ;
        creature.randomGenotype();
        creature.setPosition(0,0);
        creature2.setPosition(0,0);
        creature2.randomGenotype();
        child= creature.breedingWith(creature2);
        System.out.println("1 c 2");
        for (int i = 0; i < 32; i++) {
            System.out.println(creature.gens[i]+" "+child.gens[i]+" "+creature2.gens[i]);
        }

    }
}