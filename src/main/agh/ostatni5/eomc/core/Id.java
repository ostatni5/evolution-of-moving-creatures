package agh.ostatni5.eomc.core;

public class Id {
    public int own;
    public int parent1 = 0;
    public int parent2 = 0;

    public Id (Id id)
    {
        this.own=id.own;
        this.parent1=id.parent1;
        this.parent2=id.parent2;
    }

    public Id(int own) {
        this.own = own;
    }

    public Id(int own, int parent1, int parent2) {
        this.own = own;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;

        Id id = (Id) o;

        if (own != id.own) return false;
        if (parent1 != id.parent1) return false;
        return parent2 == id.parent2;
    }

    @Override
    public int hashCode() {
        int result = own;
        result = 31 * result + parent1;
        result = 31 * result + parent2;
        return result;
    }
}
