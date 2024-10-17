package inventory_develop;

import Sound_Operator.SoundManager; //Sound_Operator
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

    // Bomb를 먹을 때 true로 전환할 예정
    private static boolean IsBomb = false;
    // Bomb를 먹을 때 true로 전환할 예정
    private static boolean CanShoot = false;

    private static boolean isBombExploded = false; //CLOVE

    private static int totalPoint = 0; //CLOVE

    private static Set<EnemyShip> DestroyedshipByBomb = new HashSet<>();    // for dicide next shooter

    public Bomb() {
    }

    public static int[] destroyByBomb(List<List<EnemyShip>> enemyShips, EnemyShip destroyedShip, Logger logger) {
        int count = 0;   // number of destroyed enemy by Bomb
        int point = 0;  // point of destroyed enemy by Bomb

        for (List<EnemyShip> column : enemyShips)
            for (int i = 0; i < column.size(); i++) {
                if (column.get(i).equals(destroyedShip)) {

                    // middle
                    DestroyedshipByBomb.add(column.get(i));
                    //point += column.get(i).getPointValue(); //CLOVE-duplicate calculation
                    column.get(i).destroy();
                    count++;

                    int columnIndex = enemyShips.indexOf(column);
                    //Sound_Operator
                    sm = SoundManager.getInstance();
                    sm.playES("enemy_explosion");

                    // left
                    if (columnIndex > 0) {
                        List<EnemyShip> leftColumn = enemyShips.get(columnIndex - 1);
                        if (i < leftColumn.size() && inposition(column, leftColumn, i, i)) {
                            DestroyedshipByBomb.add(leftColumn.get(i));
                            point += leftColumn.get(i).getPointValue();
                            leftColumn.get(i).destroy();
                            count++;
                            logger.info("Destroyed left ship at (" + (columnIndex - 1) + "," + i + ")");
                        }
                    }

                    // right
                    if (columnIndex < enemyShips.size() - 1) {
                        List<EnemyShip> rightColumn = enemyShips.get(columnIndex + 1);
                        if (i < rightColumn.size() && inposition(column, rightColumn, i, i)) {
                            DestroyedshipByBomb.add(rightColumn.get(i));
                            point += rightColumn.get(i).getPointValue();
                            rightColumn.get(i).destroy();
                            count++;
                            logger.info("Destroyed right ship at (" + (columnIndex + 1) + "," + i + ")");

                        }
                    }

                    // top
                    if (i > 0) {
                        List<EnemyShip> currentColumn = enemyShips.get(columnIndex);
                        if (i - 1 < currentColumn.size() && inposition(column, currentColumn, i, i - 1)) {
                            DestroyedshipByBomb.add(currentColumn.get(i - 1));
                            point += currentColumn.get(i - 1).getPointValue();
                            currentColumn.get(i - 1).destroy();
                            count++;
                            logger.info("Destroyed top ship at (" + columnIndex + "," + (i - 1) + ")");
                        }
                    }

                    // bottom
                    List<EnemyShip> currentColumn = enemyShips.get(columnIndex);
                    if (i + 1 < currentColumn.size() && inposition(column, currentColumn, i, i + 1)) {
                        DestroyedshipByBomb.add(currentColumn.get(i + 1));
                        point += currentColumn.get(i + 1).getPointValue();
                        currentColumn.get(i + 1).destroy();
                        count++;
                        logger.info("Destroyed bottom ship at (" + columnIndex + "," + (i + 1) + ")");
                    }
                }
            }

        isBombExploded = true; //CLOVE
        totalPoint += point; //CLOVE

        Bomb.setIsbomb(false);
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

    public static boolean inposition(List<EnemyShip> column, List<EnemyShip> nextcolumn, int pos, int nextpos){
        int distanceY = column.get(pos).getPositionY() - nextcolumn.get(nextpos).getPositionY();
        int distanceX = column.get(pos).getPositionX() - nextcolumn.get(nextpos).getPositionX();
        
        return (distanceY >= -60 && distanceX >= -60) && (distanceY <= 60 && distanceX <= 60);
    }

}