package Enemy;

import java.awt.Color;
import entity.EnemyShip;

public class HpEnemyShip {

    /**
     * Determine the color of the ship according to hp
     * @param hp
     * 			The ship's hp
     * @return if hp is 2, return yellow
     * 		   if hp is 3, return orange
     * 		   if hp is 1, return white
     */
    public static Color determineColor(int hp) {
        if (hp == 2)
            return new Color(0xFFEB3B);
        else if (hp == 3)
            return new Color(0xFFA500);
        return new Color(0xFFFFFF);
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
        enemyShip.setColor(determineColor(hp));
    }


}
