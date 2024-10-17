package HUDTeam;

import engine.DrawManager;
import screen.Screen;
import entity.Entity;
import java.awt.Color;

public class DrawManagerImpl extends DrawManager {

    // Add method
    public static void drawLevel(final Screen screen, final int level){
        String levelText = "Level: " + level;

        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);

        int xPosition = screen.getWidth() / 2;
        int yPosition = 25;

        backBufferGraphics.drawString(levelText, xPosition - fontRegularMetrics.stringWidth(levelText) / 2, yPosition); // edit by jesung ko - TeamHUD
    } // Lee Hyun Woo - level

    public static void drawBulletSpeed(final Screen screen, final int bulletSpeed) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        String bulletSpeedText = String.format("BS : %d px/f ", bulletSpeed);
        backBufferGraphics.drawString(bulletSpeedText, 10, screen.getHeight() - 15);
    }

    public static void drawSpeed(final Screen screen, final int speed) {
        String speedString = "MS : " + speed;
        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.drawString(speedString, 10, screen.getHeight() - 35);
    }

    public void drawLivesWithHeart(final Screen screen, final int lives) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);

        Entity heart = new Entity(0, 0, 13 * 2, 8 * 2, Color.RED);
        heart.setSpriteType(SpriteType.Heart);

        for (int i = 0; i < lives; i++) {
            drawEntity(heart, 20 + 30 * i, 10);
        }
    } // Saeum Jung - heart graphic

    /**
     * Draws current score on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param playTime
     *            Current playtime.
     *
     * by Soomin Lee - TeamHUD
     */
    public static void drawTime(final Screen screen, final int playTime) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        int playTimeMinutes = playTime / 60;
        int playTimeSeconds = playTime % 60;
        String playTimeString = String.format("%d"+"m "+"%d"+"s", playTimeMinutes, playTimeSeconds);
        int xPosition = (screen.getWidth() * 4) / 6; // position 4/6
        backBufferGraphics.drawString(playTimeString, xPosition - fontRegularMetrics.stringWidth(playTimeString) / 2, 25); // edit by jesung ko - TeamHUD
    }

    /**
     * Draws the player's score on the screen.
     * The score is displayed at the 2/6 position of the screen width.
     *
     * @param screen
     *          The screen to draw on.
     * @param score
     *          The current score to display.
     * by jesung Ko - TeamHUD
     */
    public static void drawScore2(final Screen screen, final int score) {
        String scoreString = "Score: " + score;
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        int xPosition = (screen.getWidth() * 2) / 6; // 2/6 지점
        backBufferGraphics.drawString(scoreString, xPosition - fontRegularMetrics.stringWidth(scoreString) / 2, 25);
    }
    /**
     * Show accomplished achievement
     *
     * @param screen
     *            Screen to draw on.
     * @param achievementText
     *            Accomplished achievement text.
     *
     * by Jo Minseo - HUD team
     */
    public static void drawAchievement(final Screen screen, String achievementText) {
        int width = screen.getWidth() / 4;
        int height = screen.getHeight() / 16;
        int fontWidth;

        backBufferGraphics.setColor(Color.white);
        backBufferGraphics.drawRect(screen.getWidth() - width - 3, screen.getHeight() - height - 18, width, height);

        //Modify the location of the window and the text - Jo Minseo/HUD team
        backBufferGraphics.setColor(Color.white);
        backBufferGraphics.setFont(fontRegular);
        if(achievementText.length() < 14){
            fontWidth = fontRegularMetrics.stringWidth(achievementText);
            backBufferGraphics.drawString(achievementText, screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 35);
        }
        else if(achievementText.length() < 27){
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(0,13));
            backBufferGraphics.drawString(achievementText.substring(0,13), screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 43);
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(13));
            backBufferGraphics.drawString(achievementText.substring(13), screen.getWidth() - width/2 - fontWidth / 2, screen.getHeight() - 25);
        }
        else{
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(0,13));
            backBufferGraphics.drawString(achievementText.substring(0,13), screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 43);
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(13,25)+"...");
            backBufferGraphics.drawString(achievementText.substring(13,25)+"...", screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 25);
        }
    }

    /**
     * Draw remaining enemies
     *
     * @param screen
     *            Screen to draw on.
     * @param remainingEnemies
     *            remaining enemies count.
     */
    public static void drawRemainingEnemies(final Screen screen, final int remainingEnemies) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        String remainingEnemiesString = "Enemies: " + remainingEnemies;
        int textWidth = fontRegularMetrics.stringWidth(remainingEnemiesString);

        int x = (screen.getWidth() - textWidth) / 2;
        int y = screen.getHeight() - 25;

        backBufferGraphics.drawString(remainingEnemiesString, x, y);
    } // by SeungYun

    /**
     * Draws current score on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param positionY
     *            Position to display separator line.
     *
     * by Ko jesung - TeamHUD
     */
    public static void drawSeparatorLine(final Screen screen, final int positionY) {
        backBufferGraphics.setColor(Color.GREEN);
        backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
        backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
                positionY + 1);
    }
}
