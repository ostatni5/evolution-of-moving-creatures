package agh.ostatni5.eomc;

public class Rectangle {
    final public int width;
    final public int height;
    final public Vector2d[] corners = new Vector2d[4];
    Rectangle(int _width,int _height, Vector2d startPoint){
        width=_width;
        height=_height;
        corners[0]=startPoint;
        corners[1]= new Vector2d(startPoint.x+width,startPoint.y);
        corners[2]= new Vector2d(startPoint.x+width,startPoint.y+height);
        corners[3]= new Vector2d(startPoint.x,startPoint.y+height);
    };

    public boolean isIn(Vector2d v)
    {
        return corners[0].follows(v) && corners[2].precedes(v);
    }
}
//3--2
//|  |
//0--1