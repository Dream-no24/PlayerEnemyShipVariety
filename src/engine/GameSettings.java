package engine;

/**
 * Implements an object that stores a single game's difficulty settings.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameSettings {

	/** Width of the level's enemy formation. */
	private int formationWidth;
	/** Height of the level's enemy formation. */
	private int formationHeight;
	/** Speed of the enemies, function of the remaining number. */
	private int baseSpeed;
	/** Frequency of enemy shootings, +/- 30%. */
	private int shootingFrequency;
	/** Level Design team modification
	 * Number of enemy ships waves during the level **/
	private int wavesNumber;
	/** Flag to indicate boss level */
	private boolean isBossLevel;

	/**
	 * Constructor.
	 * 
	 * @param formationWidth
	 *            Width of the level's enemy formation.
	 * @param formationHeight
	 *            Height of the level's enemy formation.
	 * @param baseSpeed
	 *            Speed of the enemies.
	 * @param shootingFrequency
	 *            Frequency of enemy shootings, +/- 30%.
	 * @param wavesNumber
	 * 				Number of waves in the level (Added by the Level Design team)
	 * @param isBossLevel
	 * 				Indicates if this level is a boss level.
	 */
	public GameSettings(final int formationWidth, final int formationHeight,
                        final int baseSpeed, final int shootingFrequency, final int wavesNumber, final boolean isBossLevel) {
		this.formationWidth = formationWidth;
		this.formationHeight = formationHeight;
		this.baseSpeed = baseSpeed;
		this.shootingFrequency = shootingFrequency;

		/** Added by the Level Design team **/
		this.wavesNumber = wavesNumber;

		this.isBossLevel = isBossLevel;
    }

	/**
	 * @return isBossLevel
	 */
	public final boolean isBossLevel() {
		return isBossLevel;
	}

	/**
	 * @return the formationWidth
	 */
	public final int getFormationWidth() {
		return formationWidth;
	}

	/**
	 * @return the formationHeight
	 */
	public final int getFormationHeight() {
		return formationHeight;
	}

	/**
	 * @return the baseSpeed
	 */
	public final int getBaseSpeed() {
		return baseSpeed;
	}

	/**
	 * @return the shootingFrequency
	 */
	public final int getShootingFrequency() {
		return shootingFrequency;
	}

	/**
	 * Added by the Level Design team
	 * @return the wavesNumber
	 */
	public final int getWavesNumber() {
		return wavesNumber;
	}

}
