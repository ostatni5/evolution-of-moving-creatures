package agh.ostatni5.eomc;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Creature creature);
}
