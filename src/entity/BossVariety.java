package entity;

import engine.DrawManager.SpriteType;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a variety of bosses with specific configurations.
 */
public class BossVariety {
    private final String name;
    private final List<SpriteType> spriteTypes;
    private final int healthPerPart;
    private final int separationDistance;
    private int bossWidth;
    private int bossHeight;

    private static final Map<String, BossVariety> bossVarieties = new HashMap<>();

    /**
     * Private constructor for BossVariety.
     *
     * @param name Name of the boss variety.
     * @param spriteTypes List of sprite types used for the boss.
     * @param healthPerPart Health value assigned to each boss part.
     */

    private BossVariety(String name, List<SpriteType> spriteTypes, int healthPerPart, int separationDistance) {
        this.name = name;
        this.spriteTypes = spriteTypes;
        this.healthPerPart = healthPerPart;
        this.separationDistance = separationDistance;
    }


    static {
        bossVarieties.put("Crab", new BossVariety(
                "Crab",
                List.of(SpriteType.BossALeft1, SpriteType.BossACore1, SpriteType.BossARight1), 5, 48));

        bossVarieties.put("Turtle", new BossVariety(
                "Turtle",
                List.of(SpriteType.BossBCore1),8, 50)
                );


        bossVarieties.put("DefaultBoss", new BossVariety(
                "DefaultBoss",
                List.of(SpriteType.BossALeft1, SpriteType.BossACore1, SpriteType.BossARight1),5,48));
    }



    /**
     *
     * @param name The name of the boss variety.
     * @return The BossVariety instance corresponding to the name.
     */
    public static BossVariety getBossVariety(String name) {
        return bossVarieties.getOrDefault(name, bossVarieties.get("DefaultBoss"));
    }

    public String getName() {
        return name;
    }

    public List<SpriteType> getSpriteTypes() {
        return spriteTypes;
    }

    public int getHealthPerPart() { return healthPerPart; }

    public int getSeparationDistance() { return separationDistance; }

}
