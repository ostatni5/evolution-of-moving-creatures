package agh.ostatni5.eomc;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    public static final int genotypeLength = 32;
    private Rotation[] gens = new Rotation[genotypeLength];
    private Random random = new Random();
    private Rotation dominantGen;

    Genotype() {
        int j = 0;
        for (; j < Rotation.values().length && j < genotypeLength; j++) {
            gens[j] = Rotation.values()[j];
        }
        for (; j < genotypeLength; j++) {
            gens[j] = Rotation.getRandom();
        }
        Arrays.sort(gens);
        calculateDominant();
    }

    Genotype(Rotation[] gens) {
        this.gens = gens;
        calculateDominant();
    }

    public Rotation[] getGens() {
        return gens;
    }

    Rotation getRotation() {
        return gens[random.nextInt(genotypeLength)];
    }


    public Genotype combineGenotype(Genotype genotype) {
        int geneCutIndex = random.nextInt(genotypeLength / 2) + genotypeLength / 4;
        Rotation[] newGens = new Rotation[genotypeLength];
        int i = 0;
        for (; i <= geneCutIndex; i++) {
            newGens[i] = gens[i];
        }
        for (; i < 32; i++) {
            newGens[i] = genotype.gens[i];
        }
        correctGens(newGens);
        return new Genotype(newGens);
    }

    private void correctGens(Rotation[] gens) {
        int[] countGenes = countGenes(gens);
        for (int i = 0; i < countGenes.length; i++) {
            while (countGenes[i] == 0) {
                int pos = random.nextInt(genotypeLength);
                if (countGenes[gens[pos].ordinal()] > 1) {
                    countGenes[gens[pos].ordinal()]--;
                    gens[pos] = Rotation.values()[i];
                    countGenes[i]++;
                }
            }

        }
        Arrays.sort(gens);
    }

    private void calculateDominant ()
    {
        dominantGen = Rotation.values()[MyArrays.getIndexOfMax(countGenes())];
    }

    public int[] countGenes() {
       return countGenes(gens);
    }
    private int[] countGenes(Rotation[] gens) {
        int[] counter = new int[8];
        Arrays.fill(counter, 0);
        for (Rotation r : gens) {
            counter[r.ordinal()]++;
        }
        return counter;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Rotation r : gens) {
            stringBuilder.append(r.ordinal());
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public Rotation getDominantGen() {
        return dominantGen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genotype)) return false;

        Genotype genotype = (Genotype) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(gens, genotype.gens);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(gens);
    }
}
