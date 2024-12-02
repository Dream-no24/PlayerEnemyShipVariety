package ctrlS;

import engine.Core;
import engine.GameState;
// item level Bonus
import inventory_develop.ShipStatus;

import java.io.IOException;

public class RoundState {
    private final GameState prevState;
    private final GameState currState;
    private final int roundScore;
    private final int roundBulletsShot;
    private final int roundHitCount;
    private final int roundCoin;
    private final int roundCoinItemsCollected;
    private final int timeBonus_amount;
    private final int levelBonus_amount;
    private final int accuracyBonus_amount;
    private final int baseCoin_amount;

    private final float roundHitRate;
    private final long roundTime;

    private double statBonus;
    private static final int COIN_ITEM_VALUE = 10;

    public RoundState(GameState prevState, GameState currState) {
        this.prevState = prevState;
        this.currState = currState;
        this.roundScore = currState.getScore() - prevState.getScore();
        this.roundBulletsShot = currState.getBulletsShot() - prevState.getBulletsShot();
        this.roundHitCount = currState.getHitCount() - prevState.getHitCount();
        this.roundHitRate = roundHitCount / (float) roundBulletsShot;
        this.roundTime = currState.getTime() - prevState.getTime();
        this.roundCoinItemsCollected = currState.getCoinItemsCollected() - prevState.getCoinItemsCollected();
        //Calculate BaseCoin
        this.baseCoin_amount = calculate_BaseCoin();
        //Calculate levelBonus
        this.levelBonus_amount = calculate_levelBonus();
        //Calculate accuracyBonus
        this.accuracyBonus_amount = calculate_accuracyBonus();
        //Calculate timeBonus
        this.timeBonus_amount = calculate_timeBonus();


        //Calculate RoundCoin

        // CtrlS: load stat bonus via UpgradeManager
        try {
            this.statBonus = Core.getUpgradeManager().getCoinAcquisitionMultiplier();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.roundCoin = calculateCoin();
    }

    private int calculateCoin() {
        int coin, bonusCoin = 0;
        // If it's not Game Over, apply bonus
        if(currState.getLivesRemaining() > 0) {
            bonusCoin += this.levelBonus_amount + this.accuracyBonus_amount + this.timeBonus_amount;
            Core.getLogger().info("Bonus occurs! level bonus: " + this.levelBonus_amount + ", accuracy bonus: " + this.accuracyBonus_amount + ", time bonus: " + this.timeBonus_amount);
        }

        // Apply stat bonus
        coin = (int) ((this.baseCoin_amount + bonusCoin) * statBonus);
        Core.getLogger().info("coin = ((baseCoin(" + this.baseCoin_amount + ") + bonusCoin(" + bonusCoin + ")) * statBonus(" + statBonus + ") = " + coin);
        return coin;
    }

    private int calculate_BaseCoin() {
        int baseCoin;
        int coinItemsCollectedValue = COIN_ITEM_VALUE * this.roundCoinItemsCollected;
        int scoreCoin = roundScore / 10;
        baseCoin = coinItemsCollectedValue + scoreCoin;
        Core.getLogger().info("base coin = coinItemsCollectedValue(" + coinItemsCollectedValue + ") + scoreCoin(" + scoreCoin + ") = " + baseCoin);

        return baseCoin;
    }

    private int calculate_levelBonus() {
        int levelBonus_amount = (this.baseCoin_amount / 10) * currState.getLevel();

        return levelBonus_amount;
    }

    private int calculate_accuracyBonus() {
        int accuracyBonus = 0;
        if (roundHitRate > 0.9) {
            accuracyBonus += (int) (this.baseCoin_amount * 0.3);
            Core.getLogger().info("hitRate bonus occurs (30%).");
        } else if (roundHitRate > 0.8) {
            accuracyBonus += (int) (this.baseCoin_amount * 0.2);
            Core.getLogger().info("hitRate bonus occurs (20%).");
        } else if (roundHitRate > 0.7) {
            accuracyBonus += (int) (this.baseCoin_amount * 0.1);
            Core.getLogger().info("hitRate bonus occurs (10%).");
        }
        return accuracyBonus;
    }

    private int calculate_timeBonus() {
        long timeDifferenceInSeconds = (currState.getTime() - prevState.getTime()) / 1000;
        int timeBonus = 0;
        /*
          clear time   : 0 ~ 50    : +50
                       : 51 ~ 80   : +30
                       : 81 ~ 100  : +10
                       : 101 ~     : 0
         */
        if (timeDifferenceInSeconds <= 50) {
            timeBonus = 50;
        } else if (timeDifferenceInSeconds <= 80) {
            timeBonus = 30;
        } else if (timeDifferenceInSeconds <= 100) {
            timeBonus = 10;
        }
        return timeBonus;
    }
    public int getRoundScore() {
        return roundScore;
    }

    public float getRoundHitRate() {
        return roundHitRate;
    }

    public long getRoundTime() {
        return roundTime;
    }

    public int getRoundCoin() {
        return roundCoin;
    }

    public int getLevelBonus_amount() { return levelBonus_amount; }

    public int getTimeBonus_amount() { return timeBonus_amount; }

    public int getAccuracyBonus_amount() { return accuracyBonus_amount; }

    public int getBaseCoin_amount() { return baseCoin_amount; }

    public int getRoundCoinItemsCollected() { return roundCoinItemsCollected; }
}
