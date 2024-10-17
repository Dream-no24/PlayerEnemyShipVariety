package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import CtrlS.CurrencyManager;
import CtrlS.RoundState;
import CtrlS.ReceiptScreen;
import CtrlS.UpgradeManager;
import Sound_Operator.SoundManager;
import level_design.Background;
import clove.AchievementConditions;
import clove.AchievementManager;
import screen.*;
import twoplayermode.TwoPlayerMode;


/**
 * Implements core game logic.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class Core {

	/** Width of current screen. */
	private static final int WIDTH = 630;
	/** Height of current screen. */
	private static final int HEIGHT = 720;
	/** Max fps of current screen. */
	private static final int FPS = 60;

	/** Max lives. */
	public static final int MAX_LIVES = 3; // TEAM CLOVER: Fixed MAX_LIVES from private to public for usage in achievement
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	public static final int NUM_LEVELS = 7; // TEAM CLOVER : Fixed NUM_LEVELS from privated to public for usage in achievement
	
	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 2000, 1);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2 =
			new GameSettings(5, 5, 50, 2500, 1);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3 =
			new GameSettings(1, 1, -8, 500, 1);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4 =
			new GameSettings(6, 6, 30, 1500, 2);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5 =
			new GameSettings(7, 6, 20, 1000, 2);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6 =
			new GameSettings(7, 7, 10, 1000, 3);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7 =
			new GameSettings(8, 7, 2, 500, 1);
	
	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;
	// Sound Operator
	private static SoundManager sm;
    private static AchievementManager achievementManager; // Team CLOVER
	private static AchievementConditions achievementConditions;

	/**
	 * Test implementation.
	 * 
	 * @param args
	 *            Program args, ignored.
	 */

	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());
			// Sound Operator
			sm = SoundManager.getInstance();

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

			// TEAM CLOVER : Added log to check if function is working
			System.out.println("Initializing AchievementManager...");
			achievementManager = new AchievementManager(DrawManager.getInstance());
			System.out.println("AchievementManager initialized!");
			achievementConditions = new AchievementConditions();

			// CtrlS: Make instance of Upgrade Manager
			Core.getUpgradeManager();

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		/** ### TEAM INTERNATIONAL ###*/
		/** Initialize singleton instance of a background*/
		Background.getInstance().initialize(frame);

		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1);
		gameSettings.add(SETTINGS_LEVEL_2);
		gameSettings.add(SETTINGS_LEVEL_3);
		gameSettings.add(SETTINGS_LEVEL_4);
		gameSettings.add(SETTINGS_LEVEL_5);
		gameSettings.add(SETTINGS_LEVEL_6);
		gameSettings.add(SETTINGS_LEVEL_7);
		
		GameState gameState;
		RoundState roundState;

		int returnCode = 1;
		do {
			// Add playtime parameter - Soomin Lee / TeamHUD
			// Add hitCount parameter - Ctrl S
			// Add coinItemsCollected parameter - Ctrl S
			gameState = new GameState(1, 0, MAX_LIVES, 0,0, 0, 0, 0, 0, 0, 0);
			switch (returnCode) {
			case 1:
				// Main menu.
                currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing title screen.");
				break;
			case 2:
				// Game & score.
				LOGGER.info("Starting inGameBGM");
				// Sound Operator
				sm.playES("start_button_ES");
				sm.playBGM("inGame_bgm");

				do {
					// One extra live every few levels.
					boolean bonusLife = gameState.getLevel()
							% EXTRA_LIFE_FRECUENCY == 0
							&& gameState.getLivesRemaining() < MAX_LIVES;

					GameState prevState = gameState;
					currentScreen = new GameScreen(gameState,
							gameSettings.get(gameState.getLevel() - 1),
							bonusLife, width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " game screen at " + FPS + " fps.");
					frame.setScreen(currentScreen);
					LOGGER.info("Closing game screen.");


					achievementManager.updateAchievements(currentScreen); // TEAM CLOVER : Achievement

					gameState = ((GameScreen) currentScreen).getGameState();

					roundState = new RoundState(prevState, gameState);

					// Add playtime parameter - Soomin Lee / TeamHUD
					gameState = new GameState(gameState.getLevel() + 1,
							gameState.getScore(),
							gameState.getLivesRemaining(),
							gameState.getLivesTwoRemaining(),
							gameState.getBulletsShot(),
							gameState.getShipsDestroyed(),
							gameState.getTime(),
							gameState.getCoin() + roundState.getRoundCoin(),
							gameState.getGem(),
							gameState.getHitCount(),
							gameState.getCoinItemsCollected());
          			LOGGER.info("Round Coin: " + roundState.getRoundCoin());
					LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
					LOGGER.info("Round Time: " + roundState.getRoundTime());

					// Show receiptScreen
					// If it is not the last round and the game is not over
					// Ctrl-S
					if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
						LOGGER.info("loading receiptScreen");
						currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);

						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " receipt screen at " + FPS + " fps.");
						frame.setScreen(currentScreen);
						LOGGER.info("Closing receiptScreen.");
					}

                    if (achievementManager != null) { // TEAM CLOVER : Added code
                        achievementManager.updateAchievements(currentScreen);
                    }

				} while (gameState.getLivesRemaining() > 0
						&& gameState.getLevel() <= NUM_LEVELS);

				LOGGER.info("Stop InGameBGM");
				// Sound Operator
				sm.stopAllBGM();

				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " score screen at " + FPS + " fps, with a score of "
						+ gameState.getScore() + ", "
						+ gameState.getLivesRemaining() + " lives remaining, "
						+ gameState.getBulletsShot() + " bullets shot and "
						+ gameState.getShipsDestroyed() + " ships destroyed.");
				currentScreen = new ScoreScreen(width, height, FPS, gameState);
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing score screen.");
				break;
			case 3:
				// High scores.
				currentScreen = new HighScoreScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing high score screen.");
				break;
			case 4:
				LOGGER.info("Starting inGameBGM");
				// Sound Operator
				sm.playES("start_button_ES");
				sm.playBGM("inGame_bgm");

				do {
					if (gameSettings == null || gameSettings.isEmpty()) {
						gameSettings = new ArrayList<>();
						gameSettings.add(SETTINGS_LEVEL_1);
						gameSettings.add(SETTINGS_LEVEL_2);
						gameSettings.add(SETTINGS_LEVEL_3);
						gameSettings.add(SETTINGS_LEVEL_4);
						gameSettings.add(SETTINGS_LEVEL_5);
						gameSettings.add(SETTINGS_LEVEL_6);
						gameSettings.add(SETTINGS_LEVEL_7);
					}

					GameSettings currentGameSettings = gameSettings.get(gameState.getLevel() - 1);

					int fps = FPS;
					boolean bonusLife = gameState.getLevel() % EXTRA_LIFE_FRECUENCY == 0 &&
							(gameState.getLivesRemaining() < MAX_LIVES || gameState.getLivesTwoRemaining() < MAX_LIVES);

					GameState prevState = gameState;

					// TwoPlayerMode의 생성자를 호출할 때 필요한 매개변수를 모두 전달
					currentScreen = new TwoPlayerMode(gameState, currentGameSettings, bonusLife, width, height, fps);




					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " game screen at " + FPS + " fps.");
					frame.setScreen(currentScreen);
					LOGGER.info("Closing game screen.");


					achievementManager.updateAchievements(currentScreen); // TEAM CLOVER : Achievement

					gameState = ((TwoPlayerMode) currentScreen).getGameState();

					roundState = new RoundState(prevState, gameState);

					// Add playtime parameter - Soomin Lee / TeamHUD
					gameState = new GameState(gameState.getLevel() + 1,
							gameState.getScore(),
							gameState.getLivesRemaining(),
							gameState.getLivesTwoRemaining(),
							gameState.getBulletsShot(),
							gameState.getShipsDestroyed(),
							gameState.getTime(),
							gameState.getCoin() + roundState.getRoundCoin(),
							gameState.getGem(),
							gameState.getHitCount(),
							gameState.getCoinItemsCollected());
					LOGGER.info("Round Coin: " + roundState.getRoundCoin());
					LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
					LOGGER.info("Round Time: " + roundState.getRoundTime());

					// Show receiptScreen
					// If it is not the last round and the game is not over
					// Ctrl-S
					if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
						LOGGER.info("loading receiptScreen");
						currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);

						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " receipt screen at " + FPS + " fps.");
						frame.setScreen(currentScreen);
						LOGGER.info("Closing receiptScreen.");
					}

					if (achievementManager != null) { // TEAM CLOVER : Added code
						achievementManager.updateAchievements(currentScreen);
					}

				} while ((gameState.getLivesRemaining() > 0 || gameState.getLivesTwoRemaining() > 0) && gameState.getLevel() <= NUM_LEVELS);

				LOGGER.info("Stop InGameBGM");
				// Sound Operator
				sm.stopAllBGM();

				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " score screen at " + FPS + " fps, with a score of "
						+ gameState.getScore() + ", "
						+ gameState.getLivesRemaining() + " lives remaining, "
						+ gameState.getBulletsShot() + " bullets shot and "
						+ gameState.getShipsDestroyed() + " ships destroyed.");
				currentScreen = new ScoreScreen(width, height, FPS, gameState);
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing score screen.");
				break;
			case 5: // 7 -> 5 replaced by Starter
				// Recent Records.
				currentScreen = new RecordScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " recent record screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing recent record screen.");
				break;
			default:
				break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 * 
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 * 
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 * 
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 * 
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
			final int variance) {
		return new Cooldown(milliseconds, variance);
	}

	/**
	 * Controls access to the currency manager.
	 *
	 * @return Application currency manager.
	 */
	// Team-Ctrl-S(Currency)
	public static CurrencyManager getCurrencyManager() {
		return CurrencyManager.getInstance();
	}

	/**
	 * Controls access to the currency manager.
	 *
	 * @return Application currency manager.
	 */
	// Team-Ctrl-S(Currency)
	public static UpgradeManager getUpgradeManager() {
		return UpgradeManager.getInstance();
	}
}
