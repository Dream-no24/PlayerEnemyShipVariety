package twoplayermode;



import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.HashSet;

import enemy.*;
import hudTeam.DrawManagerImpl;
import screen.GameScreen;
import engine.GameState;
import engine.GameSettings;
import entity.Ship;
import engine.DrawManager;
import enemy.PiercingBullet;
import entity.Bullet;
import entity.BulletPool;
import entity.EnemyShip;
import entity.Obstacle;
import inventory_develop.ItemBarrierAndHeart;
import inventory_develop.FeverTimeItem;
import sound_Operator.SoundManager;


public class TwoPlayerMode extends GameScreen {


    public static int livestwo = 3;


    public TwoPlayerMode(GameState gameState, GameSettings gameSettings, boolean bonusLife, int width, int height, int fps) {
        super(gameState, gameSettings, bonusLife, width, height, fps);



    }

    @Override
    public void initialize() {
        super.initialize(); // GameScreen의 초기화 로직 호출
        this.setTwoPlayerMode(true);
        // player2 초기화
        this.player2 = new Ship(this.width / 4, this.height - 30, Color.BLUE); // add by team HUD
    }

//    @Override
//    protected void update() {
//        super.update(); // GameScreen의 기존 기능을 사용
//
////        if (player2 != null) {
////            // Player 2 movement and shooting
////            boolean moveRight2 = inputManager.isKeyDown(KeyEvent.VK_C);
////            boolean moveLeft2 = inputManager.isKeyDown(KeyEvent.VK_Z);
////
////            if (moveRight2 && player2.getPositionX() + player2.getWidth() < width) {
////                player2.moveRight();
////            }
////            if (moveLeft2 && player2.getPositionX() > 0) {
////                player2.moveLeft();
////            }
////            if (inputManager.isKeyDown(KeyEvent.VK_X)) {
////                player2.shoot(bullets);
////            }
////
////            // Player 2 bullet collision handling
////            handleBulletCollisionsForPlayer2();
////
////            // 장애물과 아이템 상호작용 추가
////            handleObstacleCollisionsForPlayer2();
////            handleItemCollisionsForPlayer2();
////        }
//    }

    // Player 2의 총알과 충돌 처리
    public static void handleBulletCollisionsForPlayer2( Set<PiercingBullet> bullets, Ship player2) {
        if(player2==null) return;
        Set<Bullet> recyclable = new HashSet<>();
        for (Bullet bullet : bullets) {
            if (bullet.getSpeed() > 0 && checkCollision(bullet, player2)) {
                recyclable.add(bullet);
                if (!player2.isDestroyed()) {
                    player2.destroy();
                    livestwo--;
                    System.out.println("Player 2 hit, lives remaining: " + livestwo);
                    if (livestwo <= 0) {
                        player2 = null; // Player 2가 파괴된 경우
                    }
                }
            }
        }
        bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    // Player 2의 장애물과 충돌 처리
    public static void handleObstacleCollisionsForPlayer2(Set<Obstacle> obstacles, Ship player2) {
        if(player2==null) return;
        for (Obstacle obstacle : obstacles) {
            if (!obstacle.isDestroyed() && checkCollision(player2, obstacle)) {
                livestwo--;
                if (!player2.isDestroyed()) {
                    player2.destroy(); // Player 2가 파괴됨
                }
                obstacle.destroy(); // 장애물 제거
                System.out.println("Player 2 hit an obstacle, lives remaining: " + livestwo);

                if (livestwo <= 0) {
                    player2 = null; // Player 2가 파괴된 경우
                }
                break;
            }
        }
    }

    // Player 2의 아이템과 충돌 처리
    public static void handleItemCollisionsForPlayer2(Ship player2) {
        if(player2==null) return;
        for (Item item : itemManager.items) {
            if (checkCollision(item, player2)) {
                itemManager.OperateItem(item); // 아이템 효과 적용
                System.out.println("Item collected by Player 2.");
            }
        }
    }

    /**
     *
     * */
    public static int getLivestwo() {
        return livestwo;
    }

    public void setLivestwo(int livestwo) {
        this.livestwo = livestwo;
    }

}

