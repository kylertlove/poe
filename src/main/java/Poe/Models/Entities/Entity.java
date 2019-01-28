package Poe.Models.Entities;

import Poe.Engine.GameLoop;
import Poe.Models.GameObject;
import Poe.World.World;
import Poe.Engine.Utlities.GameUtils;
import Poe.Engine.Utlities.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Class for all "Living" GameObjects
 */
public class Entity extends GameObject {

    public boolean canMoveUp = true;
    public boolean canMoveDown = true;
    public boolean canMoveLeft = true;
    public boolean canMoveRight = true;
    public float viewDistance = 5;
    public float destinationX = 0;
    public float destinationY = 0;
    public float attackDamage = 1;
    public boolean isTrackingEntity = false;
    public boolean isAttackingPlayer = false;
    public boolean attackCooldownFinished = true;
    public List<GameObject> objectsCollidedWith = new ArrayList<>();

    /**
     * Set Entity to start Tracking passed Entity
     * @param entityToTrack
     */
    public void trackingTarget(Entity entityToTrack) {
        this.destinationX = entityToTrack.X;
        this.destinationY = entityToTrack.Y;
    }

    @Override
    public void update() {
        if(this.isTrackingEntity) {
            float xVal = 0;
            float yVal = 0;

            if(this.destinationX < X && canMoveLeft) {
                xVal -= this.velocity;
            } 
            if(this.destinationX > X && canMoveRight) {
                xVal += this.velocity;
            }
            if(this.destinationY < Y && canMoveDown) {
                yVal -= this.velocity;
            }
            if(this.destinationY > Y && canMoveUp){
                yVal += this.velocity;
            }

            X += xVal * GameLoop.getDelta();
            Y += yVal * GameLoop.getDelta();
            rotation = MathUtils.getAngle(this.destinationX, this.destinationY, X, Y);
            if((!canMoveDown || !canMoveLeft || !canMoveRight || !canMoveUp) 
            && isAttackingPlayer
            && attackCooldownFinished) {
                attackCooldownFinished = false;
                World.player.recieveHit(this.attackDamage);
                GameUtils.setTimeout(() -> {
                    attackCooldownFinished = true;
                }, 1200);
            }
        }
        canMoveLeft = true;
        canMoveUp = true;
        canMoveDown = true;
        canMoveRight = true;
    }
}
