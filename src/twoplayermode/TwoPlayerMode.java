package twoplayermode;



import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.HashSet;

import screen.GameScreen;
import engine.GameState;
import engine.GameSettings;
import entity.Ship;
import engine.DrawManager;
import Enemy.PiercingBullet;
import entity.Bullet;
import entity.BulletPool;


public class TwoPlayerMode extends GameScreen {

    private Ship player2;
    public int livestwo = 3;

    public TwoPlayerMode(GameState gameState, GameSettings gameSettings, boolean bonusLife, int width, int height, int fps) {
        super(gameState, gameSettings, bonusLife, width, height, fps);
        this.player2 = new Ship(width / 4, height -30);
        this.player2.setColor(Color.RED);// 두 번째 플레이어 초기화
        this.player2.setSpriteType(DrawManager.SpriteType.Ship);
    }

    @Override
    protected void update() {
        super.update(); // GameScreen의 기존 기능을 사용

        if (player2 != null) {
            boolean moveRight2 = inputManager.isKeyDown(KeyEvent.VK_C);
            boolean moveLeft2 = inputManager.isKeyDown(KeyEvent.VK_Z);

            if (moveRight2 && player2.getPositionX() + player2.getWidth() < width) {
                player2.moveRight();
            }
            if (moveLeft2 && player2.getPositionX() > 0) {
                player2.moveLeft();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_X)) {
                player2.shoot(bullets);
            }

            // Player 2와 적의 총알 충돌 처리
            Set<Bullet> recyclable = new HashSet<>();
            for (Bullet bullet : this.bullets) {
                if (bullet.getSpeed() > 0 && checkCollision(bullet, player2)) {
                    recyclable.add(bullet);
                    if (!player2.isDestroyed()) {
                        player2.destroy();
                        livestwo--;
                        System.out.println("Hit on Player 2, " + livestwo + " lives remaining.");

                        if (livestwo <= 0) {
                            System.out.println("Player 2 destroyed.");
                            player2 = null;  // Player 2가 파괴된 경우 처리
                        }
                    }
                }
            }
            this.bullets.removeAll(recyclable);
            BulletPool.recycle(recyclable);
        }

    }

    @Override
    public void draw() {



        if (player2 != null) {
            drawManager.drawEntity(player2, player2.getPositionX(), player2.getPositionY());
        }



        super.draw(); // GameScreen의 기존 기능을 사용
    }
}
