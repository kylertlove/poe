package Poe.Level;

import Poe.Models.Entities.Entity;
import Poe.Models.Structures.Structure;

import java.util.List;
import java.util.Map;

public interface ILevelBuilder {

    void createWalls();

    Map<Integer, Entity> createEnemies();

    String getLevel();

}
