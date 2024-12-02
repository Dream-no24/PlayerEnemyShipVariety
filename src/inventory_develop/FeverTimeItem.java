package inventory_develop;

import sound_Operator.SoundManager;
import engine.Core;

import java.util.logging.Logger;


public class FeverTimeItem {

    private static final long FEVER_DURATION = 5000; // 5 second
    private long startTime;
    private boolean isActive;
    protected Logger logger = Core.getLogger();
    //Sound Operator
    private static SoundManager sm;


    public FeverTimeItem() {
        this.isActive = false;
    }

    // Activate fever time item
    public void activate() {
        this.isActive = true;
        this.startTime = System.currentTimeMillis();
        //Sound Operator
        sm = SoundManager.getInstance();
        sm.playES("fever_time");
        logger.info("Fever Time activated!");


    }

    // Check whether fever time has ended every frame
    public void update() {
        if (isActive) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > FEVER_DURATION) {
                deactivate(); // 5초 지나면 비활성화
            }
        }
    }

    // Fever time ends
    private void deactivate() {
        this.isActive = false;
        logger.info("Fever Time effect ends");
    }

    // Check if Fevertime is currently enabled
    public boolean isActive() {
        return isActive;
    }
}
