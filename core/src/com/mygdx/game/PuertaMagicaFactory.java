package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class PuertaMagicaFactory implements GameFactory{
    
    public RegularAsteroid createAsteroid(int x, int y, int size, int xSpeed, int ySpeed, EstrategiaMovimiento estrategia) {
        Texture texture = new Texture(Gdx.files.internal("aGreyMedium4.png")); // Puedes ajustar esto según sea necesario
        return new RegularAsteroid(x, y, size, xSpeed, ySpeed, texture, estrategia);
    }
}
