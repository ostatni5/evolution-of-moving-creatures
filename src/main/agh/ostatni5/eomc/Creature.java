package agh.ostatni5.eomc;

import java.util.*;

public class Creature implements IMapElement {
    private Vector2d position;
    public Energy energy;
    Rotation rotation = Rotation.R0;
    WorldMap map;
    Genotype genotype;
    Random random = new Random();
    Boolean breeded = true;
    Boolean moved = false;
    LinkedList<Vector2d> history = new LinkedList<>();
    int ID = 0;
    int parent1ID=0;
    int parent2ID=0;
    LinkedList  children = new LinkedList<Creature>();
    int lifespan = 0;
    int death =-1;

    Creature(Creature creature) {
        position = creature.position;
        map = creature.map;
        energy = creature.energy;
        genotype = creature.genotype;
        rotation = creature.rotation;
        ID = creature.ID;
    }

    Creature(WorldMap _map, Vector2d _position, int _startEnergy) {
        position = new Vector2d(_position);
        map = _map;
        energy = new Energy(_startEnergy, _startEnergy);
        genotype = new Genotype();
        ID = map.creatureID++;
        chooseRotation();
    }

    Creature(WorldMap _map, Vector2d _position, int _startEnergy, Genotype _genotype) {
        position = new Vector2d(_position);
        map = _map;
        energy = new Energy(_startEnergy, _startEnergy);
        genotype = _genotype;
        ID = map.creatureID++;
    }

    Creature(WorldMap _map, Vector2d _position, int _startEnergy, int _energyValue, Genotype _genotype,Creature creature1, Creature creature2) {
        position = new Vector2d(_position);
        map = _map;
        energy = new Energy(_startEnergy, _energyValue);
        genotype = _genotype;
        rotation = genotype.getRotation();
        ID = map.creatureID++;
        parent1ID= creature1.ID;
        parent2ID= creature2.ID;
    }

    private void rotate(Rotation rotation) {
        this.rotation = this.rotation.rotate(rotation);
    }

    public void moveForward() {
        Vector2d positionOld = new Vector2d(position);
        position = position.add(rotation.getUnitVector());
        position = map.correctPos(position);
        map.positionChanged(positionOld, this);
        moved = true;
        history.offerFirst(position);
    }


    public Creature breedingWith(Creature partner) {
        if (ableToBreed() && partner.ableToBreed()) {
            int parentEnergy = breedEnergy() + partner.breedEnergy();
            Genotype childGenotype = genotype.combineGenotype(partner.genotype);
            Creature child =  new Creature(map, map.findFreeNearForChild(position), energy.start, parentEnergy, childGenotype, this,partner);
            children.add(child);
            return child ;
        }
        return null;
    }


    private int breedEnergy() {
        energy.loss(energy.value / 4);
        return energy.value / 4;
    }

    public void chooseRotation() {
        rotate(genotype.getRotation());
    }

    public boolean isDead() {
        return energy.value <= 0;
    }

    public void makeBreedAble() {
        breeded = false;
    }

    public boolean ableToBreed() {
        return energy.value >= energy.start / 2 && !breeded;
    }

    public void setPosition(Vector2d position) {
        this.position = new Vector2d(position);
    }

    public void setPosition(int x, int y) {
        this.position = new Vector2d(x, y);
    }


    private void  setParents(Creature creature1, Creature creature2)
    {
        parent1ID= creature1.ID;
        parent2ID= creature2.ID;
    }

    @Override
    public Vector2d getPosition() {
        return new Vector2d(position);
    }

    @Override
    public String toString() {
        return rotation.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Creature creature = (Creature) o;

        if (ID != creature.ID) return false;
        return genotype.equals(creature.genotype);
    }

    @Override
    public int hashCode() {
        int result = genotype.hashCode();
        result = 31 * result + ID;
        return result;
    }

    public Rotation getRotation() {
        return rotation;
    }
}
