package entity;

import Sound_Operator.SoundManager;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

import java.awt.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements the part of the Boss.
 */
public class BossParts extends Entity {
	/** Part of the Boss' health point */
	private int hp;
	private int maxHp;

	/** Cooldown between sprite changes. */
	private Cooldown animationCooldown;
	/** Checks if the part of the Boss has been hit by a bullet. */
	private Cooldown bossBActiveSkillCooldown;
	private Cooldown bossBActiveSkillDuration;
	private boolean isDestroyed;
	/** Values of the part of the Boss, in points, when destroyed. */
	private int pointValue;

	private static SoundManager sm;

	/** Speed reduction or increase multiplier (1.0 means normal speed). */
	private double speedMultiplier;
	private double defaultSpeedMultiplier;
	/** Checks if the ship is bombed */
	private boolean isChainExploded;

	/**
	 * Constructor, establishes the part's properties.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param spriteType
	 *            Sprite type, image corresponding to the part of the Boss.
	 */
	public BossParts(final int positionX, final int positionY, int BossWidth, int BossHeight,
					 final SpriteType spriteType, int hp) {
		super(positionX, positionY, BossWidth * 2, BossHeight * 2, determineColor(hp, hp));

		this.hp = hp;
		this.maxHp = hp;
		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.bossBActiveSkillCooldown = Core.getVariableCooldown(1000, 500);
		this.bossBActiveSkillDuration = Core.getVariableCooldown(2000, 1000);
		this.isDestroyed = false;
		this.speedMultiplier = 1.0; // default 1.0
		this.defaultSpeedMultiplier = 1.0;

		this.pointValue = 1000;
	}

	/**
	 * Getter for the score bonus if this part is destroyed.
	 *
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Moves the part of the Boss in the specified distance.
	 *
	 * @param distanceX
	 *            Distance to move in the X axis.
	 * @param distanceY
	 *            Distance to move in the Y axis.
	 */
	public final void move(final int distanceX, final int distanceY) {
		this.positionX += distanceX * this.getSpeedMultiplier();
		this.positionY += distanceY;
	}

	/**
	 * Updates attributes, mainly used for animation purposes.
	 */
	public final void update() {
		if (this.animationCooldown.checkFinished()) {
			this.animationCooldown.reset();

			switch (this.spriteType) {
				case BossACore1:
					this.spriteType = SpriteType.BossACore2;
					break;
				case BossACore2:
					this.spriteType = SpriteType.BossACore1;
					break;
				case BossALeft1:
					this.spriteType = SpriteType.BossALeft2;
					break;
				case BossALeft2:
					this.spriteType = SpriteType.BossALeft1;
					break;
				case BossARight1:
					this.spriteType = SpriteType.BossARight2;
					break;
				case BossARight2:
					this.spriteType = SpriteType.BossARight1;
					break;
				case BossBCore1:
					// Check skill cooldown and change sprite type to B3 which is B3.
					if (this.bossBActiveSkillCooldown.checkFinished()) {
						this.spriteType = SpriteType.BossBCoreDamaged;
						bossBActiveSkillDuration.reset();
					}
					else
						this.spriteType = SpriteType.BossBCore2;
					break;
				case BossBCore2:
					this.spriteType = SpriteType.BossBCore1;
					break;
				case BossBCoreDamaged:
					if (this.bossBActiveSkillDuration.checkFinished()) {
						this.spriteType = SpriteType.BossBCore1;
						bossBActiveSkillCooldown.reset();
					}
					else
						this.spriteType = SpriteType.BossBCoreDamaged;
				default:
					break;
			}
		}
	}

	/**
	 * Determine the color of the part of the Boss according to hp
	 * @param hp
	 * 			The Boss' hp
	 * @return color of Boss
	 */
	public static Color determineColor(int hp, int maxHp) {

		if (hp <= 0) return Color.WHITE;

		hp--;
		maxHp--;

		int red = 0, green = 0, blue = 0;
		double ratio = (double) hp * 4 / maxHp;

		if (hp >= (double) maxHp / 2) {
			blue = (int) (127 * (ratio - 2));
			green = 255 - (int) (127 * ((ratio - 2)));
		}else if (hp >= (double) maxHp / 4) {
			green = 255;
			red = 255 -(int) (255 * (ratio -1));
		}else {
			green = (int) (255 * ratio);
			red = 255;
		}

		red = Math.min(255, Math.max(0, red));
		green = Math.min(255, Math.max(0, green));
		blue = Math.min(255, Math.max(0, blue));

		return new Color(red, green, blue);
	}
	public final void changeColor() {
	}

	public static void hit(BossParts bossParts) {
		int hp = bossParts.getHp();
		hp -= 1;
		bossParts.setHp(hp);

		if (hp <= 0) {
			bossParts.destroy();
		}else{
			sm = SoundManager.getInstance();
			sm.playES("hit_enemy");

			SpriteType originalSprite = bossParts.getSpriteType();
			String originalSpriteName = originalSprite.name();
			String damagedSpriteName = originalSpriteName.replaceAll("\\d+$", "") + "Damaged"; // Search for sprite names that have 'Damaged'
			try {
				SpriteType damagedSpriteType = SpriteType.valueOf(damagedSpriteName);
				bossParts.setSpriteType(damagedSpriteType);

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						bossParts.setSpriteType(originalSprite);
					}
				}, 100);
			} catch (IllegalArgumentException e) {
				System.err.println("Damaged sprite type not found: " + damagedSpriteName);
			}
		}

		bossParts.setColor(determineColor(hp, bossParts.maxHp));
	}

	/**
	 * Destroys the part of the Boss, causing an explosion.
	 */
	public final void destroy() {
		this.isDestroyed = true;
		sm = SoundManager.getInstance();
		if(this.spriteType.toString().contains("Core")){
			sm.playES("boss_die");
		}
		else if(this.spriteType.toString().contains("Boss")){
			sm.playES("boss_part_destroy");
		}
		else {
			sm.playES("special_enemy_die");
		}

		this.spriteType = SpriteType.Explosion;
	}

	/**
	 * Checks if the part of the Boss has been destroyed.
	 *
	 * @return True if the part of the Boss has been destroyed.
	 */
	public final boolean isDestroyed() {
		return this.isDestroyed;
	}

	/**
	 * Getter for the Hp of this part of the Boss.
	 *
	 * @return Hp of the part of the Boss.
	 */
	public final int getHp() {
		return this.hp;
	}

	/**
	 * Setter for the Hp of the part of the Boss.
	 *
	 * @param hp
	 * 			New hp of the part of the Boss.
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * Getter for the current speed multiplier.
	 *
	 * @return The current speed multiplier.
	 */
	public double getSpeedMultiplier() {
		return this.speedMultiplier;
	}

	public void setSpeedMultiplier(double speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public void resetSpeedMultiplier() {
		this.speedMultiplier = this.defaultSpeedMultiplier;
	}

	/**
	 * Destroys ship, causing a chain explode.
	 */
	public final void chainExplode() { // Added by team Enemy
		destroy();
		setChainExploded(true);
		setHp(0);
	}

	/**
	 * Checks if the ship has been chain exploded.
	 *
	 * @return True if the ship has been chain exploded.
	 */
	public final boolean isChainExploded() {
		return this.isChainExploded;
	}

	/**
	 * Setter for enemy ship's isChainExploded to false.
	 */
	public final void setChainExploded(boolean isChainExploded) {
		this.isChainExploded = isChainExploded;
	}


}
