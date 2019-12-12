package agh.ostatni5.eomc;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GenotypeTest {
    @Test
    public void constructorTest() {
        Genotype genotype = new Genotype();
        for (int i = 0; i < genotype.getGens().length-1 ; i++) {
            System.out.print(" "+genotype.getGens()[i].ordinal());
            assertEquals(true,(genotype.getGens()[i].ordinal() <= genotype.getGens()[i + 1].ordinal()));
        }
    }


    @Test
    public void combineGenotype() {
        Genotype genotype = new Genotype();
        Genotype genotype2 = new Genotype();
        Genotype genotype3 = genotype.combineGenotype(genotype2);
        System.out.println(genotype.toString());
        System.out.println(genotype2.toString());
        System.out.println(genotype3.toString());
        Rotation[] g = new Rotation[Genotype.genotypeLength];
        Arrays.fill(g,Rotation.R0);
        genotype2 = new Genotype(g);
        genotype3 = genotype.combineGenotype(genotype2);
        System.out.println(" ");
        System.out.println(genotype2.toString());
        System.out.println(genotype3.toString());
    }
}