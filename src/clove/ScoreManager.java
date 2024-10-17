package clove;

import java.time.Instant;
import inventory_develop.Bomb;

public class ScoreManager {

    private int levelScore;
    private int accumulatedScore = 0;
    private int level;

    // Constructor - initializes level
    public ScoreManager(int level, int score) {
        this.level = level;
        this.accumulatedScore = score;
    }

    public void setScore(int score){
        this.accumulatedScore = score;
    }

    // Adds score based on enemy score and level
    public void addScore(int enemyScore) {
        int scoreToAdd = enemyScore * this.level;
        if (Bomb.isBombExploded()){
            scoreToAdd += (250 + (Bomb.getTotalPoint() * this.level));
            Bomb.resetBombExploded();
        }
        this.levelScore += scoreToAdd;
        this.accumulatedScore += scoreToAdd;
        //System.out.println("Enemy destroyed. Score added: " + scoreToAdd + ", Level Score: " + this.levelScore);
    }

    public int getAccumulatedScore() {
        return accumulatedScore;
    }

    public int getLevel() {
        return level;
    }
}
