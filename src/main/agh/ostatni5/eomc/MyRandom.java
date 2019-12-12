package agh.ostatni5.eomc;

import java.util.Random;

public class MyRandom extends Random {
    MyRandom(){
        super();
    }
    public Vector2d randomPos(Vector2d lowerV, Vector2d higherV) {
        Vector2d area = higherV.subtract(lowerV);
        int x = this.nextInt(area.x + 1);
        int y = this.nextInt(area.y + 1);
        Vector2d randPos = new Vector2d(x, y);
        randPos = randPos.add(lowerV);
        return randPos;
    }
}
