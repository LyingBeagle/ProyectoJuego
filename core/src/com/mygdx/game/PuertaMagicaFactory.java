package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class PuertaMagicaFactory implements GameFactory{
    
    public RegularProjectile createProjectile(int x, int y, int size, int xSpeed, int ySpeed, EstrategiaMovimiento estrategia) {
        Texture texture = new Texture(Gdx.files.internal("Sprite-0009.png"));
        return new RegularProjectile(x, y, size, xSpeed, ySpeed, texture, estrategia);
    }
}
