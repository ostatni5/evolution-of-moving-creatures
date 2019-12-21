package agh.ostatni5.eomc.core;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Creature creature);
}
