package clove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import engine.Core;
import engine.DrawManager;
import engine.GameState;
import clove.Statistics;
import hudTeam.DrawAchievementHud;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class AchievementConditions {

    private DrawManager drawManager;

    private GameState gameState;
    private static Logger logger;

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
    private List<Achievement> fastKillAchievements = new ArrayList<>();
    private List<Achievement> noDeathAchievements = new ArrayList<>();
    private List<Achievement> stageAchievements = new ArrayList<>();
    private List<Achievement> scoreAchievements = new ArrayList<>();
    private List<Achievement> allAchievements = new ArrayList<>();
    private List<String> unlockedAchievements = new ArrayList<>();

    private static final int FAST_KILL_TIME = 5;

    public AchievementConditions() {
        initializeAchievements();

        try{
            this.stats = new Statistics();
            setStatistics();
        } catch (IOException e){
            logger.warning("Couldn't load Statistics!");
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        startFastKillCheck();
    }

    private void setStatistics() throws IOException {
        this.stats = stats.loadUserData(stats);
    }

    private int enemiesKilledIn3Seconds = 0;

    public void startFastKillCheck() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (enemiesKilledIn3Seconds > 0) {
                    fastKill(enemiesKilledIn3Seconds);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stopFastKillCheck() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public AchievementConditions(DrawManager drawManager) {
        this.drawManager = drawManager;
        initializeAchievements();
    }

    public void initializeAchievements() {

        // TODO Some of the codes below are non-operational.(fastKillAchievement,trialAchievement) Will be updated if related record fuctions are added.
        noDeathAchievements.add(new Achievement("Aerobatics","Maintain Maximum Life",3, Achievement.AchievementType.LIVES));

        killAchievements.add(new Achievement("Rookie Pilot", "Destroy 25 enemies", 25, Achievement.AchievementType.KILLS, 1));
        killAchievements.add(new Achievement("Space Hunter", "Destroy 50 enemies", 100, Achievement.AchievementType.KILLS, 1));
        killAchievements.add(new Achievement("Space Trooper", "Destroy 100 enemies", 250, Achievement.AchievementType.KILLS, 1));
        killAchievements.add(new Achievement("Guardian of Universe", "Destroy 200 enemies", 500, Achievement.AchievementType.KILLS, 1));

        trialAchievements.add(new Achievement("Welcome Recruit", "Finished first game", 1, Achievement.AchievementType.TRIALS, 1));
        trialAchievements.add(new Achievement("Skilled Solider", "Finished 10th game", 10, Achievement.AchievementType.TRIALS, 1));
        trialAchievements.add(new Achievement("Veteran Pilot", "Finished 50th game", 50, Achievement.AchievementType.TRIALS, 1));

        streakAchievements.add(new Achievement("Preheating", "Kill streak of 10", 10, Achievement.AchievementType.KILLSTREAKS, 1));
        streakAchievements.add(new Achievement("Overheating", "Kill streak of 30", 30, Achievement.AchievementType.KILLSTREAKS, 5));
        streakAchievements.add(new Achievement("Runaway","Kill streak of 60", 60, Achievement.AchievementType.KILLSTREAKS, 10));

        fastKillAchievements.add(new Achievement("Gunsliger","Kill 3 enemies 5 seconds", 3, Achievement.AchievementType.FASTKILL,5));
        fastKillAchievements.add(new Achievement("Fear the Enemy","Kill 5 enemies 5 seconds", 5, Achievement.AchievementType.FASTKILL, 5));
        fastKillAchievements.add(new Achievement("Genocide","Kill 15 enemies 5 seconds", 15, Achievement.AchievementType.FASTKILL, 5));

        scoreAchievements.add(new Achievement("First Milestone", "Reach 1,000 points", 6000, Achievement.AchievementType.SCORE,1));
        scoreAchievements.add(new Achievement("Score Hunter", "Reach 5,000 points", 15000, Achievement.AchievementType.SCORE,3));
        scoreAchievements.add(new Achievement("Score Master", "Reach 10,000 points", 30000, Achievement.AchievementType.SCORE,5));

        stageAchievements.add(new Achievement("Home Sweet Home","Cleared Final Stage", Core.NUM_LEVELS, Achievement.AchievementType.STAGE, 5));

        allAchievements.addAll(killAchievements);
        allAchievements.addAll(trialAchievements);
        allAchievements.addAll(streakAchievements);
        allAchievements.addAll(fastKillAchievements);
        allAchievements.addAll(noDeathAchievements);
        allAchievements.addAll(stageAchievements);
        allAchievements.addAll(scoreAchievements);

        allAchievements.add(new Achievement("Medal of Honor", "Complete all achievements", 0, Achievement.AchievementType.STAGE, 10));
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
    // TODO Not functioning well
    public void fastKill(int killCount) throws IOException {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastKillTime <= TIME_LIMIT) {
            resetKillCountIfNeeded();
        } else {
            enemiesKilledIn3Seconds = 0;
        }
        lastKillTime = currentTime;

        if (enemiesKilledIn3Seconds > 0) {
            //System.out.println("Kills in last 3 seconds: " + enemiesKilledIn3Seconds);
        }
        if (enemiesKilledIn3Seconds >= Achievement.getRequiredFastKills()) {
            System.out.println("Checking fastkill achievements. Current fastkills: " + killCount);
            for (Achievement achievement : fastKillAchievements) {
                if (!achievement.isCompleted()) {
                    completeAchievement(achievement);
                }
            }
        }
    }

    public void checkNoDeathAchievements(int lives) {
        System.out.println("Checking No Death achievements. Current lives: " + lives);
        if (lives == Core.MAX_LIVES) {
            for (Achievement achievement : noDeathAchievements) {
                if (highestLevel==7 && !achievement.isCompleted()) {
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

    // TODO function getShipsDestructionStreak not added yet
    public void killStreak() throws IOException {
        int shipsDestructionStreak = stats.getShipsDestructionStreak();
        System.out.println("Checking killstreak achievements. Current killstreaks: " + shipsDestructionStreak);
        System.out.println("Killstreak Achievements size: " + streakAchievements.size());
        for (Achievement achievement : streakAchievements) {
            System.out.println("Checking " + achievement.getAchievementName());
            if (shipsDestructionStreak>= achievement.getRequiredKillStreaks() && !achievement.isCompleted()) {
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

    /*
    // TODO Achievements to add : Hidden Achievements
    */

    private void completeAchievement(Achievement achievement) {
        if (!unlockedAchievements.contains(achievement.getAchievementName())) {
            System.out.println("Achievement Unlocked: " + achievement.getAchievementName() + " - " + achievement.getAchievementDescription());
            unlockedAchievements.add(achievement.getAchievementName());
            achievement.completeAchievement();
            getUnlockedAchievements();
            System.out.println("Unlocked achievements list: " + unlockedAchievements);
            DrawAchievementHud.achieve(achievement.getAchievementName());
            if(achievement.getGem() > 0) {
                try {
                    Core.getCurrencyManager().addGem(achievement.getGem());
                } catch (IOException e) {
                    logger.warning("Couldn't load gem!");
                }
            }
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