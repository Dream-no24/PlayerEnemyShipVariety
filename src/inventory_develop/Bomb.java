package inventory_develop;

import enemy.ItemManager;
import sound_Operator.SoundManager; //Sound_Operator
import engine.DrawManager;
import entity.EnemyShip;
import entity.EnemyShipFormation;
import entity.Entity;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import clove.ScoreManager; //CLOVE

public class Bomb{
    // Sound Operator
    private static SoundManager sm;
    private int BombSpeed;

    private static boolean IsBomb = false;
    private static boolean CanShoot = false;
    private static int IndexRange = 1;
    private static int PositionRange = 60;
    private static int PositionRange_isCircle = 60;

    private static boolean isBombExploded = false; //CLOVE

    private static int totalPoint = 0; //CLOVE

    private static Set<EnemyShip> DestroyedshipByBomb = new HashSet<>();    // for dicide next shooter

    public Bomb() {
    }

    // if EnemyShipFormation is not Circle
    public static int[] destroyByBomb(List<List<EnemyShip>> enemyShips, EnemyShip destroyedShip, ItemManager itemManager, Logger logger) {
        int count = 0;   // number of destroyed enemy by Bomb
        int point = 0;  // point of destroyed enemy by Bomb

        for (List<EnemyShip> column : enemyShips)
            for (int i = 0; i < column.size(); i++) {
                if (column.get(i).equals(destroyedShip)) {

                    // middle
                    DestroyedshipByBomb.add(column.get(i));
                    if (column.get(i).getColor().equals(Color.MAGENTA)) { //add by team enemy
                        itemManager.dropItem(column.get(i), 1, 1);
                    }
                    //point += column.get(i).getPointValue(); //CLOVE-duplicate calculation
                    column.get(i).destroy();
                    count++;

                    int columnIndex = enemyShips.indexOf(column);
                    //Sound_Operator
                    sm = SoundManager.getInstance();
                    sm.playES("enemy_explosion");

                    // Operate according to IndexRange
                    for (int dx = -IndexRange; dx <= IndexRange; dx++) {
                        for (int dy = -IndexRange; dy <= IndexRange; dy++) {
                            if (dx == 0 && dy == 0) continue;

                            int newColumnIndex = columnIndex + dx;
                            int newRowIndex = i + dy;

                            if (newColumnIndex >= 0 && newColumnIndex < enemyShips.size()) {
                                List<EnemyShip> targetColumn = enemyShips.get(newColumnIndex);
                                if (newRowIndex >= 0 && newRowIndex < targetColumn.size() && inposition(column, targetColumn, i, newRowIndex, PositionRange)) {
                                    EnemyShip targetShip = targetColumn.get(newRowIndex);
                                    DestroyedshipByBomb.add(targetShip);
                                    if (targetShip.getColor().equals(Color.MAGENTA)) { //add by team enemy
                                        itemManager.dropItem(targetShip, 1, 1);
                                    }
//                                    point += targetShip.getPointValue();
                                    targetShip.destroy();
                                    count++;
                                    logger.info("Destroyed ship at (" + newColumnIndex + "," + newRowIndex + ")");
                                }
                            }
                        }
                    }
                }
            }

        isBombExploded = true; //CLOVE
        totalPoint += point; //CLOVE

        Bomb.setIsbomb(false);
        int[] returnValue = {count, point};

        return returnValue;
    }

    // if EnemyShipFormation is Circle
    public static int[] destroyByBomb_isCircle(List<List<EnemyShip>> enemyShips, EnemyShip destroyedShip, ItemManager itemManager, Logger logger) {
        int count = 0;   // number of destroyed enemy by Bomb
        int point = 0;   // point of destroyed enemy by Bomb

        for (List<EnemyShip> column : enemyShips)
            for (int i = 0; i < column.size(); i++) {
                if (column.get(i).equals(destroyedShip)) {

                    DestroyedshipByBomb.add(column.get(i));
                    //point += column.get(i).getPointValue(); //CLOVE-duplicate calculation
                    column.get(i).destroy();
                    count++;

                    int columnIndex = enemyShips.indexOf(column);

                    for (List<EnemyShip> column2 : enemyShips)
                        for (int j = 0; j < column.size(); j++) {

                            int column2Index = enemyShips.indexOf(column2);
                            double distance = Math.sqrt(Math.pow(columnIndex - column2Index, 2) + Math.pow(i - j, 2));
                            System.out.println(distance);
                            if (j >= 0 && j < column2.size() && inposition(column, column2, i, j, PositionRange_isCircle)) {
                                int newColumnIndex = enemyShips.indexOf(column2);
                                EnemyShip targetShip = column2.get(j);
                                DestroyedshipByBomb.add(targetShip);
                                if (targetShip.getColor().equals(Color.MAGENTA)) {
                                    itemManager.dropItem(targetShip, 1, 1);
                                }
                                targetShip.destroy();
                                count++;
                                logger.info("Destroyed ship at (" + newColumnIndex + "," + j + ")");
                            }
                        }

                }
            }

        isBombExploded = true; //CLOVE
        totalPoint += point; //CLOVE

        int[] returnValue = {count, point};

        return returnValue;
    }

    public static void nextShooterByBomb(List<List<EnemyShip>> enemyShips, List<EnemyShip> shooters,
                                          EnemyShipFormation enemyShipFormation, Logger logger) {

        for (EnemyShip destroyedByBomb : DestroyedshipByBomb)

            if (destroyedByBomb.isDestroyed()) {
                if (shooters.contains(destroyedByBomb)) {
                    int destroyedShipIndex = shooters.indexOf(destroyedByBomb);
                    int destroyedShipColumnIndex = -1;

                    for (List<EnemyShip> column : enemyShips)
                        if (column.contains(destroyedByBomb)) {
                            destroyedShipColumnIndex = enemyShips.indexOf(column);
                            break;
                        }

                    EnemyShip nextShooter = enemyShipFormation.getNextShooter(enemyShips
                            .get(destroyedShipColumnIndex));

                    if (nextShooter != null)
                        shooters.set(destroyedShipIndex, nextShooter);
                    else {
                        shooters.remove(destroyedShipIndex);
                        logger.info("Shooters list reduced to "
                                + shooters.size() + " members.");
                    }
                }
            }
    }

    public static int getTotalPoint() { return totalPoint; }

    public static boolean isBombExploded() { return isBombExploded; }

    public static void resetBombExploded() { isBombExploded = false;}

    public static boolean getIsBomb() {
        return IsBomb;
    }

    public static void setIsbomb(boolean isbomb) {
        IsBomb = isbomb;
    }

    public static boolean getCanShoot() {
        return CanShoot;
    }

    public static void setCanShoot(boolean canshoot) {
        CanShoot = canshoot;
    }

    public final void setSpeed(final int BoobSpeed) {this.BombSpeed = BoobSpeed;}

    public final int getSpeed() {return this.BombSpeed;}

    public static boolean inposition(List<EnemyShip> column, List<EnemyShip> nextcolumn, int pos, int nextpos, int range){
        int distanceY = column.get(pos).getPositionY() - nextcolumn.get(nextpos).getPositionY();
        int distanceX = column.get(pos).getPositionX() - nextcolumn.get(nextpos).getPositionX();

        boolean result = Math.pow(distanceX, 2) + Math.pow(distanceY, 2) <= Math.pow(range, 2);
        
        return result;
    }

}