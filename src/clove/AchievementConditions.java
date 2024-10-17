package clove;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import engine.Core;
import engine.DrawManager;
import engine.GameState;
import HUDTeam.DrawAchievementHud;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;


public class AchievementConditions {

    private DrawManager drawManager;

    private GameState gameState;
    private static Logger logger;

    private static int killCount;
    private PropertyChangeSupport support;

    private int highestLevel;
    private int totalBulletsShot;
    private int totalShipsDestroyed;
    private int shipsDestructionStreak;
    private int playedGameNumber;
    private int clearAchievementNumber;
    private long TotalPlaytime;
    private Statistics stats;

    private List<Achievement> killAchievements = new ArrayList<>();
    private List<Achievement> trialAchievements = new ArrayList<>();
    private List<Achievement> streakAchievements = new ArrayList<>();
    private List<Achievement> accuracyAchievements = new ArrayList<>();
    private List<Achievement> noDeathAchievements = new ArrayList<>();
    private List<Achievement> stageAchievements = new ArrayList<>();
    private List<Achievement> scoreAchievements = new ArrayList<>();
    private List<Achievement> allAchievements = new ArrayList<>();
    private List<String> unlockedAchievements = new ArrayList<>();

    public AchievementConditions() {
        initializeAchievements();

        try{
            this.stats = new Statistics();
            setStatistics();
        } catch (IOException e){
            logger.warning("Couldn't load Statistics!");
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private void setStatistics() throws IOException {
        this.stats = stats.loadUserData(stats);
    }

    private int enemiesKilledIn3Seconds = 0;

    public AchievementConditions(DrawManager drawManager) {
        this.drawManager = drawManager;
        initializeAchievements();
    }

    public void initializeAchievements() {

        noDeathAchievements.add(new Achievement("Aerobatics","Maintain Maximum Life",3, Achievement.AchievementType.LIVES));

        killAchievements.add(new Achievement("Rookie Pilot", "Destroy 25 enemies", 25, Achievement.AchievementType.KILLS));
        killAchievements.add(new Achievement("Space Hunter", "Destroy 100 enemies", 100, Achievement.AchievementType.KILLS));
        killAchievements.add(new Achievement("Space Trooper", "Destroy 250 enemies", 250, Achievement.AchievementType.KILLS));
        killAchievements.add(new Achievement("Guardian of Universe", "Destroy 500 enemies", 500, Achievement.AchievementType.KILLS));

        trialAchievements.add(new Achievement("Welcome Recruit", "Finished first game", 1, Achievement.AchievementType.TRIALS));
        trialAchievements.add(new Achievement("Skilled Solider", "Finished 20th game", 20, Achievement.AchievementType.TRIALS));
        trialAchievements.add(new Achievement("Veteran Pilot", "Finished 50th game", 50, Achievement.AchievementType.TRIALS));

        accuracyAchievements.add(new Achievement("Gunsliger", "Accuracy of 60%", 60, Achievement.AchievementType.ACCURACY));
        accuracyAchievements.add(new Achievement("Fear the Enemy", "Accuracy of 75%", 75, Achievement.AchievementType.ACCURACY));
        accuracyAchievements.add(new Achievement("Genocide", "Accuracy of 85%", 85, Achievement.AchievementType.ACCURACY));

        streakAchievements.add(new Achievement("Preheating", "Kill streak of 3", 3, Achievement.AchievementType.KILLSTREAKS));
        streakAchievements.add(new Achievement("Overheating", "Kill streak of 5", 5, Achievement.AchievementType.KILLSTREAKS));
        streakAchievements.add(new Achievement("Runaway","Kill streak of 7", 7, Achievement.AchievementType.KILLSTREAKS));

        scoreAchievements.add(new Achievement("First Milestone", "Reach 6,000 points", 6000, Achievement.AchievementType.SCORE));
        scoreAchievements.add(new Achievement("Score Hunter", "Reach 15,000 points", 15000, Achievement.AchievementType.SCORE));
        scoreAchievements.add(new Achievement("Score Master", "Reach 30,000 points", 30000, Achievement.AchievementType.SCORE));

        stageAchievements.add(new Achievement("Home Sweet Home","Cleared Final Stage", Core.NUM_LEVELS-1, Achievement.AchievementType.STAGE));

        allAchievements.addAll(killAchievements);
        allAchievements.addAll(trialAchievements);
        allAchievements.addAll(streakAchievements);
        allAchievements.addAll(noDeathAchievements);
        allAchievements.addAll(stageAchievements);
        allAchievements.addAll(scoreAchievements);

        allAchievements.add(new Achievement("Medal of Honor", "Complete all achievements", 0, Achievement.AchievementType.STAGE));
    }

    // Have to check if the code right below works
    public void checkAllAchievements() {
        boolean allCompleted = true;

        System.out.println("Checking all achievements...");
        for (Achievement achievement: allAchievements) {
            if (!achievement.isCompleted()) {
                allCompleted = false;
                break;
            }
        }
        if (allCompleted) {
            completeAchievement(allAchievements.get(allAchievements.size() - 1));
        }
    }

    public void onStage() throws  IOException {
        int highestStage = stats.getHighestLevel();
        System.out.println("Checking stage achievements. Highest stage: " + highestStage);
        for (Achievement achievement : stageAchievements) {
            if (highestStage >= achievement.getRequiredStages() && !achievement.isCompleted()) {
                completeAchievement(achievement);
            }
        }
    }

    public void onKill() throws IOException {
        enemiesKilledIn3Seconds++;
        lastKillTime = System.currentTimeMillis();

        int currentKills = stats.getTotalShipsDestroyed();
        System.out.println("Checking kill achievements. Current kills: " + currentKills);
        System.out.println("Kill Achievements size: " + killAchievements.size());
        for (Achievement achievement : killAchievements) {
            System.out.println("Checking " + achievement.getAchievementName());
            if (currentKills >= achievement.getRequiredKills() && !achievement.isCompleted()) {
                completeAchievement(achievement);
            }
        }
    }

    private ScheduledExecutorService scheduler;

    private static final long TIME_LIMIT = 3000;
    private long lastKillTime = 0;

    private void resetKillCountIfNeeded() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastKillTime > TIME_LIMIT) {
            enemiesKilledIn3Seconds = 0;
        }
    }

    public void checkNoDeathAchievements(int lives) {
        System.out.println("Checking No Death achievements. Current lives: " + lives);
        if (lives == Core.MAX_LIVES) {
            for (Achievement achievement : noDeathAchievements) {
                if (highestLevel==Core.NUM_LEVELS-1 && !achievement.isCompleted()) {
                    completeAchievement(achievement);
                }
            }
        }
    }

    public void score(int score) {
        System.out.println("Checking score achievements. Current score: " + score);
            for (Achievement achievement : scoreAchievements) {
                if (score >= achievement.getRequiredScore() && !achievement.isCompleted()) {
                    completeAchievement(achievement);
                }
            }
    }

    public void accuracy(int bulletsShot, int hitCount) throws IOException {
        float accuracy = ((float) hitCount/ (float) bulletsShot) * 100;
        System.out.println("Checking accuracy achievements. Checking accuracy: " + accuracy);
        for (Achievement achievement : accuracyAchievements) {
            System.out.println("Checking " + achievement.getAchievementName());
            if (accuracy >= achievement.getRequiredAccuracy() && !achievement.isCompleted()) {
                completeAchievement(achievement);
            }
        }
    }

    public void killStreakSystem() {
        this.killCount = 0;
    }

    public static void incrementKillCount() {
        killCount++;
        System.out.println("Kill count incremented: " + killCount);
    }

    public static void resetKillCount() {
        killCount = 0;
        System.out.println("Kill count reset.");
    }


    public int getKillCount() {
        return killCount;
    }

    public void killStreak(int count) throws IOException {
        for (Achievement achievement : streakAchievements) {
            System.out.println("Checking " + achievement.getAchievementName());
            if (killCount >= achievement.getRequiredKillStreaks() && !achievement.isCompleted()) {
                completeAchievement(achievement);
            }
        }
    }

    public void trials() {
        int playedTrials = stats.getPlayedGameNumber();
        for (Achievement achievement : trialAchievements) {
            System.out.println("Checking trial achievements. Current trials: " + playedTrials);
            if (playedTrials >= achievement.getRequiredTrials() && !achievement.isCompleted()) {
                completeAchievement(achievement);
            }
        }
    }

    private void completeAchievement(Achievement achievement) {
        if (!unlockedAchievements.contains(achievement.getAchievementName())) {
            System.out.println("Achievement Unlocked: " + achievement.getAchievementName() + " - " + achievement.getAchievementDescription());
            unlockedAchievements.add(achievement.getAchievementName());
            achievement.completeAchievement();
            getUnlockedAchievements();
            System.out.println("Unlocked achievements list: " + unlockedAchievements);
            DrawAchievementHud.achieve(achievement.getAchievementName());
        }
        else if (unlockedAchievements.contains(achievement.getAchievementName())) {
            System.out.println(achievement.getAchievementName() + " has already been unlocked.");
        }
    }

    public List<String> getUnlockedAchievements() {
        return new ArrayList<>(unlockedAchievements);
    }

    public int getTotalAchievements() {
        return unlockedAchievements.size();
    }
}