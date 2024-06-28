package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class RegularProjectile extends Projectile {
    public RegularProjectile(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, EstrategiaMovimiento estrategia) {
        super(x, y, size, xSpeed, ySpeed, tx, estrategia);
    }

    @Override
    protected void move() {
        estrategiaMovimiento.mover(this);
    }
}
