package engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;
import CtrlS.EncryptionSupport;
import engine.DrawManager.SpriteType;

import clove.Statistics; //Team Clove

/**
 * Manages files used in the application.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class FileManager {

	/** Singleton instance of the class. */
	private static FileManager instance;
	/** Application logger. */
	private static Logger logger;
	/** Max number of high scores. */
	private static final int MAX_SCORES = 7;
	/** Max number of recent recorded scores / Team Clove */
	private static final int MAX_RECORD = 10;

	/**
	 * private constructor.
	 */
	private FileManager() {
		logger = Core.getLogger();
	}

	/**
	 * Returns shared instance of FileManager.
	 * 
	 * @return Shared instance of FileManager.
	 */
	protected static FileManager getInstance() {
		if (instance == null)
			instance = new FileManager();
		return instance;
	}

	/**
	 * Loads sprites from disk.
	 * 
	 * @param spriteMap
	 *            Mapping of sprite type and empty boolean matrix that will
	 *            contain the image.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void loadSprite(final Map<SpriteType, boolean[][]> spriteMap)
			throws IOException {
		InputStream inputStream = null;

		try {
			inputStream = DrawManager.class.getClassLoader()
					.getResourceAsStream("graphics");
			char c;

			// Sprite loading.
			for (Map.Entry<SpriteType, boolean[][]> sprite : spriteMap
					.entrySet()) {
				for (int i = 0; i < sprite.getValue().length; i++)
					for (int j = 0; j < sprite.getValue()[i].length; j++) {
						do
							c = (char) inputStream.read();
						while (c != '0' && c != '1');

						if (c == '1')
							sprite.getValue()[i][j] = true;
						else
							sprite.getValue()[i][j] = false;
					}
				logger.fine("Sprite " + sprite.getKey() + " loaded.");
			}
			if (inputStream != null)
				inputStream.close();
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	/**
	 * Loads a font of a given size.
	 * 
	 * @param size
	 *            Point size of the font.
	 * @return New font.
	 * @throws IOException
	 *             In case of loading problems.
	 * @throws FontFormatException
	 *             In case of incorrect font format.
	 */
	public Font loadFont(final float size) throws IOException,
			FontFormatException {
		InputStream inputStream = null;
		Font font;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("font.ttf");
			if (inputStream != null) {
				font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(size);
			} else {
				// Set as default font, if inputStream is null
				System.out.println("Custom font not found, applying default font.");
				font = new Font("Serif", Font.PLAIN, (int) size); // Set as "Serif"
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return font;
	}

	/**
	 * Returns the application default scores if there is no user high scores
	 * file.
	 * 
	 * @return Default high scores.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	private List<Score> loadDefaultHighScores() throws IOException {
		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("scores");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			Score highScore = null;
			String name = reader.readLine();
			String score = reader.readLine();
			String time = reader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score), Integer.parseInt(time));
				highScores.add(highScore);
				name = reader.readLine();
				score = reader.readLine();
				time = reader.readLine();
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return highScores;
	}

	/**
	 * Loads high scores from file, and returns a sorted list of pairs score -
	 * value.
	 * 
	 * @return Sorted list of scores - players.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public List<Score> loadHighScores() throws IOException {

		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);
			inputStream = new FileInputStream(scoresFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			logger.info("Loading user high scores.");

			Score highScore = null;
			String name = bufferedReader.readLine();
			String score = bufferedReader.readLine();
			String time = bufferedReader.readLine();

			while ((name != null) && (score != null) && (time != null)) {
				highScore = new Score(name, Integer.parseInt(score), Long.parseLong(time));
				highScores.add(highScore);
				name = bufferedReader.readLine();
				score = bufferedReader.readLine();
				time = bufferedReader.readLine();
			}

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("Loading default high scores.");
			highScores = loadDefaultHighScores();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}

		Collections.sort(highScores);
		return highScores;
	}

	/**
	 * Saves user high scores to disk.
	 * 
	 * @param highScores
	 *            High scores to save.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void saveHighScores(final List<Score> highScores)
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);

			if (!scoresFile.exists())
				scoresFile.createNewFile();

			outputStream = new FileOutputStream(scoresFile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user high scores.");

			// Saves 7 or less scores.
			int savedCount = 0;
			for (Score score : highScores) {
				if (savedCount >= MAX_SCORES)
					break;
				bufferedWriter.write(score.getName());
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getScore()));
				bufferedWriter.newLine();
				bufferedWriter.write(Long.toString(score.getPlayTime()));
				bufferedWriter.newLine();
				savedCount++;
			}

		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}

	/**
	 * Returns the application default scores if there is no user recent scores
	 * file.
	 *
	 * @return Default recent scores.
	 * @throws IOException
	 *             In case of loading problems.
	 * Method for implement Recent score
	 * Team Clover
	 */
	private List<Score> loadDefaultRecentScores() throws IOException {
		List<Score> recentScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("recent");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			Score recentScore;
			String date = reader.readLine();
			String score = reader.readLine();
			String highestLevel = reader.readLine();
			String totalShipDestroyed = reader.readLine();
			String clearAchievementNumber = reader.readLine();
			String name = null;

			while ((date != null) && (score != null)) {
				recentScore = new Score(name, Integer.parseInt(score), date, Integer.parseInt(highestLevel),
						Integer.parseInt(totalShipDestroyed), Integer.parseInt(clearAchievementNumber));
				recentScores.add(recentScore);
				date = reader.readLine();
				score = reader.readLine();
				highestLevel = reader.readLine();
				totalShipDestroyed = reader.readLine();
				clearAchievementNumber = reader.readLine();
			}

		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return recentScores;
	}
	/**
	 * Loads recent scores from file, and returns a sorted list of pairs score -
	 * value.
	 *
	 * @return Sorted list of Recent scores - players.
	 * @throws IOException
	 *             In case of loading problems.
	 *
	 * Method for implement Recent score
	 * Team Clove
	 */
	public List<Score> loadRecentScores() throws IOException {

		List<Score> recentScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "recent";

			System.out.print(scoresPath);

			File scoresFile = new File(scoresPath);
			inputStream = new FileInputStream(scoresFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			logger.info("Loading user recent scores.");

			Score recentScore;
			String date = bufferedReader.readLine();
			String score = bufferedReader.readLine();
			String highestLevel = bufferedReader.readLine();
			String totalShipDestroyed = bufferedReader.readLine();
			String clearAchievementNumber = bufferedReader.readLine();
			String name = null;

			while ((date != null) && (score != null)) {
				recentScore = new Score(name, Integer.parseInt(score), date, Integer.parseInt(highestLevel),
						Integer.parseInt(totalShipDestroyed), Integer.parseInt(clearAchievementNumber));
				recentScores.add(recentScore);
				date = bufferedReader.readLine();
				score = bufferedReader.readLine();
				highestLevel = bufferedReader.readLine();
				totalShipDestroyed = bufferedReader.readLine();
				clearAchievementNumber = bufferedReader.readLine();

			}

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("Loading default high scores.");
			recentScores = loadDefaultRecentScores();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}

		//Collections.sort(recentScores);
		return recentScores;
	}

	/**
	 * Saves user recent scores to disk.
	 *
	 * @param recentScores
	 *            High scores to save.
	 * @throws IOException
	 *             In case of loading problems.
	 * Method for implement Recent score
	 * Team Clove
	 */

	public void saveRecentScores(final List<Score> recentScores)
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "recent";

			File scoresFile = new File(scoresPath);

			if (!scoresFile.exists())
				scoresFile.createNewFile();

			outputStream = new FileOutputStream(scoresFile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			Statistics stat = new Statistics();
			stat = stat.loadUserData(stat);

			logger.info("Saving user recent scores.");

			// Saves 10 or less scores.
			int savedCount = 0;
			for (Score score : recentScores) {
				if (savedCount >= MAX_RECORD)
					break;
				bufferedWriter.write(score.getDate());
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getScore()));
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getHighestLevel()));
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getShipDestroyed()));
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getClearAchievementNumber()));
				bufferedWriter.newLine();
				savedCount++;
			}
			stat.resetStatistics();

		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}

	/**
	 *  save userData(Statistics) to disk
	 *
	 * @param playerStatistics
	 * 				Player's Statistics list to save.
	 * @throws IOException
	 * 				In case of saving problems.
	 *
	 */
	//Team Clove
    public void saveUserData(final List<Statistics> playerStatistics) throws IOException {
		Properties properties = new Properties();
		OutputStream outputStream = null;

        try {
            String jarPath = FileManager.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            jarPath = URLDecoder.decode(jarPath, "UTF-8");

            String staticsPath = new File(jarPath).getParent();
            staticsPath += File.separator;
			staticsPath += "Statistic.properties";

            File staticsFile = new File(staticsPath);

			logger.info("Saving Player Statistic.");

            if (!staticsFile.exists())
				staticsFile.createNewFile();

			if(!playerStatistics.isEmpty()){
				Statistics stat = playerStatistics.get(0);
				properties.setProperty("highestLevel", String.valueOf(stat.getHighestLevel()));
				properties.setProperty("totalBulletsShot", String.valueOf(stat.getTotalBulletsShot()));
				properties.setProperty("totalShipsDestroyed", String.valueOf(stat.getTotalShipsDestroyed()));
				properties.setProperty("shipsDestructionStreak", String.valueOf(stat.getShipsDestructionStreak()));
				properties.setProperty("playedGameNumber", String.valueOf(stat.getPlayedGameNumber()));
				properties.setProperty("clearAchievementNumber", String.valueOf(stat.getClearAchievementNumber()));
				properties.setProperty("totalPlaytime", String.valueOf(stat.getTotalPlaytime()));
			}
			outputStream = new FileOutputStream(staticsFile);
			properties.store(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")),
					"PlayerGameStatistics");


        } finally {
            if (outputStream != null)
                outputStream.close();
        }
    }

	/**
	 *
	 * load userData(Statistics) from file, and return userData(Statistics)
	 *
	 * @return Player's Statistics
	 * @throws IOException
	 * 				In case of loading problems.
	 */
	//Team Clove
	public Statistics loadUserData() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = null;

		Statistics stat;

		try{
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String staticsPath = new File(jarPath).getParent();
			staticsPath += File.separator;
			staticsPath += "Statistic.properties";

			File staticsFile = new File(staticsPath);

			inputStream = new FileInputStream(staticsFile);
			properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

			logger.info("Loading Player Statistic.");

			int highestLevel = Integer.parseInt(properties.getProperty("highestLevel"));
			int totalBulletsShot = Integer.parseInt(properties.getProperty("totalBulletsShot"));
			int totalShipsDestroyed = Integer.parseInt(properties.getProperty("totalShipsDestroyed"));
			int shipsDestructionStreak = Integer.parseInt(properties.getProperty("shipsDestructionStreak"));
			int playedGameNumber = Integer.parseInt(properties.getProperty("playedGameNumber"));
			int clearAchievementNumber = Integer.parseInt(properties.getProperty("clearAchievementNumber"));
			long totalPlaytime = Integer.parseInt(properties.getProperty("totalPlaytime"));

			stat = new Statistics(highestLevel, totalBulletsShot, totalShipsDestroyed, shipsDestructionStreak,
					playedGameNumber, clearAchievementNumber, totalPlaytime);

		} catch (FileNotFoundException e){
			logger.info("Loading default user statistics.");
			stat = loadDefaultUserData();
		} finally {
			if(inputStream != null){
				inputStream.close();
			}

		}

		return stat;
	}

	/**
	 * Returns the application default userData(Statistics)
	 * if there is no Statistic.properties file.
	 *
	 *
	 * @return Default Player's Statistics
	 * @throws IOException
	 * 				In case of loading problems.
	 */
	//Team Clove
	public Statistics loadDefaultUserData() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = null;

		Statistics stat;

		try{
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("Statistic.properties");

			properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

			int highestLevel = Integer.parseInt(properties.getProperty("highestLevel"));
			int totalBulletsShot = Integer.parseInt(properties.getProperty("totalBulletsShot"));
			int totalShipsDestroyed = Integer.parseInt(properties.getProperty("totalShipsDestroyed"));
			int shipsDestructionStreak = Integer.parseInt(properties.getProperty("shipsDestructionStreak"));
			int playedGameNumber = Integer.parseInt(properties.getProperty("playedGameNumber"));
			int clearAchievementNumber = Integer.parseInt(properties.getProperty("clearAchievementNumber"));
			long totalPlaytime = Integer.parseInt(properties.getProperty("totalPlaytime"));

			stat = new Statistics(highestLevel, totalBulletsShot, totalShipsDestroyed, shipsDestructionStreak,
					playedGameNumber, clearAchievementNumber, totalPlaytime);

		} finally {
			if(inputStream != null){
				inputStream.close();
			}
		}
		return stat;
	}

	/**
	 * Saves user coin to disk.
	 *
	 * @param coin
	 *            amount of user coin to save.
	 * @throws IOException
	 *             In case of saving problems.
	 */

	public void saveCoin(final int coin) throws IOException {
		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		//Choose File root
		String currencyPath = new File(jarPath).getParent();
		currencyPath += File.separator;
		currencyPath += "currency";

		File currencyFile = new File(currencyPath);

		//create File If there is no currencyFile
		if (!currencyFile.exists())
			currencyFile.createNewFile();

		List<String> lines = new ArrayList<>();
		String line;

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(currencyFile), Charset.forName("UTF-8")))) {
			// Read the file's current content
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		}

		// Modify the first line (coin)
		if (!lines.isEmpty()) {
			lines.set(0, EncryptionSupport.encrypt(Integer.toString(coin)));
		} else {
			// If the file was empty, add the new coin as the first line and the new gem as the second line
			lines.add(EncryptionSupport.encrypt(Integer.toString(coin)));
			lines.add(EncryptionSupport.encrypt("0"));
		}

		try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(currencyFile), Charset.forName("UTF-8")))) {
			// Write back the modified content
			for (String l : lines) {
				bufferedWriter.write(l);
				bufferedWriter.newLine();
			}
			logger.info("Saving user's coin.");
		}
	}

	/**
	 * Loads user coin from file, and returns current coin.
	 *
	 * @return amount of current coin.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public int loadCoin() throws IOException {
		int coin;
		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		String currencyPath = new File(jarPath).getParent();
		currencyPath += File.separator;
		currencyPath += "currency";

		File currencyFile = new File(currencyPath);

		// create File If there is no currencyFile
		if (!currencyFile.exists())
			currencyFile.createNewFile();

		// If the file was empty, add the new coin as the first line and the new gem as the second line
		if (currencyFile.length() == 0) {
			try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(currencyFile), Charset.forName("UTF-8")))) {
				bufferedWriter.write(EncryptionSupport.encrypt("0"));
				bufferedWriter.newLine();
				bufferedWriter.write(EncryptionSupport.encrypt("0"));
				bufferedWriter.newLine();
			}
		}

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(currencyFile), Charset.forName("UTF-8")))) {
			logger.info("Loading user's coin.");
			String amount = bufferedReader.readLine();
			coin = Integer.parseInt(EncryptionSupport.decrypt(amount));
		}

		return coin;
	}

	/**
	 * Saves user gem to disk.
	 *
	 * @param gem
	 *            amount of user gem to save.
	 * @throws IOException
	 * 			   In case of saving problems.
	 */
	// Team-Ctrl-S(Currency)
	public void saveGem(final int gem) throws IOException {
		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		//Choose File root
		String currencyPath = new File(jarPath).getParent();
		currencyPath += File.separator;
		currencyPath += "currency";

		File currencyFile = new File(currencyPath);

		//create File If there is no currencyFile
		if (!currencyFile.exists())
			currencyFile.createNewFile();

		List<String> lines = new ArrayList<>();
		String line;

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(currencyFile), Charset.forName("UTF-8")))) {
			// Read the file's current content
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		}

		// Modify the second line (gem)
		if (!lines.isEmpty()) {
			lines.set(1, EncryptionSupport.encrypt(Integer.toString(gem)));
		} else {
			// If the file was empty, add the new coin as the first line and the new gem as the second line
			lines.add(EncryptionSupport.encrypt("0"));
			lines.add(EncryptionSupport.encrypt(Integer.toString(gem)));
		}

		try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(currencyFile), Charset.forName("UTF-8")))) {
			// Write back the modified content
			for (String l : lines) {
				bufferedWriter.write(l);
				bufferedWriter.newLine();
			}
			logger.info("Saving user's gem.");
		}
	}

	/**
	 * Loads user gem from file, and returns current gem.
	 *
	 * @return amount of current gem.
	 * @throws IOException
	 * 			   In case of loading problems.
	 */
	// Team-Ctrl-S(Currency)
	public int loadGem() throws IOException {
		int gem;
		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		String currencyPath = new File(jarPath).getParent();
		currencyPath += File.separator;
		currencyPath += "currency";

		File currencyFile = new File(currencyPath);

		//create File If there is no currencyFile
		if (!currencyFile.exists())
			currencyFile.createNewFile();

		// If the file was empty, add the new coin as the first line and the new gem as the second line
		if (currencyFile.length() == 0) {
			try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(currencyFile), Charset.forName("UTF-8")))) {
				bufferedWriter.write(EncryptionSupport.encrypt("0"));
				bufferedWriter.newLine();
				bufferedWriter.write(EncryptionSupport.encrypt("0"));
				bufferedWriter.newLine();
			}
		}

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(currencyFile), Charset.forName("UTF-8")))) {
			logger.info("Loading user's gem.");

			bufferedReader.readLine(); // Ignore first(coin) line
			String amount = bufferedReader.readLine();
			gem = Integer.parseInt(EncryptionSupport.decrypt(amount));
		}

		return gem;
	}

	/**
	 * Loads upgrade statuses from upgrade_status.properties file.
	 *
	 * @return Properties object containing the upgrade statuses.
	 * @throws IOException In case of loading problems.
	 */
	public Properties loadUpgradeStatus() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String propertiesPath = new File(jarPath).getParent();
			propertiesPath += File.separator + "upgrade_status.properties";

			File upgradeFile = new File(propertiesPath);

			if (upgradeFile.exists()) {
				inputStream = new FileInputStream(upgradeFile);
				properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			} else {
				logger.info("upgrade_status.properties not found. Loading default upgrade statuses.");
				properties = loadDefaultUpgradeStatus();
			}

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return properties;
	}

	/**
	 * Saves upgrade statuses to upgrade_status.properties file.
	 *
	 * @param properties The Properties object containing the upgrade statuses to save.
	 * @throws IOException In case of saving problems.
	 */
	public void saveUpgradeStatus(Properties properties) throws IOException {
		OutputStream outputStream = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String propertiesPath = new File(jarPath).getParent();
			propertiesPath += File.separator + "upgrade_status.properties";

			File upgradeFile = new File(propertiesPath);

			if (!upgradeFile.exists()) {
				upgradeFile.createNewFile();
			}

			outputStream = new FileOutputStream(upgradeFile);
			properties.store(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")), "Upgrade Statuses");

			logger.info("Saving upgrade statuses.");

		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	/**
	 * Loads default upgrade statuses from upgrade_default.properties file.
	 *
	 * @return Properties object containing the default upgrade statuses.
	 * @throws IOException In case of loading problems.
	 */
    public Properties loadDefaultUpgradeStatus() throws IOException {
		Properties defaultProperties = new Properties();
		InputStream inputStream = null;

		try {
			inputStream = FileManager.class.getClassLoader().getResourceAsStream("upgrade_default.properties");
			if (inputStream != null) {
				defaultProperties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
				logger.info("Default upgrade statuses loaded from upgrade_default.properties.");
			} else {
				logger.warning("upgrade_default.properties not found. Using hardcoded default values.");
				defaultProperties.setProperty("coin_acquisition_multiplier", "1.0");
				defaultProperties.setProperty("attack_speed", "750");
				defaultProperties.setProperty("movement_speed", "2");
				defaultProperties.setProperty("bullet_num", "1");

				//inventory team
				defaultProperties.setProperty("speed_LevelCount", "1");
				defaultProperties.setProperty("Coin_LevelCount", "1");
				defaultProperties.setProperty("attack_LevelCount", "1");
				defaultProperties.setProperty("bullet_LevelCount", "0");
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return defaultProperties;
	}
}