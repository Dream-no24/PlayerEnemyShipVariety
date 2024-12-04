// TestBossVariety.java
// 이 테스트 파일은 BossVariety 클래스의 메서드와 로직을 검증하기 위해 작성되었습니다.

package entity;

import engine.DrawManager.SpriteType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BossVarietyTest {

    private BossVariety crabBoss;
    private BossVariety turtleBoss;
    private List<SpriteType> mockSpriteTypes;

    @BeforeEach
    public void setUp() {
        mockSpriteTypes = Mockito.mock(List.class);
        crabBoss = new BossVariety("Crab", List.of(SpriteType.BossALeft1, SpriteType.BossACore1, SpriteType.BossARight1), 5, 48);
        turtleBoss = new BossVariety("Turtle", List.of(SpriteType.BossBCore1), 8, 50);
    }

    @Test
    public void testGetName() {
        assertThat(crabBoss.getName()).isEqualTo("Crab");
        assertThat(turtleBoss.getName()).isEqualTo("Turtle");
    }

    @Test
    public void testGetSpriteTypes() {
        assertThat(crabBoss.getSpriteTypes()).containsExactly(SpriteType.BossALeft1, SpriteType.BossACore1, SpriteType.BossARight1);
        assertThat(turtleBoss.getSpriteTypes()).containsExactly(SpriteType.BossBCore1);
    }

    @Test
    public void testGetHealthPerPart() {
        assertThat(crabBoss.getHealthPerPart()).isEqualTo(5);
        assertThat(turtleBoss.getHealthPerPart()).isEqualTo(8);
    }

    @Test
    public void testGetSeparationDistance() {
        assertThat(crabBoss.getSeparationDistance()).isEqualTo(48);
        assertThat(turtleBoss.getSeparationDistance()).isEqualTo(50);
    }

    @Test
    public void testGetBossVariety() {
        BossVariety bossCrab = BossVariety.getBossVariety("Crab");
        BossVariety bossTurtle = BossVariety.getBossVariety("Turtle");
        BossVariety bossDefault = BossVariety.getBossVariety("Unknown");

        assertThat(bossCrab.getName()).isEqualTo("Crab");
        assertThat(bossTurtle.getName()).isEqualTo("Turtle");
        assertThat(bossDefault.getName()).isEqualTo("DefaultBoss");
    }

    @Test
    public void testGetBossVariety_withNullName() {
        BossVariety defaultBoss = BossVariety.getBossVariety(null);

        assertThat(defaultBoss.getName()).isEqualTo("DefaultBoss");
    }

    @Test
    public void testMockingExternalDependency() {
        when(mockSpriteTypes.get(0)).thenReturn(SpriteType.BossACore1);

        assertThat(mockSpriteTypes.get(0)).isEqualTo(SpriteType.BossACore1);
        verify(mockSpriteTypes).get(0);
    }
}
