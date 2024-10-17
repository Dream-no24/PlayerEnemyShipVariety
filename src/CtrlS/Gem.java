package CtrlS;

import engine.DrawManager;
import entity.Entity;

import java.awt.*;

public class Gem extends Entity {
    DrawManager.SpriteType layeredSprite;

    public Gem() {
        super(0, 0, 7 * 2, 6 * 2, Color.cyan);
        this.spriteType = DrawManager.SpriteType.Gem;
    }
}