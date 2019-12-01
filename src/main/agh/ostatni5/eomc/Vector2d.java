package agh.ostatni5.eomc;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Vector2d(Vector2d a){
        this.x = a.x;
        this.y = a.y;
    }
    public String toString(){
        return "("+ this.x+","+ this.y+")";
    }
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;

        return that.x==this.x && that.y==this.y;
    }
    //@Override
//    public int hashCode() {
//        int hash = 13;
//        hash += this.x * 31;
//        hash += this.y * 17;
//        return hash;
//    }
//
    //    public boolean precedes(Vector2d a){
//        return !this.equals(a) &&a.x <= this.x && a.y <= this.y;
//    }
//    public boolean follows(Vector2d a){
//        return !this.equals(a) && a.x >= this.x && a.y >= this.y;
//    }
    public boolean precedes(Vector2d a){
        return a.x <= this.x && a.y <= this.y;
    }
    public boolean follows(Vector2d a){
        return a.x >= this.x && a.y >= this.y;
    }

    public Vector2d upperRight(Vector2d a){
        return  new Vector2d(Math.max(this.x,a.x),Math.max(this.y,a.y));
    }
    public Vector2d lowerLeft(Vector2d a){
        return  new Vector2d(Math.min(this.x,a.x),Math.min(this.y,a.y));
    }
    public Vector2d add(Vector2d a){
        return  new Vector2d(this.x+a.x,this.y+a.y);
    }
    public Vector2d subtract(Vector2d a){
        return  new Vector2d(this.x-a.x,this.y-a.y);
    }
    public Vector2d opposite(){
        return  new Vector2d(this.x*-1,this.y*-1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}