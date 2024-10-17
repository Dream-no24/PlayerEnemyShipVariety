package inventory_develop;

import engine.Core;

import java.util.logging.Logger;


public class FeverTimeItem {

    private static final long FEVER_DURATION = 5000; // 5초 동안 유지
    private long startTime;
    private boolean isActive;
    protected Logger logger = Core.getLogger();


    public FeverTimeItem() {
        this.isActive = false;
    }

    // 피버타임 아이템을 활성화
    public void activate() {
        this.isActive = true;
        this.startTime = System.currentTimeMillis();
        logger.info("Fever Time activated!");


    }

    // 매 프레임마다 피버타임이 끝났는지 체크
    public void update() {
        if (isActive) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > FEVER_DURATION) {
                deactivate(); // 5초 지나면 비활성화
            }
        }
    }

    // 피버타임 종료
    private void deactivate() {
        this.isActive = false;
        logger.info("Fever Time effect ends");
    }

    // 현재 피버타임이 활성화되어 있는지 확인
    public boolean isActive() {
        return isActive;
    }
}
