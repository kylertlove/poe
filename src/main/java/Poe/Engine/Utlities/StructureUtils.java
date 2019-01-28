package Poe.Engine.Utlities;

import Poe.Models.Structures.Building;
import Poe.Models.Structures.Wall;
import Poe.World.World;

/**
 * Structure Utility Class
 */
public class StructureUtils {

    /**
     * Verify the structure is able to be built at the coordinates
     * @return boolean
     */
    public static void tryToBuildBuilding(float x, float y) {
        Building building = new Building(x, y, MathUtils.getRandomNumberFromRange(3, 10), MathUtils.getRandomNumberFromRange(3, 10));
        PoeLogger.logger.info("Generating Building @ X:" + x + ", Y:" + y);
        if(World.walls.size() == 0) {
            Wall[] wallArray = building.buildStructure();
            for (int wallIndex = 0; wallIndex < wallArray.length; wallIndex++) {
                World.walls.put(World.currentLevel.generateId(), wallArray[wallIndex]);
            }
        } else {
            if(!CollisionDetector.isCollidedWithList(building, World.walls)) {
                Wall[] wallArray = building.buildStructure();
                for (int wallIndex = 0; wallIndex < wallArray.length; wallIndex++) {
                    World.walls.put(World.currentLevel.generateId(), wallArray[wallIndex]);
                }
            }
        }
    }
}
