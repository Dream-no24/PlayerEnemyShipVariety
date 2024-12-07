// BossPartsTest.java
// 이 테스트 파일은 BossParts 클래스의 주요 메서드들을 테스트하는 목적을 가집니다.

package entity;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.awt.*;

class BossPartsTest {

    private BossParts bossParts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bossParts = new BossParts(0, 0, 50, 50, SpriteType.BossACore1, 100);
    }

    @Test
    void testGetPointValue() {
        assertThat(bossParts.getPointValue()).isEqualTo(1000);
    }

    @Test
    void testMove() {
        bossParts.move(10, 20);
        assertThat(bossParts.positionX).isEqualTo(10);
        assertThat(bossParts.positionY).isEqualTo(20);
    }

    @Test
    void testUpdate() {
        Cooldown mockCooldown = mock(Cooldown.class);
        when(mockCooldown.checkFinished()).thenReturn(true);

        bossParts.update();

        assertThat(bossParts.spriteType).isEqualTo(SpriteType.BossACore2);
    }

    @Test
    void testDestroy() {
        bossParts.destroy();
        assertThat(bossParts.isDestroyed()).isTrue();
        assertThat(bossParts.spriteType).isEqualTo(SpriteType.Explosion);
    }

    @Test
    void testGetHp() {
        assertThat(bossParts.getHp()).isEqualTo(100);
    }

    @Test
    void testSetHp() {
        bossParts.setHp(50);
        assertThat(bossParts.getHp()).isEqualTo(50);
    }

    @Test
    void testIsDestroyed() {
        bossParts.destroy();
        assertThat(bossParts.isDestroyed()).isTrue();
    }

    @Test
    void testIsCore() {
        assertThat(bossParts.isCore()).isTrue();
    }

    @Test
    void testIsBoss() {
        BossParts b1 = new BossParts(0, 0, 50, 50, SpriteType.EnemyShipA1, 100);
        BossParts b2 = new BossParts(0, 0, 50, 50, SpriteType.BossACore1, 100);

        assertThat(b1.isBoss()).isFalse();
        assertThat(b2.isBoss()).isTrue();

    }

    @Test
    void testIsCoreDestroyed() {
        bossParts.destroy();
        assertThat(bossParts.isCoreDestroyed()).isTrue();
    }

    @Test
    void testDetermineColor() {
        Color color = BossParts.determineColor(100, 100);
        assertThat(color.getRed()).isLessThan(10);
        assertThat(color.getGreen()).isLessThan(10);
        assertThat(color.getBlue()).isGreaterThan(245);

        color = BossParts.determineColor(1, 100);
        System.out.println(color);
        assertThat(color.getRed()).isGreaterThan(245);
        assertThat(color.getGreen()).isLessThan(10);
        assertThat(color.getBlue()).isLessThan(10);
    }

    @Test
    void testHit() {
        Cooldown mockCooldown = mock(Cooldown.class);
        when(mockCooldown.checkFinished()).thenReturn(true);

        BossParts.hit(bossParts);
        assertThat(bossParts.getHp()).isEqualTo(99);
    }
}
