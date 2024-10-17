package Enemy;

import java.awt.Color;
import entity.EnemyShip;

public class HpEnemyShip {

    private static double magentaProbability = 0.1; //set basic probability

    /**
     * set the probability of being a MAGENTA color.
     *
     * @param probability the probability to a value between 0 and 1.
     */

    public static void setItemEnemyProbability(double probability) {
        if (probability >= 0.0 && probability <= 1.0) {
            magentaProbability = probability;
        } else {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        }
    }

    /**
     * returns the probability of being the current MAGENTA color.
     *
     * @return current probability.
     */
    public static double getItemEnemyProbability() {
        return magentaProbability;
    }

    /**
     * Determine the color of the ship according to hp
     * @param hp
     * 			The ship's hp
     * @return if hp is 2, return yellow
     * 		   if hp is 3, return orange
     * 		   if hp is 1, return white
     */
    public static Color determineColor(int hp) {
        Color color = Color.WHITE; // Declare a variable to store the color
                                    // set basic color WHITE

        if (hp == 2)
            return new Color(0xFFEB3B);
        else if (hp == 3)
            return new Color(0xFFA500);
        else if (hp == 1)
            return Math.random() < magentaProbability ? Color.MAGENTA : Color.WHITE;
        return Color.WHITE;
    }

    /**
     * When the EnemyShip is hit and its hp reaches 0, destroy the ship
     * @param enemyShip
     *          The ship that was hit
     */
    public static void hit(EnemyShip enemyShip) {
        int hp = enemyShip.getHp();
        hp -= 1;
        enemyShip.setHp(hp);
        if (hp <= 0) {
            enemyShip.destroy();
        }
        if(!enemyShip.getColor().equals(Color.magenta))
            enemyShip.setColor(determineColor(hp));
    }


}
