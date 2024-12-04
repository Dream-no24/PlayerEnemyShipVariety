package entity;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.awt.Color;
import java.util.*;

import enemy.ItemManager;
import enemy.PiercingBullet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import inventory_develop.NumberOfBullet;
import engine.Cooldown;
import engine.DrawManager;
import engine.GameSettings;
import screen.Screen;
import clove.ScoreManager;

/**
 * Test class for BossFormation.
 */
public class BossFormationTest {

    private BossFormation bossFormation;
    private GameSettings gameSettings;
    private BossVariety bossVariety;
    private Screen screen;
    private ScoreManager scoreManager;
    private ItemManager itemManager;
    private Cooldown cooldown;
    private BossParts bossParts;

    @BeforeEach
    public void setUp() {
        gameSettings = mock(GameSettings.class);
        bossVariety = mock(BossVariety.class);
        screen = mock(Screen.class);
        scoreManager = mock(ScoreManager.class);
        itemManager = mock(ItemManager.class);
        cooldown = mock(Cooldown.class);
        bossParts = mock(BossParts.class);

        when(gameSettings.getFormationWidth()).thenReturn(1);
        when(gameSettings.getFormationHeight()).thenReturn(1);
        when(gameSettings.getShootingFrequency()).thenReturn(10);
        when(gameSettings.getBaseSpeed()).thenReturn(20);
        when(bossVariety.getSeparationDistance()).thenReturn(30);
        when(bossVariety.getSpriteTypes()).thenReturn(Arrays.asList(
                DrawManager.SpriteType.BossBCore1,
                DrawManager.SpriteType.BossBCore2,
                DrawManager.SpriteType.BossBCoreDamaged,
                DrawManager.SpriteType.BossBCoreDamaged2,
                DrawManager.SpriteType.BossBCoreShell)
        );
        when(bossVariety.getHealthPerPart()).thenReturn(10);

        bossFormation = new BossFormation(gameSettings, bossVariety);
        bossFormation.setScreen(screen);
        bossFormation.setScoreManager(scoreManager);
        bossFormation.setItemManager(itemManager);

        List<List<BossParts>> newFormation = new ArrayList<>();
        newFormation.add(Collections.singletonList(bossParts));
        bossFormation.setBossPartsFormation(newFormation);
    }

    // 단위 테스트: getScoreManager() 메서드
    @Test
    public void testGetScoreManager() {
        assertThat(bossFormation.getScoreManager()).isEqualTo(scoreManager);
    }

    // 단위 테스트: getItemManager() 메서드
    @Test
    public void testGetItemManager() {
        assertThat(bossFormation.getItemManager()).isEqualTo(itemManager);
    }

    // 단위 테스트: setScoreManager() 메서드
    @Test
    public void testSetScoreManager() {
        ScoreManager newScoreManager = mock(ScoreManager.class);
        bossFormation.setScoreManager(newScoreManager);
        assertThat(bossFormation.getScoreManager()).isEqualTo(newScoreManager);
    }

    // 단위 테스트: setItemManager() 메서드
    @Test
    public void testSetItemManager() {
        ItemManager newItemManager = mock(ItemManager.class);
        bossFormation.setItemManager(newItemManager);
        assertThat(bossFormation.getItemManager()).isEqualTo(newItemManager);
    }

    // 단위 테스트: getBossPartsFormation() 메서드
    @Test
    public void testGetBossPartsFormation() {
        assertThat(bossFormation.getBossPartsFormation()).isNotNull();
    }

    // 단위 테스트: setBossPartsFormation() 메서드
    @Test
    public void testSetBossPartsFormation() {
        List<List<BossParts>> newFormation = new ArrayList<>();
        newFormation.add(Collections.singletonList(bossParts));
        bossFormation.setBossPartsFormation(newFormation);
        assertThat(bossFormation.getBossPartsFormation()).isEqualTo(newFormation);
    }

    // 단위 테스트: getMovementSpeed() 메서드
    @Test
    public void testGetMovementSpeed() {
        bossFormation.update();
        assertThat(bossFormation.getMovementSpeed()).isEqualTo(30); // 기본 값: baseSpeed + MINIMUM_SPEED
    }

    // 단위 테스트: setMovementSpeed() 메서드
    @Test
    public void testSetMovementSpeed() {
        int newSpeed = 50;
        bossFormation.setMovementSpeed(newSpeed);
        assertThat(bossFormation.getMovementSpeed()).isEqualTo(newSpeed);
    }

    // 단위 테스트: getScreen() 메서드
    @Test
    public void testGetScreen() {
        assertThat(bossFormation.getScreen()).isEqualTo(screen);
    }

    // 단위 테스트: setScreen() 메서드
    @Test
    public void testSetScreen() {
        Screen newScreen = mock(Screen.class);
        bossFormation.setScreen(newScreen);
        assertThat(bossFormation.getScreen()).isEqualTo(newScreen);
    }

    // 경계값 테스트: update() 메서드 - 포메이션 위치 및 방향 확인
    @Test
    public void testUpdateBoundaryConditions() {
        when(screen.getWidth()).thenReturn(800);
        when(screen.getHeight()).thenReturn(600);

        // 포메이션을 경계에 위치시키기 위한 초기 설정
        bossFormation.setMovementInterval(100);
        bossFormation.setCurrentDirection(BossFormation.Direction.RIGHT);

        BossParts bossParts1 = new BossParts(790,590,1,1, DrawManager.SpriteType.BossACore1,100);
        List<List<BossParts>> newFormation = new ArrayList<>();
        newFormation.add(Collections.singletonList(bossParts1));
        bossFormation.setBossPartsFormation(newFormation);

        bossFormation.update();

        // 포메이션이 오른쪽 끝에 도달했을 때 올바르게 방향을 전환하는지 확인
        assertThat(bossFormation.getCurrentDirection()).isEqualTo(BossFormation.Direction.LEFT);

        // 포메이션이 아래쪽 끝에 도달했을 때 올바르게 방향을 전환하는지 확인
        assertThat(bossFormation.getCurrentDirection()).isEqualTo(BossFormation.Direction.LEFT);

        // 포메이션이 경계를 초과하지 않는지 확인
        assertThat(bossFormation.getPositionX()).isLessThanOrEqualTo(800);
        assertThat(bossFormation.getPositionY()).isLessThanOrEqualTo(600);
    }



    // 예외 테스트: cleanUp() 메서드
    @Test
    public void testCleanUpNoException() {

        // 긴접적으로 실행
        bossFormation.update();

        // 클린업이 예외를 발생시키지 않고 정상 동작하는지 확인
        assertThat(bossFormation.getBossPartsFormation()).isNotNull();
    }

    // 모킹 테스트: shoot() 메서드
    @Test
    public void testShootWithMockedCooldown() {
        Set<PiercingBullet> bullets = new HashSet<>();
        Cooldown mockShootingCooldown = mock(Cooldown.class);
        when(mockShootingCooldown.checkFinished()).thenReturn(true);
        bossFormation.setShootingCooldown(mockShootingCooldown);

        bossFormation.shoot(bullets);

        verify(mockShootingCooldown, atLeastOnce()).checkFinished();
        assertThat(bullets).isNotEmpty();
    }

    // 모킹 테스트: destroy() 메서드
    @Test
    public void testDestroyWithMockedScoreManager() {
        BossParts destroyedPart = mock(BossParts.class);
        when(destroyedPart.isCoreDestroyed()).thenReturn(true);

        int[] result = bossFormation.destroy(destroyedPart);

        verify(scoreManager, atLeastOnce()).addScore(anyInt());
        assertThat(result).isNotNull();
    }

    // 모킹 테스트: reflect() 메서드
//    @Test
//    public void testReflectWithMockedNumberOfBullet() {
//        bossFormation.getNumberOfBullet().setBulletLevel(1);
//        bossFormation.getNumberOfBullet().setPiercingbulletLevel(1);
//        Set<PiercingBullet> bullets = bossFormation.getNumberOfBullet().addBullet(
//                1,
//                1,
//                10,
//                false,
//                Color.YELLOW
//        );
//        int prev = bullets.size();
//        boolean hasRed = false;
//
//        bossFormation.reflect(bullets, 100, 100);
//
//        assertThat(bullets).isNotEmpty();
//        assertThat(prev +  bossFormation.getNumberOfBullet().getBulletLevel()).isEqualTo(bullets.size());
//
//        for (PiercingBullet bullet : bullets) {
//            hasRed = (bullet.getColor().equals(Color.RED));
//            System.out.println("X : " + bullet.getPositionX());
//            System.out.println("Y : " + bullet.getPositionY());
//            System.out.println("Speed : " + bullet.getSpeed());
//            System.out.println("Color : " + bullet.getColor());
//            if (hasRed) {
//                break;
//            }
//        }
//        assertThat(hasRed).isTrue();
//    }

    // 모킹 테스트: iterator() 메서드
    @Test
    public void testIterator() {
        Iterator<BossParts> iterator = bossFormation.iterator();
        assertThat(iterator).isNotNull();
    }

    // 경계값 테스트: isEmpty() 메서드
    @Test
    public void testIsEmpty() {
        for (BossParts b : bossFormation) {
            bossFormation.destroy(b);
            bossFormation.update();
        }

        assertThat(bossFormation.isEmpty()).isTrue();
    }
}
