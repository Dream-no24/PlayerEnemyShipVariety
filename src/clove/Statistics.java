package clove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


import ctrlS.CurrencyManager;
import engine.Core;
import engine.FileManager;

public class Statistics {

    private AchievementConditions achievementConditions;
    private ScheduledExecutorService scheduler;
    /** Number of Player's Highest Reached Level */
    private int highestLevel;
    /** Number of Totally Fired Bullet */
    private int totalBulletsShot;
    /** Number of Totally Destroyed Ships*/
    private int totalShipsDestroyed;
    /** Number of ships destroyed consecutively */
    private int shipsDestructionStreak;
    /** Number of games played */
    private int playedGameNumber;
    /** Number of achievements cleared */
    private int clearAchievementNumber;
    /** Total playtime */
    private long totalPlaytime;
    /** Additional playtime */
    private long playTime;

    private static FileManager fileManager;
    private static Logger logger;

    /** Using for save statistics */
    private List<Statistics> playerStatistics;
    private Statistics stat;

    /**
     *
     * Constructor for save Variables
     *
     * @param shipsDestructionStreak
     *              Number of ships destroyed consecutively
     * @param playedGameNumber
     *              Number of games played
     * @param clearAchievementNumber
     *              Number of achievements cleared
     * @param TotalPlaytime
     *              Total playtime
     */

    public Statistics(int highestLevel, int totalBulletsShot, int totalShipsDestroyed, int shipsDestructionStreak,
                      int playedGameNumber, int clearAchievementNumber, long TotalPlaytime) {
        this.highestLevel = highestLevel;
        this.totalBulletsShot = totalBulletsShot;
        this.totalShipsDestroyed = totalShipsDestroyed;
        this.shipsDestructionStreak = shipsDestructionStreak;
        this.playedGameNumber = playedGameNumber;
        this.clearAchievementNumber = clearAchievementNumber;
        this.totalPlaytime = TotalPlaytime;

        //this.achievementConditions = new AchievementConditions();
        //this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Public Constructor
     */

    public Statistics() {
        fileManager = Core.getFileManager();
        logger = Core.getLogger();
        this.playerStatistics = new ArrayList<Statistics>();
    }

    /**
     * Compare the previously highest reached level with the currently reached level.
     * @param Level
     *              current reached level
     * @throws IOException
     *              In case of saving problems.
     */

    public void comHighestLevel(int Level) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentHighestLevel = stat.getHighestLevel();
        if(CurrentHighestLevel < Level){
            playerStatistics.clear();
            playerStatistics.add(new Statistics(Level, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                    stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime));
            fileManager.saveUserData(playerStatistics);
        }
    }

    /**
     * Add the number of bullets fired so far to the previous record.
     * @param PlusBulletShot
     *              current fired bullets.
     * @throws IOException
     *              In case of saving problems.
     */

    public void addBulletShot(int PlusBulletShot) throws IOException{
        this.stat = loadUserData(stat);
        int CurrentBulletShot = stat.getTotalBulletsShot();
        CurrentBulletShot += PlusBulletShot;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, CurrentBulletShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime));
        fileManager.saveUserData(playerStatistics);
    }

    /**
     * Add the number of ships destroyed so far to the previous record.
     * @param PlusShipsDestroyed
     *              The number of ships destroyed
     * @throws IOException
     *              In case of saving problems.
     */



    public void addShipsDestroyed(int PlusShipsDestroyed) throws IOException{
        this.stat = loadUserData(stat);
        int CurrentShipsDestroyed = stat.getTotalShipsDestroyed();
        CurrentShipsDestroyed += PlusShipsDestroyed;

        if (achievementConditions != null) {
            achievementConditions.onKill();
        }

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, CurrentShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime));
        fileManager.saveUserData(playerStatistics);
    }

    /**
     * Add the number of games played.
     *
     * @param PlusPlayedGameNumber
     *              The number of times the game has been played
     *              until the program is executed and closed.
     * @throws IOException
     *              In case of saving problems.
     */

    public void addPlayedGameNumber(int PlusPlayedGameNumber) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentPlayedGameNumber = stat.getPlayedGameNumber();
        CurrentPlayedGameNumber += PlusPlayedGameNumber;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                CurrentPlayedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime));
        fileManager.saveUserData(playerStatistics);
    }

    /**
     * Compare the current game's destruction streak
     * with the high score for shipsDestructionStreak.
     *
     * @param DestroyedShipNumber
     *              current game score
     * @throws IOException
     *              In case of saving problems.
     */

    public void comShipsDestructionStreak(int DestroyedShipNumber) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentShipsDestructionStreak = stat.getShipsDestructionStreak();
        if(CurrentShipsDestructionStreak < DestroyedShipNumber){
            playerStatistics.clear();
            playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, DestroyedShipNumber,
                    stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime));
            fileManager.saveUserData(playerStatistics);
        }
    }

    /**
     * Compare the number of achievements cleared up to now with
     * the number of achievements cleared after the current game.
     *
     * @param ClearedAchievement
     *              current game score
     * @throws IOException
     *              In case of saving problems.
     */

    public void comClearAchievementNumber(int ClearedAchievement) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentClearAchievementNumber = stat.getClearAchievementNumber();
        if(CurrentClearAchievementNumber < ClearedAchievement){
            playerStatistics.clear();
            playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed,stat.shipsDestructionStreak,
                    stat.playedGameNumber, ClearedAchievement, stat.totalPlaytime));
            fileManager.saveUserData(playerStatistics);
        }
    }

    /**
     *  Add the current game's playtime to the previous total playtime.
     *
     * @param Playtime
     *              current playtime
     * @throws IOException
     *              In case of saving problems.
     */

    public void addTotalPlayTime(long Playtime) throws IOException {
        this.stat = loadUserData(stat);
        long CurrentPlaytime = stat.getTotalPlaytime();
        CurrentPlaytime += Playtime;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, CurrentPlaytime));
        fileManager.saveUserData(playerStatistics);
    }

    /**
     *  Load Statistic.property (userdata)
     *
     * @throws IOException
     *              In case of loading problems.
     */
    public Statistics loadUserData(Statistics stat) throws IOException {
        stat = fileManager.loadUserData();
        return stat;
    }

    public Statistics getStatisticsData() throws IOException {
        Statistics StatisticsData = fileManager.loadUserData();
        return StatisticsData;
    }

    public void resetStatistics() throws IOException {
        this.playerStatistics = new ArrayList<Statistics>();
        playerStatistics.add(new Statistics(0, 0, 0, 0,
                0, 0, 0));
        fileManager.saveUserData(playerStatistics);
    }

    public void startAddingShipsDestroyed() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                addShipsDestroyed(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public int getHighestLevel() { return highestLevel; }

    public int getTotalBulletsShot() { return totalBulletsShot; }

    public int getTotalShipsDestroyed() { return totalShipsDestroyed; }

    public int getShipsDestructionStreak() { return shipsDestructionStreak; }

    public int getPlayedGameNumber() { return playedGameNumber; }

    public int getClearAchievementNumber() { return clearAchievementNumber; }

    public long getTotalPlaytime() { return totalPlaytime; }

}