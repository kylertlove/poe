package Poe.World;

import Poe.Engine.GameLoop;
import Poe.Engine.Gui.DebugScreen;
import Poe.Engine.Gui.PauseMenu;
import Poe.Engine.Renderer;
import Poe.Engine.Utlities.CollisionDetector;
import Poe.Engine.Utlities.GameUtils;
import Poe.GameObjects.Entities.Entity;
import Poe.GameObjects.Entities.Player;
import Poe.GameObjects.GameObject;
import Poe.GameObjects.Item.Weapons.Projectile.Projectile;
import Poe.GameObjects.Structures.Structure;
import Poe.World.Levels.LevelBuilder;
import Poe.World.Levels.Level1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class World {

    public static Player player = null;
    public static Map<Long, Structure> walls;
    public static Map<Long, Entity> enemies;
    public static LevelBuilder currentLevel;
    public static boolean debug = true;

    /** Game Initialization */
    public static void init() {
        currentLevel = new Level1();
        currentLevel.init();
        player = new Player();
    }

    /**
     * Game Update Function
     */
    public static void update() {
        //update player and camera position
        player.update();
        Renderer.updateCamera(player.X, player.Y);
        //update range weapons
        Arrays.stream(player.projectiles)
                .filter(Projectile.isProjectileActive)
                .forEach(Projectile::update);

        /**
         * Melee Attack.
         * isMeleeAttacking = Entity Value to update and render the attack.
         * isActive = Melee instance value if attack should provide damage to collided Entities
         * different so that long attacks dont hit on each tick
         */
        if(player.isMeleeAttacking()) {
            player.activeMeleeWeapon.update();
        }
        if(player.activeMeleeWeapon.isActive) {
            enemies.entrySet().stream()
                    .filter(set -> set.getValue().isTrackingEntity)
                    .forEach(entry -> player.activeMeleeWeapon.attack(entry.getValue()));
            player.activeMeleeWeapon.isActive = false;
        }
        enemies.forEach((index, entity) -> {
            entity.update();
            if(GameUtils.entityNearEntity(player, entity, entity.viewDistance)) {
                entity.isTrackingEntity = true;
                entity.trackingTarget(player);
            } else {
                entity.isTrackingEntity = false;
            }
            if(CollisionDetector.isCollided(player, entity)) {
                player.objectsCollidedWith.add(entity);
                entity.isAttackingEntity = true;
            } else {
                entity.isAttackingEntity = false;
            }
            Arrays.stream(player.projectiles)
                    .filter(Projectile.isProjectileActive)
                    .filter(projectile -> CollisionDetector.isCollided(projectile, entity))
                    .forEach(projectile -> {
                        projectile.destroy();
                        entity.recieveHit(projectile.getDamageAmount());
                    });
        });
        walls.forEach((index, structure) -> {
            if(CollisionDetector.isCollided(player, structure)) {
                player.objectsCollidedWith.add(structure);
            }
            Arrays.stream(player.projectiles)
                    .filter(Projectile.isProjectileActive)
                    .filter(projectile -> CollisionDetector.isCollided(projectile, structure))
                    .forEach(Projectile::destroy);

            //Make sure Entities cant run through walls
            enemies.entrySet()
                    .stream()
                    .filter(set -> set.getValue().isTrackingEntity)
                    .forEach(trackingEntity -> {
                        if(CollisionDetector.isCollided(trackingEntity.getValue(), structure)) {
                            CollisionDetector.updateMoveableSingle(trackingEntity.getValue(), structure);
                        }
                    });
        });
        CollisionDetector.updateMoveable(player, player.objectsCollidedWith);
        player.objectsCollidedWith = new ArrayList<>();
    }

    /**
     * Game Render Function
     */
    public static void render() {
        player.render();
        Arrays.stream(player.projectiles)
                .filter(Projectile.isProjectileActive)
                .forEach(GameObject::render);

        enemies.entrySet()
                .stream()
                .filter(entity -> GameUtils.isInBounds(entity.getValue()))
                .forEach(inBoundsEnemy -> inBoundsEnemy.getValue().render());

        walls.forEach((index, structure) -> {
            /**
             *TODO:
             * IsInBounds breaks since structures can be larger than screen size.
             * Need different Method to check for 'Any' part of GameObject inBounds
             */
            structure.render();
        });
        if(debug) {
            DebugScreen.writeToScreen();
        }
        if(GameLoop.paused) {
            PauseMenu.renderPauseMenu();
        }
    }
}
