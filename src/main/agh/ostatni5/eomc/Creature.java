package agh.ostatni5.eomc;

import java.util.*;

public class Creature implements IMapElement {
    private Vector2d position;
    private Energy energy;
    private Rotation rotation = Rotation.R0;
    private WorldMap worldMap;
    private Genotype genotype;
    private Random random = new Random();
    private Boolean multiplied = true;
    private Boolean moved = false;
    private Id id;
    private LinkedList<Creature> children = new LinkedList<Creature>();
    private int birth = 0;
    private int lifespan = 0;
    private int death = -1;

    Creature(Creature creature) {
        position = creature.position;
        worldMap = creature.worldMap;
        energy = creature.energy;
        genotype = creature.genotype;
        rotation = creature.rotation;
        id = new Id(worldMap.creatureID++);
    }

    Creature(WorldMap _map, Vector2d _position, int _startEnergy) {
        position = new Vector2d(_position);
        worldMap = _map;
        energy = new Energy(_startEnergy, _startEnergy);
        genotype = new Genotype();
        id = new Id(worldMap.creatureID++);
        chooseRotation();
    }

    Creature(WorldMap _map, Vector2d _position, int _startEnergy, int _energyValue, Genotype _genotype, Creature creature1, Creature creature2) {
        position = new Vector2d(_position);
        worldMap = _map;
        energy = new Energy(_startEnergy, _energyValue);
        genotype = _genotype;
        rotation = genotype.getRotation();
        id = new Id(worldMap.creatureID++, creature1.getId().own, creature2.getId().own);
    }

    Creature(WorldMap _map, Vector2d _position, int _startEnergy, int _energyValue, Genotype _genotype, Creature creature1, Creature creature2, int iteration) {
        this(_map, _position, _startEnergy, _energyValue, _genotype, creature1, creature2);
        birth = iteration;
    }

    private void rotate(Rotation rotation) {
        this.rotation = this.rotation.rotate(rotation);
    }

    public void moveForward() {
        Vector2d positionOld = new Vector2d(position);
        position = position.add(rotation.getUnitVector());
        position = worldMap.correctPos(position);
        worldMap.positionChanged(positionOld, this);
        moved = true;
    }


    public Creature breedingWith(Creature partner, int iteration) {
        if (ableToBreed() && partner.ableToBreed()) {
            int parentEnergy = breedEnergy() + partner.breedEnergy();
            Genotype childGenotype = genotype.combineGenotype(partner.genotype);
            Creature child = new Creature(worldMap, worldMap.findFreePosForChildNear(position), energy.start, parentEnergy, childGenotype, this, partner, iteration);
            children.add(child);
            return child;
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
        multiplied = false;
    }

    public boolean ableToBreed() {
        return energy.value >= energy.start / 2 && !multiplied;
    }

    public void setPosition(Vector2d position) {
        this.position = new Vector2d(position);
    }

    public void setPosition(int x, int y) {
        this.position = new Vector2d(x, y);
    }

    public Vector2d getPosition() {
        return new Vector2d(position);
    }

    public String toString() {
        return rotation.toString();
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Id getId() { return id;
    }

    public void setId(Id id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Creature)) return false;

        Creature creature = (Creature) o;

        if (!genotype.equals(creature.genotype)) return false;
        return id.equals(creature.id);
    }

    @Override
    public int hashCode() {
        int result = genotype.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public int getBirth() {
        return birth;
    }

    public Energy getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public Boolean getMultiplied() {
        return multiplied;
    }

    public Boolean getMoved() {
        return moved;
    }

    public LinkedList<Creature> getChildren() {
        return children;
    }

    public void setNotMoved() {
        this.moved = false;
    }
    public int getLifespan() {
        return lifespan;
    }

    public void incrementLifespan() {
        this.lifespan++;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }
}
