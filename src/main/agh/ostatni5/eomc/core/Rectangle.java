package agh.ostatni5.eomc.core;

import agh.ostatni5.eomc.my.MyRandom;

public class Rectangle {
    private MyRandom random = new MyRandom();
    final public int width;
    final public int height;
    final public Vector2d[] corners = new Vector2d[4];
    public Rectangle(int _width,int _height, Vector2d startPoint){
        width=_width;
        height=_height;
        corners[0]=startPoint;
        corners[1]= new Vector2d(startPoint.x+width-1,startPoint.y);
        corners[2]= new Vector2d(startPoint.x+width-1,startPoint.y+height-1);
        corners[3]= new Vector2d(startPoint.x,startPoint.y+height-1);
    };
    public Rectangle(Rectangle rectangle)
    {
        width=rectangle.width;
        height=rectangle.height;
        for (int i = 0; i < 4 ; i++) {
            corners[i]= rectangle.corners[i];
        }
    }

    public boolean isIn(Vector2d v)
    {
        return corners[0].follows(v) && corners[2].precedes(v);
    }
    public Vector2d randomIn(){
       return random.randomPos(corners[0], corners[2]);
    }
}
//3--2
//|  |
//0--1