package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public interface GameFactory {
    RegularAsteroid createAsteroid(int x, int y, int size, int xSpeed, int ySpeed, EstrategiaMovimiento estrategia);
}
