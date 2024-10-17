package HUDTeam;

// Sound Operator
import Sound_Operator.SoundManager;

public class DrawAchievementHud {
    // Sound Operator
    private static SoundManager sm;
    static int timer = 100;
    static String achievementText = null;

    public DrawAchievementHud() {}

    public static int getTimer(){return timer;}

    public static String getAchievementText(){return achievementText;}

    public static void addTimer(){timer++;}

    /**
     * Input accomplished achievement text.
     *
     * @param Text
     *            Accomplished achievement text.
     */
    public static void achieve(String Text){
        timer = 0;
        achievementText = Text;
        // Sound Operator
        sm = SoundManager.getInstance();
        sm.playES("achievement");
    }
}
