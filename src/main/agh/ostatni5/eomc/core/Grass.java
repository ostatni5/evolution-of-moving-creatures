package agh.ostatni5.eomc.core;

public class Grass  implements IMapElement{
    private Vector2d position;
    public Grass(Vector2d _position){
        position = new Vector2d(_position);
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }
    @Override
    public String toString() {
        return "G";
    }
}
