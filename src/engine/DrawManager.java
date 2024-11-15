package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import CtrlS.RoundState;
import CtrlS.Gem;
import entity.AddSign;
import entity.Coin;
import inventory_develop.Bomb;
import screen.Screen;
import entity.Entity;

import level_design.Background;

import javax.imageio.ImageIO;

/**
* Manages screen drawing.
*
* @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
*
*/
public class DrawManager {

	/** Singleton instance of the class. */
	public static DrawManager instance;
	/** Current frame. */
	private static Frame frame;
	/** FileManager instance. */
	private static FileManager fileManager;
	/** Application logger. */
	private static Logger logger;
	/** Graphics context. */
	private static Graphics graphics;
	/** Buffer Graphics. */
	public static Graphics backBufferGraphics;	// Modifying Access Restrictor to public - Lee Hyun Woo
	/** Buffer image. */
	private static BufferedImage backBuffer;
	/** Normal sized font. */
	public static Font fontRegular;  // Modifying Access Restrictor to public - Lee Hyun Woo
	/** Normal sized font properties. */
	public static FontMetrics fontRegularMetrics; // Modifying Access Restrictor to public - Lee Hyun Woo
	/** Big sized font. */
	private static Font fontBig;
	/** Big sized font properties. */
	private static FontMetrics fontBigMetrics;

	/** ###TEAM INTERNATIONAL ### */
	private Background background;
	private BufferedImage backgroundImage;

	/** Sprite types mapped to their images. */
	private static Map<SpriteType, boolean[][]> spriteMap;

	/** Sprite types. */
	public static enum SpriteType {
		/** Player ship. */
		Ship,
		/** Destroyed player ship. */
		ShipDestroyed,
		/** Player bullet. */
		Bullet,
		/** Enemy bullet. */
		EnemyBullet,
		/** First enemy ship - first form. */
		EnemyShipA1,
		/** First enemy ship - second form. */
		EnemyShipA2,
		/** Second enemy ship - first form. */
		EnemyShipB1,
		/** Second enemy ship - second form. */
		EnemyShipB2,
		/** Third enemy ship - first form. */
		EnemyShipC1,
		/** Third enemy ship - second form. */
		EnemyShipC2,
		/** First explosive enemy ship - first form. */
		ExplosiveEnemyShip1, // Edited by Enemy
		/** First explosive enemy ship - second form. */
		ExplosiveEnemyShip2, // Edited by Enemy
		/** Bonus ship. */
		EnemyShipSpecial,
		/** Destroyed enemy ship. */
		Explosion,
		/**HEART Graphics Produced by Nice HUD Team*/
		Heart, // Please have the Nice HUD team fix it. - Enemy team
		/**Item*/
		Item, // by enemy team
		/**Boss*/
		Boss, // by enemy team
		/** Player Lives. */
		/** Item */
    	ItemHeart,
		ShipBarrierStatus,
		ItemCoin,
		ItemPierce,
		ItemBomb,
		ItemBarrier,
		ItemFeverTime,
		// Produced by Starter Team

        // Produced by Starter Team
		/** coin */
		Coin,
		/** add sign */
		AddSign,
		/** Gem - Added by CtrlS */
		Gem,
        ItemSpeedUp, ItemSpeedSlow, Obstacle,

		BossALeft1,
		BossAMiddle1,
		BossARight1,
		BossALeft2,
		BossAMiddle2,
		BossARight2,

		BossB1,
		BossB2,
		BossB3

	};

	/**
	* Private constructor.
	*
	* Modifying Access Restrictor to public
	* - HUDTeam - LeeHyunWoo
	*/
	public DrawManager() {
		fileManager = Core.getFileManager();
		logger = Core.getLogger();
		logger.info("Started loading resources.");

		try {
			spriteMap = new LinkedHashMap<SpriteType, boolean[][]>();
			spriteMap.put(SpriteType.Obstacle, new boolean[12][12]); // by Level Design Team
			spriteMap.put(SpriteType.Ship, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.Bullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyBullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyShipA1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipA2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC2, new boolean[12][8]);
			spriteMap.put(SpriteType.ExplosiveEnemyShip1, new boolean[12][8]); // Edited by Enemy
			spriteMap.put(SpriteType.ExplosiveEnemyShip2, new boolean[12][8]); // Edited by Enemy
			spriteMap.put(SpriteType.EnemyShipSpecial, new boolean[16][7]);
			spriteMap.put(SpriteType.Explosion, new boolean[13][7]);
			spriteMap.put(SpriteType.Heart, new boolean[13][8]);
			spriteMap.put(SpriteType.Boss, new boolean[24][16]); // by Enemy team
			spriteMap.put(SpriteType.Coin, new boolean[5][5]); // by Starter Team
			spriteMap.put(SpriteType.AddSign, new boolean[5][5]); // by Starter Team
			spriteMap.put(SpriteType.Gem, new boolean[7][6]); // CtrlS: res/graphics, line 20
			// by Item team
			spriteMap.put(SpriteType.ItemHeart, new boolean[7][5]);
			spriteMap.put(SpriteType.ItemBarrier, new boolean[9][10]);
			spriteMap.put(SpriteType.ItemBomb, new boolean[7][9]);
			spriteMap.put(SpriteType.ShipBarrierStatus, new boolean[13][8]);	// temporary
			spriteMap.put(SpriteType.ItemCoin, new boolean[7][7]);
			spriteMap.put(SpriteType.ItemFeverTime, new boolean[9][9]);
			spriteMap.put(SpriteType.ItemPierce, new boolean[7][7]);
			spriteMap.put(SpriteType.ItemSpeedUp, new boolean[9][9]);
			spriteMap.put(SpriteType.ItemSpeedSlow, new boolean[9][9]);
			// by Enemy team+
			spriteMap.put(SpriteType.BossALeft1, new boolean[12][24]);
			spriteMap.put(SpriteType.BossAMiddle1, new boolean[12][24]);
			spriteMap.put(SpriteType.BossARight1, new boolean[12][24]);
			spriteMap.put(SpriteType.BossALeft2, new boolean[12][24]);
			spriteMap.put(SpriteType.BossAMiddle2, new boolean[12][24]);
			spriteMap.put(SpriteType.BossARight2, new boolean[12][24]);
			spriteMap.put(SpriteType.BossB1, new boolean[30][36]);
			spriteMap.put(SpriteType.BossB2, new boolean[30][36]);
			spriteMap.put(SpriteType.BossB3, new boolean[30][36]);

			fileManager.loadSprite(spriteMap);
			logger.info("Finished loading the sprites.");

			// Font loading.
			fontRegular = fileManager.loadFont(14f);
			fontBig = fileManager.loadFont(24f);
			logger.info("Finished loading the fonts.");

		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}
	}

	/**
	 * Returns shared instance of DrawManager.
	 *
	 * @return Shared instance of DrawManager.
	 */
	public static DrawManager getInstance() {
		if (instance == null)
			instance = new DrawManager();
		return instance;
	}

	/**
	 * Sets the frame to draw the image on.
	 *
	 * @param currentFrame
	 *            Frame to draw on.
	 */
	public void setFrame(final Frame currentFrame) {
		frame = currentFrame;
	}

	/**
	 * First part of the drawing process. Initialices buffers, draws the
	 * background and prepares the images.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	public void initDrawing(final Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = frame.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		fontRegularMetrics = backBufferGraphics.getFontMetrics(fontRegular);
		fontBigMetrics = backBufferGraphics.getFontMetrics(fontBig);

		// drawBorders(screen);
		// drawGrid(screen);
	}

	/**
	 * Draws the completed drawing on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void completeDrawing(final Screen screen) {
		graphics.drawImage(backBuffer, frame.getInsets().left,
				frame.getInsets().top, frame);
	}

	/**
	 * Draws an entity, using the apropiate image.
	 *
	 * @param entity
	 *            Entity to be drawn.
	 * @param positionX
	 *            Coordinates for the left side of the image.
	 * @param positionY
	 *            Coordinates for the upper side of the image.
	 */
	public static void drawEntity(final Entity entity, final int positionX,
						   final int positionY) {

		try {
			boolean[][] image = spriteMap.get(entity.getSpriteType());

			backBufferGraphics.setColor(entity.getColor());
			for (int i = 0; i < image.length; i++)
				for (int j = 0; j < image[i].length; j++)
					if (image[i][j])
						backBufferGraphics.drawRect(positionX + i * 2, positionY
								+ j * 2, 1, 1);

		} catch(Exception e) {

			System.out.println(e);
			System.exit(1);
		}


	}

	/**
	 * For debugging purpouses, draws the canvas borders.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawBorders(final Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purpouses, draws a grid over the canvas.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawGrid(final Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * Draws current score on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Current score.
	 */
	public void drawScore(final Screen screen, final int score) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String scoreString = String.format("%04d", score);
		backBufferGraphics.drawString(scoreString, screen.getWidth() - 60, 25);
	}

	/**
	 * Draws number of remaining lives on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param lives
	 *            Current lives.
	 */
	public void drawLives(final Screen screen, final int lives) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
//		backBufferGraphics.drawString(Integer.toString(lives), 20, 25);

		Entity heart = new Entity(0, 0, 13 * 2, 8 * 2, Color.RED) {

		};
		heart.setSpriteType(SpriteType.Heart);

		for (int i = 0; i < lives; i++)
			drawEntity(heart, 20 + 30 * i, 10);
	}

	/**
	 * Draws a thick line from side to side of the screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param positionY
	 *            Y coordinate of the line.
	 */
	public void drawHorizontalLine(final Screen screen, final int positionY) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
		backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
				positionY + 1);
	}

	/**
	 * Draws game title.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawTitle(final Screen screen) {
		String titleString = "Invaders";
		String instructionsString =
				"select with w+s / arrows, confirm with space";

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() * 3 / 10);

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, titleString, screen.getHeight() / 4);
	}

	/**
	 * Draws main menu.
	 *
	 * @param screen Screen to draw on.
	 * @param option Option selected.
	 */
	public void drawMenu(final Screen screen, final int option, final int option2, final int option3) {
		String onePlayerModeString = "1 player mode";
		String twoPlayerModeString = "2 player mode";
		String mode = onePlayerModeString;
		String RecentRecord = "Recent Records";
		String playString = "Play";
		String highScoresString = "High scores";
		String exitString = "exit";
		String merchant = "Merchant";
		String bulletCountString = String.format("bullet count up"); // Starter
		String shipSpeedString = String.format("ship speed up"); // Starter
		String attackSpeedString = String.format("attack speed up"); // Starter
		String coinGainString = String.format("coin gain up"); // Starter
		String merchantState = merchant;

        AddSign addSign = new AddSign();


		// Play (Starter)
		if (option == 2 && option2 == 0)
			backBufferGraphics.setColor(Color.CYAN);
		else if (option == 2 && option2 == 1)
			backBufferGraphics.setColor(Color.MAGENTA);
		else
			backBufferGraphics.setColor(Color.WHITE);
		if (option2 == 1) {mode = twoPlayerModeString;} // 2 player mode (Starter), default: 1 player mode
		if (option == 2) {mode = "<- " + mode + " ->";}
		drawCenteredRegularString(screen, mode, screen.getHeight()
				/ 4 * 2); // adjusted Height

		// High scores (Starter)
		if (option == 3)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, highScoresString, screen.getHeight()
				/ 4 * 2 + fontRegularMetrics.getHeight() * 2); // adjusted Height

		if (option3 == 0) {merchantState = merchant;}
		try {
			if (option3 == 1) {
				merchantState = bulletCountString + MerchantTxt(Core.getUpgradeManager().getBulletCount(),1);
			}
			if (option3 == 2) {
				merchantState = shipSpeedString + MerchantTxt(Core.getUpgradeManager().getSpeedCount(),2);
			}
			if (option3 == 3) {
				merchantState = attackSpeedString + MerchantTxt(Core.getUpgradeManager().getAttackCount(),3);
			}
			if (option3 == 4) {
				merchantState = coinGainString + MerchantTxt(Core.getUpgradeManager().getCoinCount(),4);
			}
			if (option == 4) {
				merchantState = "<- " + merchantState + " ->";
			}
		} catch (IOException e){
			throw new RuntimeException(e);
		}

		if (option == 4 && option3 == 0)
			backBufferGraphics.setColor(Color.GREEN);
		else if (option == 4 && option3 != 0)
			backBufferGraphics.setColor(Color.CYAN);
		else
			backBufferGraphics.setColor(Color.WHITE);

		drawCenteredRegularString(screen, merchantState, screen.getHeight()
				/ 4 * 2 + fontRegularMetrics.getHeight() * 4);
		/*drawEntity(addSign, screen.getWidth()/2 + 50, screen.getHeight()
				/ 4 * 2 + fontRegularMetrics.getHeight() * 6 - 12);*/

        // Record scores (Team Clove)
        if (option == 5)
            backBufferGraphics.setColor(Color.GREEN);
        else
            backBufferGraphics.setColor(Color.WHITE);
        drawCenteredRegularString(screen, RecentRecord, screen.getHeight()
                / 4 * 2 + fontRegularMetrics.getHeight() * 6); // adjusted Height

        // Exit (Starter)
		if (option == 0)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, exitString, screen.getHeight()
				/ 4 * 2 + fontRegularMetrics.getHeight() * 10); // adjusted Height
	}

	/**
	 * Draws game results.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Score obtained.
	 * @param livesRemaining
	 *            Lives remaining when finished.
	 * @param shipsDestroyed
	 *            Total ships destroyed.
	 * @param accuracy
	 *            Total accuracy.
	 */

	// Ctrl S - add Coin String
	public void drawResults(final Screen screen, final int score,
							final int livesRemaining, final int shipsDestroyed,
							final float accuracy, final GameState gameState) {
		String scoreString = String.format("score: %04d", score);
		String livesRemainingString = "lives remaining: " + livesRemaining;
		String shipsDestroyedString = "enemies destroyed: " + shipsDestroyed;
		String accuracyString = String
				.format("accuracy: %.2f%%", accuracy * 100);
		String coinString = "Total earned  $ " + gameState.getCoin() + "  Coins!";

		int height = 4;

		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, scoreString, screen.getHeight()
				/ height);
		drawCenteredRegularString(screen, livesRemainingString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 2);
		drawCenteredRegularString(screen, shipsDestroyedString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 4);
		//Change the accuracy String when player does not shoot any bullet
		if (accuracy != accuracy) {
			accuracyString = "You didn't shoot any bullet.";
		}
		drawCenteredRegularString(screen, accuracyString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 6);
		drawCenteredRegularString(screen, coinString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 8);
	}

	/**
	 * Draws interactive characters for name input.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param name
	 *            Current name selected.
	 * @param nameCharSelected
	 *            Current character selected for modification.
	 */
	// Ctrl-S : move to lower position
	public void drawNameInput(final Screen screen, final char[] name,
							  final int nameCharSelected) {
		String newRecordString = "New Record!";
		String introduceNameString = "Introduce name:";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredRegularString(screen, newRecordString, screen.getHeight()
				/ 4 + fontRegularMetrics.getHeight() * 12);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, introduceNameString,
				screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 14);

		// 3 letters name.
		int positionX = screen.getWidth()
				/ 2
				- (fontRegularMetrics.getWidths()[name[0]]
				+ fontRegularMetrics.getWidths()[name[1]]
				+ fontRegularMetrics.getWidths()[name[2]]
				+ fontRegularMetrics.getWidths()[' ']) / 2;

		for (int i = 0; i < 3; i++) {
			if (i == nameCharSelected)
				backBufferGraphics.setColor(Color.GREEN);
			else
				backBufferGraphics.setColor(Color.WHITE);

			positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
			positionX = i == 0 ? positionX
					: positionX
					+ (fontRegularMetrics.getWidths()[name[i - 1]]
					+ fontRegularMetrics.getWidths()[' ']) / 2;

			backBufferGraphics.drawString(Character.toString(name[i]),
					positionX,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight()
							* 16);
		}
	}

	/**
	 * Draws basic content of game end screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param acceptsInput
	 *            If the screen accepts input.
	 */
	// CtrlS
	public void drawGameEnd(final Screen screen, final boolean acceptsInput, boolean isGameClear) {
		String gameEndString = isGameClear ? "Game Clear" : "Game Over";
		String continueOrExitString =
				"Press Space to play again, Escape to exit";
		String lostBonus = "You lost your Bonus on this level. Try Harder!";

		int height = 4;

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, gameEndString, screen.getHeight()
				/ height - fontBigMetrics.getHeight() * 2);
		if (!isGameClear) {
			backBufferGraphics.setColor(Color.GRAY);
			drawCenteredRegularString(screen, lostBonus, screen.getHeight()
					/ height - fontRegularMetrics.getHeight() - 20);
		}

		if (acceptsInput)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, continueOrExitString,
				screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * Draws high score screen title and instructions.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawHighScoreMenu(final Screen screen) {
		String highScoreString = "High Scores";
		String instructionsString = "Press Space to return";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, highScoreString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}

	/**
	 * Draws recent score(record) screen title and instructions.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * Team Clove
	 */
	public void drawRecordMenu(final Screen screen) {
		String recentScoreString = "Recent Records";
		String instructionsString = "Press Space to return";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, recentScoreString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}

	/**
	 * Draws high scores.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param highScores
	 *            List of high scores.
	 */
	public void drawHighScores(final Screen screen,
							   final List<Score> highScores) {
		backBufferGraphics.setColor(Color.WHITE);
		int i = 0;
		String scoreString = "";

		for (Score score : highScores) {
			scoreString = String.format("%s        %04d           %04d", score.getName(),
					score.getScore(), score.getPlayTime());
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
		}
	}

	/**
	 * Draws recent scores.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param recentScores
	 *            List of recent scores.
	 * Team Clove
	 */
	public void drawRecentScores(final Screen screen,
								 final List<Score> recentScores) {
		backBufferGraphics.setColor(Color.WHITE);
		int i = 0;
		boolean isFirstLine = true;
		int[] attributeXPosition = {50, 200, 295, 380, 480};
		int[] instanceXPostition = {25, 205, 300, 400, 515};

		if (isFirstLine) { // Create Header
			String[] Attribute = {"Date", "Score", "Level", "Destroy"};
			for(int k=0; k<4; k++){
				drawRightedRegularString(screen, Attribute[k], attributeXPosition[k],
						screen.getHeight() / 4 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			}
			isFirstLine = false;

			i++;
		}

		for (Score score : recentScores) {
			String[] Instance = new String[4];
			Instance[0] = String.format("%s",score.getDate());
			Instance[1] = String.format("%04d",score.getScore());
			Instance[2] = String.format("%04d",score.getHighestLevel());
			Instance[3] = String.format("%04d", score.getShipDestroyed());

			for(int k=0; k<4; k++){
				drawRightedRegularString(screen, Instance[k], instanceXPostition[k],
						screen.getHeight() / 4 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			}
			i++;
		}
	}


	/**
	 * Draws a righted string on regular font
	 *
	 * @param screen
	 * 				Screen to draw on.
	 * @param string
	 * 				String to draw.
	 * @param height
	 * 				Height of the drawing.
	 *
	 * 		//Clove
	 */
	public void drawRightedRegularString(final Screen screen,
										 final String string, final int width, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, width, height);
	}

	/**
	 * Draws a centered string on regular font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredRegularString(final Screen screen,
										  final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on big font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredBigString(final Screen screen, final String string,
									  final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontBigMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Countdown to game start.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param level
	 *            Game difficulty level.
	 * @param number
	 *            Countdown number.
	 * @param bonusLife
	 *            Checks if a bonus life is received.
	 */
	public void drawCountDown(final Screen screen, final int level,
							  final int number, final boolean bonusLife) {
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.GREEN);
		if (number >= 4)
			// Adjust the numbers here to match the appropriate boss levels.
			if (level == 3) { // Edited by team Enemy // ex) (level == 3 || level == 6 || level == 9)
				drawCenteredBigString(screen, "BOSS",
						screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
			} else if (!bonusLife) {
				drawCenteredBigString(screen, "Level " + level,
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			} else {
				drawCenteredBigString(screen, "Level " + level
								+ " - Bonus life!",
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			}
		else if (number != 0)
			drawCenteredBigString(screen, Integer.toString(number),
					screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
		else
			drawCenteredBigString(screen, "GO!", screen.getHeight() / 2
					+ fontBigMetrics.getHeight() / 3);
	}

	// Ctrl-S
	/**
	 * Show ReceiptScreen
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param roundState
	 *            State of one game round
	 */

	public void drawReceipt(final Screen screen, final RoundState roundState, final GameState gameState) {
		String stageScoreString = "Stage Score";
		String totalScoreString = "Total Score : ";
		String stageCoinString = "Coins Obtained";
		String instructionsString = "Press Space to Continue to get more coin!";
		String hitrateBonusString = "HitRate Bonus: $ " + roundState.getAccuracyBonus_amount() + "  Coins";
		String timeBonusString = "Time Bonus: $ " + roundState.getTimeBonus_amount() + "  Coins";
		String levelBonusString = "Level Bonus: $ " + roundState.getLevelBonus_amount() + "  Coins";
		//draw Score part
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, stageScoreString, screen.getHeight() / 8);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredBigString(screen, Integer.toString(roundState.getRoundScore()), screen.getHeight() / 8 + fontBigMetrics.getHeight() / 2 * 3);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, totalScoreString + gameState.getScore(), screen.getHeight() / 8 + fontRegularMetrics.getHeight() / 2 * 7);
		//draw Coin part
		backBufferGraphics.setColor(Color.LIGHT_GRAY);
		drawCenteredBigString(screen, stageCoinString, screen.getHeight() / 3 - 30);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredBigString(screen, Integer.toString(roundState.getBaseCoin_amount()), (screen.getHeight() / 3) - 30 + fontBigMetrics.getHeight() / 2 * 3);

		//draw HitRate Bonus part
		if (roundState.getAccuracyBonus_amount() != 0) {
			backBufferGraphics.setColor(Color.LIGHT_GRAY);
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.drawString(hitrateBonusString, screen.getWidth() / 2 - fontRegularMetrics.stringWidth(hitrateBonusString) / 2, (screen.getHeight() / 3) - 30 + fontRegularMetrics.getHeight() / 2 * 7);
		}
		//draw Time Bonus part
		if (roundState.getTimeBonus_amount() != 0) {
			backBufferGraphics.setColor(Color.LIGHT_GRAY);
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.drawString(timeBonusString, screen.getWidth() / 2 - fontRegularMetrics.stringWidth(timeBonusString) / 2, (screen.getHeight() / 3) - 30 + fontRegularMetrics.getHeight() / 2 * 9);
		}
		//draw level Bonus part
		if (roundState.getLevelBonus_amount() != 0) {
			backBufferGraphics.setColor(Color.LIGHT_GRAY);
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.drawString(levelBonusString, screen.getWidth() / 2 - fontRegularMetrics.stringWidth(levelBonusString) / 2, (screen.getHeight() / 3) - 30 + fontRegularMetrics.getHeight() / 2 * 11);

		}
		//draw Total coins part
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, "Total Round Coins", screen.getHeight() / 3 + 120);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredBigString(screen, Integer.toString(roundState.getRoundCoin()), screen.getHeight() / 3 + 120 + fontBigMetrics.getHeight() / 2 * 3);

		//draw instructionString part
		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * draw current coin.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param coin
	 *            Current Coin.
	 */
	public void drawCurrentCoin(final Screen screen , final int coin) {
		Coin coinImage = new Coin();
		int coinX = 10; //Starter edited
		int coinY = 7; //Adjust the y position value - Starter
		drawEntity(coinImage, coinX, coinY);
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.drawString(Integer.toString(coin), coinX + coinImage.getWidth() + 10, 20);
	}

	public void drawCurrentGem(final Screen screen , final int gem) {
		Gem gemImage = new Gem();
		int coinX = 10; //Starter edited
		int coinY = 24; //Adjust the y position value - Starter
		drawEntity(gemImage, coinX, coinY);
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.drawString(Integer.toString(gem), coinX + gemImage.getWidth() + 10, 35);
	}
	/**
	* ### TEAM INTERNATIONAL ###
	* Background draw and update method
	*/

	public void loadBackground(int levelNumber) {
		background = Background.getInstance();
		// I still have no clue how relative pathing or class pathing works
		InputStream imageStream = Background.getBackgroundImageStream(levelNumber);
		try {
			assert imageStream != null;
			backgroundImage = ImageIO.read(imageStream);
			background.backgroundReset(backgroundImage.getHeight(),backgroundImage.getWidth());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void drawBackground(boolean backgroundMoveRight, boolean backgroundMoveLeft) {
		int verticalOffset = background.getVerticalOffset();
		int horizontalOffset = background.getHorizontalOffset(backgroundMoveRight, backgroundMoveLeft);

		backBufferGraphics.drawImage(backgroundImage, horizontalOffset, verticalOffset, null);
	}

	/**
	* ### TEAM INTERNATIONAL ###
	*
	* Wave draw method
	* **/
	public void drawWave(final Screen screen, final int wave, final int number) {
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
		rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.GREEN);
		if (number >= 4)

		drawCenteredBigString(screen, "Wave " + wave,
		screen.getHeight() / 2
		+ fontBigMetrics.getHeight() / 3);

		else if (number != 0)
		drawCenteredBigString(screen, Integer.toString(number),
		screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
		else
		drawCenteredBigString(screen, "GO!", screen.getHeight() / 2
		+ fontBigMetrics.getHeight() / 3);
	}


	/**
	 * Draw the item that player got
	 *
	 * @param screen
	 *			  Screen to draw on.
	 *
	 * HUD Team - Jo Minseo
	 */
	public void drawItem(final Screen screen){
		//Bomb
		Entity itemBomb = new Entity(0, 0, 13 * 2, 8 * 2, Color.gray) {

		};
		itemBomb.setSpriteType(DrawManager.SpriteType.ItemBomb);

		if(Bomb.getIsBomb() && Bomb.getCanShoot()){
			drawEntity(itemBomb, screen.getWidth() / 5, screen.getHeight() - 50);
		}
	}

	public String MerchantTxt(int count, int number){
		if ((number == 1 && count > 3) ||
				(count != 0 && Core.getUpgradeManager().LevelCalculation(count) > 9)){
			return " max";
		}
		else {
			return " +" + Core.getUpgradeManager().LevelCalculation
					(count) + "   " + Core.getUpgradeManager().Price(number) + " "
					+ Core.getUpgradeManager().whatMoney(count,number);
		}
	}
}
