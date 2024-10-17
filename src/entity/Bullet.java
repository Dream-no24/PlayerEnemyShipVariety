package entity;

import java.awt.Color;

import clove.AchievementConditions;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import inventory_develop.Bomb;

import screen.GameScreen;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Bullet extends Entity {

	/**
	 * Speed of the bullet, positive or negative depending on direction -
	 * positive is down.
	 */
	private int speed;
	//Ctrl S
	/**
	 * Check if there is count on the bullet.
	 * if hit occur then checkCount will be false
	 */
	protected boolean checkCount;
	//Ctrl S
	/**
	 * give unique id for certain shot of bullets
	 */
	protected int fire_id;
	/**
	 * Constructor, establishes the bullet's properties.
	 * 
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */
	public Bullet(final int positionX, final int positionY, final int speed) {
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);
		//CtrlS
		this.checkCount = true;
		//CtrlS
		this.speed = speed;
		setSprite();
	}


	/**
	 * Sets correct sprite for the bullet, based on speed.
	 */
	public final void setSprite() {

		if (speed < 0) {
			if(Bomb.getIsBomb() && Bomb.getCanShoot())
				this.spriteType = SpriteType.ItemBomb;
			else
				this.spriteType = SpriteType.Bullet;
		}
		else
			this.spriteType = SpriteType.EnemyBullet;
	}

	/**
	 * Updates the bullet's position.
	 */
	public final void update() {
		this.positionY += this.speed;
	}

	/**
	 * Setter of the speed of the bullet.
	 * 
	 * @param speed
	 *            New speed of the bullet.
	 */
	public final void setSpeed(final int speed) {
		this.speed = speed;
	}

	/**
	 * Getter for the speed of the bullet.
	 * 
	 * @return Speed of the bullet.
	 */
	public final int getSpeed() {
		return this.speed;
	}

	/**
	 * Getter for the checkCount of the bullet.
	 *
	 * @return checkCount of the bullet.
	 */
	public final boolean isCheckCount() { return this.checkCount; }

	/**
	 * Setter for the checkCount of the bullet.
	 *
	 *  @param checkCount
	 * 	          New checkCount of the bullet.
	 */
	public final void setCheckCount(final boolean checkCount) { this.checkCount = checkCount; }

	/**
	 * Getter for the fire_id of the bullet
	 *
	 * @return Get fire_id of the bullet.
	 */
	public final int getFire_id() { return this.fire_id; }
	/**
	 * Setter for the fire_id of the bullet.
	 *
	 *  @param id
	 * 	          New fire_id of the bullet.
	 */
	public final void setFire_id(final int id) { this.fire_id = id; }

	/**
	 * Notify the KillStreakSystem about the bullet's hit or miss.
	 *
	 * @param killStreakSystem
	 *            The instance of KillStreakSystem to notify.
	 */
	public final void notifyKillStreak(AchievementConditions killStreakSystem) {
		if (this.isCheckCount()) {
			AchievementConditions.incrementKillCount();
		} else {
			AchievementConditions.resetKillCount();
		}
	}
}
