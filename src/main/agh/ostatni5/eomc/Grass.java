package agh.ostatni5.eomc;

public class Grass  implements IMapElement{
    Vector2d position;

    Grass(Vector2d _position){
        position = _position;
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
