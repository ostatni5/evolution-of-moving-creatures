package agh.ostatni5.eomc;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.*;

public class Creature implements IMapElement {
    Vector2d position;
    int energy =256;
    int maxEnergy = 256;
    int orientation = 0;
    WorldMap map;
    int[] gens = new int[32];
    Random random = new Random();
    public Boolean breeded = false;

    Creature() {
    }

    Creature(WorldMap _map, Vector2d _position,int _maxEnergy ,int initialEnergy) {
        position = _position;
        map = _map;
        maxEnergy=_maxEnergy;
        energy = initialEnergy;
        randomGenotype();
        rotate(chooseRotation());
    }

    public void eat(int energyGain) {
        energy += energyGain;
    }

    public void removeEnergy(int amount) {
        energy -= amount;
    }

    int chooseRotation() {
        return gens[random.nextInt(32)];
    }

    void rotate(int rotation) {
        orientation += rotation;
        orientation %= DirectionParser.directionAmount;
    }

    void randomGenotype() {
        int taken = 0;
        int[] tempDirections = Arrays.copyOf(DirectionParser.directions,DirectionParser.directionAmount);
        MyArrays.shuffleArray(tempDirections);
        for (int i = 0; i < 7; i++) {
            int bound = 32 - (8 - 1 - i) - taken;
            int amount = bound == 0 ? 1 : random.nextInt(bound) + 1;
            for (int j = 0; j < amount; j++) {
                gens[j + taken] = tempDirections[i];
            }
            taken += amount;
        }
        for (int j = taken; j < 32; j++) {
            gens[j] = tempDirections[7];
        }
       this.setGens(gens);
    }

    Boolean allGeneCountAboveZero(int[] counter) {
        for (int i = 0; i < 8; i++) {
            if (counter[i] == 0) return false;
        }
        return true;
    }

    int[] countGenes() {
        int[] counter = new int[8];
        Arrays.fill(counter, 0);
        for (int i : gens) {
            counter[i]++;
        }
        return counter;
    }

    public Creature breedingWith(Creature partner){
        if(ableToBreed() && partner.ableToBreed()){
            Creature child = new Creature();
            child.setGens(combineGenotype(partner));
            int[] countGenes = child.countGenes();
            while(!child.allGeneCountAboveZero(countGenes)){
                for (int i = 0; i < countGenes.length; i++) {
                    if(countGenes[i]==0)
                    {
                        child.gens[random.nextInt(32)]=i;
                    }
                    countGenes=child.countGenes();
                }
            }
            int parentEnergy = energy/4 +partner.energy/4;
            removeEnergy(energy/4);
            partner.removeEnergy(partner.energy/4);
            child.energy=parentEnergy;
            child.sortGens();
            child.orientation=DirectionParser.directions[random.nextInt(DirectionParser.directionAmount)];
            child.setPosition(position);
            return child;
        }
        return null;
    }

    int [] combineGenotype(Creature partner){
        int geneCutIndex = random.nextInt(16)+8;
        int[] newGenotype = new int[32];
        int i = 0;
        for (; i <=geneCutIndex ; i++) {
            newGenotype[i]=gens[i];
        }
        for (; i <32 ; i++) {
            newGenotype[i] = partner.gens[i];
        }
        return newGenotype;
    }

    public boolean ableToBreed()
    {
        return energy>= maxEnergy/2 && !breeded;
    }

    public void setGens(int[] gens) {
        this.gens = gens;
        sortGens();
    }

    public void sortGens(){
        Arrays.sort(this.gens);
    }

    public void setPosition(Vector2d position) {
        this.position = new Vector2d(position);
    }
    public void setPosition(int x, int y ) {
        this.position = new Vector2d(x,y);
    }
    public int getEnergy() {
        return energy;
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }

    @Override
    public String toString() {
        return DirectionParser.toString(orientation);
    }
}
