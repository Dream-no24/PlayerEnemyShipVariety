package Enemy;

import engine.Core;
import java.io.IOException;

public class PlayerGrowth {

    //Player's base stats
    private int health;          //Health
    private static int moveSpeed = 2;       //Movement speed
    private static int bulletSpeed = -4;     // Bullet speed
    private static int shootingDelay = 750;   // Shooting delay

    //Constructor to set initial values
    public PlayerGrowth() {//  Base shooting delay is 750ms

        // CtrlS: set player growth based on upgrade_status.properties
        try {
            moveSpeed = Core.getUpgradeManager().getMovementSpeed();
            shootingDelay = Core.getUpgradeManager().getAttackSpeed();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Increases health
    public void increaseHealth(int increment) {
        this.health += increment;
    }

    //Increases movement speed
    public void increaseMoveSpeed(int increment) {
        this.moveSpeed += increment;
    }

    // Increases bullet speed (makes bullets faster)
    public void increaseBulletSpeed(int increment) {
        this.bulletSpeed -= increment; // Increase by subtracting (since negative speed)
    }

    // Decreases shooting delay (makes shooting faster)
    public void decreaseShootingDelay(int decrement) {
        this.shootingDelay -= decrement; //  Decrease delay for faster shooting
        if (this.shootingDelay < 100) {
            this.shootingDelay = 100; // Minimum shooting delay is 100ms
        }
    }

    // reset bullet speed
    //Edit by inventory
    public void ResetBulletSpeed(){
        bulletSpeed = -4;
    }

    // Returns current health
    public int getHealth() {
        return this.health;
    }

    //  Returns current movement speed
    public int getMoveSpeed() {
        return this.moveSpeed;
    }

    // Returns current bullet speed
    public int getBulletSpeed() {
        return this.bulletSpeed;
    }

    //  Returns current shooting delay
    public int getShootingDelay() {
        return this.shootingDelay;
    }

    // Prints player stats (for debugging)
    public void printStats() {
        System.out.println("Health: " + this.health);
        System.out.println("MoveSpeed: " + this.moveSpeed);
        System.out.println("BulletSpeed: " + this.bulletSpeed);
        System.out.println("ShootingDelay: " + this.shootingDelay + "ms");
    }
}
