package agh.ostatni5.eomc;

public class DirectionParser {
    static public Vector2d toUnitVector(int direction){
        final Vector2d[] unitV = new Vector2d[]{
                new Vector2d(0, 1),
                new Vector2d(1, 1),
                new Vector2d(1, 0),
                new Vector2d(1, -1),
                new Vector2d(0, -1),
                new Vector2d(-1, -1),
                new Vector2d(-1, 0),
                new Vector2d(-1, 1)
        };
        return new Vector2d(unitV[direction].x,unitV[direction].y);
    }
}
