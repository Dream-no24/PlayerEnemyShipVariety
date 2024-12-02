package inventory_develop;
import engine.Core;
import engine.DrawManager;
import screen.GameScreen;
import entity.Ship;
import enemy.PlayerGrowth;

import java.util.logging.Logger;

public class ItemBarrierAndHeart {

    private static final int barrier_DURATION = 3000;
    private static final int MAX_LIVES = 3;

    private boolean barrierActive = false;
    private long barrierActivationTime;
    protected Logger logger = Core.getLogger();

    public ItemBarrierAndHeart() {
    }

    //barrier
    public void updateBarrierAndShip(Ship ship) {
        if (this.barrierActive) {
            ship.setSpriteType(DrawManager.SpriteType.ShipBarrierStatus);

            long currentTime = System.currentTimeMillis();

            if (currentTime - this.barrierActivationTime >= barrier_DURATION) {
                ship.setSpriteType(DrawManager.SpriteType.Ship);
                deactivatebarrier();    // deactive barrier
            }
        } else {    // When barrier is not active, call the ship's update()
            ship.update();
        }
    }

    public void activatebarrier() {
        this.barrierActive = true;
        this.barrierActivationTime = System.currentTimeMillis();
    }

    public void deactivatebarrier() {
        this.barrierActive = false;
        this.logger.info("barrier effect ends");
    }

    public boolean isbarrierActive() {
        return barrierActive;
    }

    //heart
    public void activeheart(GameScreen gameScreen) {
        if (gameScreen.getLives() < MAX_LIVES) {
            gameScreen.setLives(gameScreen.getLives() + 1);
        }
    }
}