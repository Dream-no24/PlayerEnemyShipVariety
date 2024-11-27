package Enemy;

import java.awt.Color;

import engine.DrawManager;
import entity.Bullet;
import entity.Entity;

/**
 * The PiercingBullet class extends the Bullet class to implement a bullet
 * that can pierce through multiple enemies based solely on piercing count.
 */
public class PiercingBullet extends Bullet {

    // Variable to track how many enemies the bullet can pierce.
    private int piercingCount;

    /**
     * Constructor for PiercingBullet.
     *
     * @param positionX Initial X position of the bullet.
     * @param positionY Initial Y position of the bullet.
     * @param speed Speed of the bullet, positive is down, negative is up.
     * @param piercingCount Number of enemies the bullet can pierce.
     */
    public PiercingBullet(final int positionX, final int positionY, final int speed, int piercingCount, final Color color) {
        super(positionX, positionY, speed, color);  // Piercing bullets do not use isPiercing flag anymore.
        this.piercingCount = piercingCount;
        setSprite();    // team Inventory
    }

    /**
     * Handles the logic when the bullet collides with an entity.
     * Reduces the piercing count and destroys the bullet when piercing is exhausted.
     *
     * @param entity The entity the bullet collided with.
     */
    public void onCollision(Entity entity) {
        this.piercingCount--;
        if (this.piercingCount <= 0) {
            this.destroy(); // Destroys the bullet when it can no longer pierce.
        }
    }

    /**
     * Getter for the number of remaining piercings.
     *
     * @return The remaining piercings.
     */
    public int getPiercingCount() {
        return piercingCount;
    }

    /**
     * Setter for the piercing count.
     *
     * @param piercingCount The new number of piercings the bullet can perform.
     */
    public void setPiercingCount(int piercingCount) {
        this.piercingCount = piercingCount;
    }

    /**
     * Destroys the ship, causing an explosion.
     */
    public void destroy() {
        this.spriteType = DrawManager.SpriteType.Explosion;
    }
}
