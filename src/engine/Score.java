package engine;

import clove.Statistics;

import java.io.IOException;
import java.util.logging.Level;
import java.time.LocalDate;

import screen.GameScreen;

/**
 * Implements a high score record.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Score implements Comparable<Score> {

	/** Player's name. */
	private String name;
	/** Score points. */
	private int score;

	private LocalDate currentDate;
	/** Integers for describe present Date */
	private int Year;
	private int Month;
	private int Day;
	/** String for storage present Date */
	private String Date;

	/** highest level that player reached */
    private int highestLevel;
	/** ship number that player destroyed */
	private int totalShipDestroyed;
	/** achievement number that player cleared */
	private int clearAchievementNumber;
	/** Time for Total level clear time(player has done) */
	private long playTime;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Player name, three letters.
	 * @param score
	 *            Player score.
	 * Added non-parameter elements for Date // Clove
	 * Added load statistics for save recent score // Clove
	 */
	public Score(final String name, final int score) {
		this.name = name;
		this.score = score;

		this.currentDate = LocalDate.now();
		this.Year = currentDate.getYear();
		this.Month = currentDate.getMonthValue();
		this.Day = currentDate.getDayOfMonth();
		this.Date = String.format("%d-%02d-%02d", Year, Month, Day);

        try{
			Statistics stat = new Statistics();
			stat = stat.loadUserData(stat);
			this.highestLevel = stat.getHighestLevel();
			this.totalShipDestroyed = stat.getTotalShipsDestroyed();
			this.clearAchievementNumber = stat.getClearAchievementNumber();
			this.playTime = stat.getTotalPlaytime();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Constructor for read/write Recent Record files
	 *
	 * @param name
	 * 				Player name but non-value parameter, just for overload constructor
	 * @param score
	 * 				Player score
	 * @param date
	 * 				Date (especially Time Player played the game.
	 * @param highestLevel
	 * 				Player reached level.
	 * @param totalShipDestroyed
	 * 				Number of Destroyed Ship.
	 * @param clearAchievementNumber
	 * 				Number of Cleared Achievement.
	 */
	public Score(final String name, final int score, final String date, int highestLevel,
				 final int totalShipDestroyed, final int clearAchievementNumber) {
		this.name = name;
		this.score = score;
		this.Date = date;
		this.highestLevel = highestLevel;
		this.totalShipDestroyed = totalShipDestroyed;
		this.clearAchievementNumber = clearAchievementNumber;
	}
	/**
	 * Constructor
	 */
	public Score(final String name, final int score, final long playTime) {
		this.name = name;
		this.score = score;
		this.playTime = playTime;
	}


	/**
	 * Getter for the player's name.
	 * 
	 * @return Name of the player.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Getter for the player's score.
	 * 
	 * @return High score.
	 */
	public final int getScore() { return this.score; }

	/**
	 * Getter for Date
	 *
	 * @return Object currentDate
	 */
	public final LocalDate getcurrentDate() { return this.currentDate; }

	/**
	 * Getter for String type Date
	 *
	 * @return Date
	 */
	public final String getDate() { return this.Date; }

	public long getPlayTime() { return playTime; }

	public final int getHighestLevel() { return this.highestLevel; }

	public final int getShipDestroyed() { return this.totalShipDestroyed; }

	public final int getClearAchievementNumber() { return this.clearAchievementNumber; }

	/**
	 * Orders the scores descending by score.
	 * 
	 * @param score
	 *            Score to compare the current one with.
	 * @return Comparison between the two scores. Positive if the current one is
	 *         smaller, positive if its bigger, zero if its the same.
	 */

	@Override
	public final int compareTo(final Score score) {
		/*int comparison = this.score < score.getScore() ? 1 : this.score > score
				.getScore() ? -1 : 0;*/
		int comparison = Integer.compare(score.score, this.score);
		if (comparison != 0) { return comparison; }

		return Long.compare(this.playTime, score.playTime);
	}

}
