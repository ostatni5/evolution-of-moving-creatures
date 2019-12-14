package agh.ostatni5.eomc;

import java.awt.*;

public class Grass  implements IMapElement{
    Vector2d position;

    Grass(int x, int y ){
        position = new Vector2d(x,y);
    }
    Grass(Vector2d _position){
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
