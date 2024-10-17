package Enemy;

import java.awt.Color;

import engine.DrawManager.SpriteType;
import entity.Entity;
import inventory_develop.ShipStatus;

public class Item extends Entity {
    private final ShipStatus shipStatus = new ShipStatus();
    private int speed;
    public Item(final int positionX, final int positionY, final int speed, final int type) {
        super(positionX, positionY, 3 * 2, 5 * 2, Color.yellow);
        this.speed = speed;
        setSprite();
    }

    public final void setSprite() {
        double rdItem = Math.random();
        shipStatus.loadProbability();
        double bombP = shipStatus.getBomb_probability();
        double PierceP = shipStatus.getPierce_probability();
        double ShieldP = shipStatus.getShield_probability();
        double HearthP = shipStatus.getHearth_probability();
        double FeverP = shipStatus.getFeverTimeProbability();

        // Import odds from properties file for easy balance patches
        if (rdItem < bombP) { // 30%
            this.spriteType = SpriteType.ItemBomb;
            this.setColor(Color.gray);
        } else if (rdItem < bombP + PierceP) { // 0% - Fixing error
            this.spriteType = SpriteType.ItemPierce;
            this.setColor(Color.white);
        } else if (rdItem < bombP + PierceP + ShieldP) { // 20%
            this.spriteType = SpriteType.ItemBarrier;
            this.setColor(Color.green);
        } else if (rdItem < bombP + PierceP + ShieldP + HearthP) { // 100%
            this.spriteType = SpriteType.ItemHeart;
            this.setColor(Color.red);
        } else if (rdItem < bombP + PierceP + ShieldP + HearthP + FeverP) {
            this.spriteType = SpriteType.ItemFeverTime;
            this.setColor(Color.yellow);
        } else {
            this.spriteType = SpriteType.ItemCoin;
            this.setColor(Color.yellow);
        }
    }

    public final void update() {
        this.positionY += this.speed;
    }
    public final void setSpeed(final int speed) {
        this.speed = speed;
    }
    public final int getSpeed() {
        return this.speed;
    }
}
