package entity;

import java.awt.Color;
import java.io.File;
import java.util.Set;

import Enemy.PiercingBullet;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import inventory_develop.Bomb;
import Enemy.PiercingBulletPool;
// Sound Operator
import Sound_Operator.SoundManager;
// Import PlayerGrowth class
import Enemy.PlayerGrowth;
// Import NumberOfBullet class
import inventory_develop.NumberOfBullet;
// Import ShipStatus class
import inventory_develop.ItemBarrierAndHeart;
import inventory_develop.ShipStatus;
/**
 * Implements a ship, to be controlled by the player.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Ship extends Entity {
	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;
	/** PlayerGrowth 인스턴스 / PlayerGrowth instance */
	private PlayerGrowth growth;
	/** ShipStaus instance*/
	private ShipStatus shipStatus;
	/** Item */
	private ItemBarrierAndHeart item;
	// Sound Operator
	private static SoundManager sm;
	/** NumberOfBullet instance*/
	private NumberOfBullet numberOfBullet;

	/**
	 * Constructor, establishes the ship's properties.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	//Edit by Enemy
	public Ship(final int positionX, final int positionY) {
		super(positionX, positionY - 50, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = SpriteType.Ship;

		// Create PlayerGrowth object and set initial stats
		this.growth = new PlayerGrowth();  // PlayerGrowth 객체를 먼저 초기화

		this.shipStatus = new ShipStatus();
		shipStatus.loadStatus();

		//  Now use the initialized growth object
		this.shootingCooldown = Core.getCooldown(growth.getShootingDelay());

		this.destructionCooldown = Core.getCooldown(1000);

		this.numberOfBullet = new NumberOfBullet();
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += growth.getMoveSpeed(); //  Use PlayerGrowth for movement speed
	} //Edit by Enemy


	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= growth.getMoveSpeed(); // Use PlayerGrowth for movement speed
	} //Edit by Enemy

	/**
	 * Shoots a bullet upwards.
	 *
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 *
	 * You can set Number of enemies the bullet can pierce at here.
	 */
	//Edit by Enemy and Inventory
	public final boolean shoot(final Set<PiercingBullet> bullets) {

		if (this.shootingCooldown.checkFinished()) {

			this.shootingCooldown.reset(); // Reset cooldown after shooting

			// Sound Operator, Apply a Shooting sound
			sm = SoundManager.getInstance();
			sm.playES("My_Gun_Shot");

			// Use NumberOfBullet to generate bullets
			Set<PiercingBullet> newBullets = numberOfBullet.addBullet(
					positionX + this.width / 2,
					positionY,
					growth.getBulletSpeed(), // Use PlayerGrowth for bullet speed
					Bomb.getCanShoot()
			);

			// now can't shoot bomb
			Bomb.setCanShoot(false);

			// Add new bullets to the set
			bullets.addAll(newBullets);

			return true;
		}
		return false;
	}





	/**
	 * Updates status of the ship.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
		// Sound Operator
		sm = SoundManager.getInstance();
		sm.playES("ally_airship_damage");
	}

	/**
	 * Checks if the ship is destroyed.
	 *
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}
	/**
	 * 스탯을 증가시키는 메서드들 (PlayerGrowth 클래스 사용)
	 * Methods to increase stats (using PlayerGrowth)
	 */

	// Increases health
	//Edit by Enemy
	public void increaseHealth(int increment) {
		growth.increaseHealth(increment);
	}

	//  Increases movement speed
	//Edit by Enemy
	public void increaseMoveSpeed() {
		growth.increaseMoveSpeed(shipStatus.getSpeedIn());
	}

	// Increases bullet speed
	//Edit by Enemy
	public void increaseBulletSpeed() {
		growth.increaseBulletSpeed(shipStatus.getBulletSpeedIn());
	}

	//  Decreases shooting delay
	//Edit by Enemy
	public void decreaseShootingDelay() {
		growth.decreaseShootingDelay(shipStatus.getSuootingInIn());
		System.out.println(growth.getShootingDelay());
		this.shootingCooldown = Core.getCooldown(growth.getShootingDelay()); // Apply new shooting delay
	}

	/**
	 * Getter for the ship's speed.
	 *
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return growth.getMoveSpeed();
	}

	/**
	 * Calculates and returns the attack speed in bullets per second.
	 *
	 * @return Attack speed (bullets per second).
	 */
	public final double getAttackSpeed() {
		return 1000.0 / growth.getShootingDelay();
	}

	public PlayerGrowth getPlayerGrowth() {
		return growth;
	}	// Team Inventory(Item)
}
