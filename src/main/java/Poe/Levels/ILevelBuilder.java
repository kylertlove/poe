package Poe.Levels;

public interface ILevelBuilder {

    void init();

    void createWalls();

    void createEnemies();

    long generateId();

    String getLevel();

}