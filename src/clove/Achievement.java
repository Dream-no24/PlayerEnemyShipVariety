package clove;

public class Achievement {

    public enum AchievementType {
        KILLS,
        KILLSTREAKS,
        LIVES,
        STAGE,
        TRIALS,
        SCORE,
        ACCURACY
    }

    private String achievementName;
    private String achievementDescription;
    private int requiredKills;
    private int requiredKillStreaks;
    private int requiredScore;
    private int requiredStages;
    private int requiredLives;
    private int requiredAccuracy;
    private int requiredTrials;
    private boolean isCompleted;
    private AchievementType achievementType;

    public Achievement(String achievementName, String achievementDescription, int requiredValue, AchievementType type) {
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.isCompleted = false;
        this.achievementType = type;

        // Assign values to appropriate fields based on type
        switch (type) {
            case KILLS:
                requiredKills = requiredValue;
                break;
            case KILLSTREAKS:
                this.requiredKillStreaks = requiredValue;
                break;
            case SCORE:
                this.requiredScore = requiredValue;
                break;
            case STAGE:
                this.requiredStages = requiredValue;
            case TRIALS:
                this.requiredTrials = requiredValue;
                break;
            case ACCURACY:
                this.requiredAccuracy = requiredValue;
                break;
            case LIVES:
                this.requiredLives = requiredValue;
                break;
            default:
                throw new IllegalArgumentException("Unsupported AchievementType: " + type);
        }
    }

    public void logCompleteAchievement() {
        this.isCompleted = true;
        System.out.println("Achievement completed: " + achievementName);
    }

    public AchievementType getType() {
        return achievementType;
    }

    public int getRequiredScore() {
        return requiredScore;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    public int getRequiredAccuracy() {
        return requiredAccuracy;
    }

    public int getRequiredKillStreaks() {
        return requiredKillStreaks;
    }

    public int getRequiredTrials() {
        return requiredTrials;
    }

    public int getRequiredStages() {
        return requiredStages;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean checkKillConditions(Achievement achievement, int currentKills) {
        System.out.println("Checking kill conditions for achievement: " + achievement.getAchievementName());
        if (achievement.getType() == Achievement.AchievementType.KILLS) {
            System.out.println("Current kills: " + currentKills + ", Required kills: " + achievement.getRequiredKills());
            return currentKills >= achievement.getRequiredKills();
        }
        return false;
    }

    public boolean checkKillStreakConditions(Achievement achievement, int currentKillStreak) {
        System.out.println("Checking killstreak conditions for achievement: " + achievement.getAchievementName());
        if (achievement.getType() == AchievementType.KILLSTREAKS) {
            System.out.println("Current killstreaks: " + currentKillStreak + ", Required killstreaks: " + achievement.getRequiredKillStreaks());
            return currentKillStreak >= achievement.getRequiredKillStreaks();
        }
        return false;
    }

    public boolean checkRequiredTrials(Achievement achievement, int currentTrials) {
        System.out.println("Checking trial conditions for achievement " + achievement.getAchievementName());
        if (achievement.getType() == Achievement.AchievementType.TRIALS) {
            System.out.println("Current trials: " + currentTrials + ", Required trials: " + achievement.getRequiredTrials());
            return currentTrials >= achievement.getRequiredTrials();
        }
        return false;
    }

    public boolean checkStageConditions(Achievement achievement, int currentStages) {
        System.out.println("Checking stage conditions for achievement: " + achievement.getAchievementName());
        if (achievement.getType() == Achievement.AchievementType.STAGE) {
            System.out.println("Current stages: " + currentStages + ", Required stages: " + achievement.getRequiredStages());
            return currentStages >= achievement.getRequiredStages();
        }
        return false;
    }

    public boolean checkScoreConditions(Achievement achievement, int currentScore) {
        System.out.println("Checking score conditions for achievement: " + achievement.getAchievementName());
        if (achievement.getType() == Achievement.AchievementType.SCORE) {
            System.out.println("Current score: " + currentScore + ", Required score: " + achievement.getRequiredScore());
            return currentScore >= achievement.getRequiredScore();
        }
        return false;
    }

    public boolean checkLivesCondition(int currentLives) {
        if (this.achievementType == AchievementType.LIVES) {
            return currentLives >= requiredLives;
        }
        return false;
    }

    public void completeAchievement() {
        if (!isCompleted) {
            isCompleted = true;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return achievementName.equals(that.achievementName);
    }

    @Override
    public int hashCode() {
        return achievementName.hashCode();
    }

    @Override
    public String toString() {
        return "Achievement{" + "Achieved: " + achievementName + " - " + achievementDescription + "}";
    }
}