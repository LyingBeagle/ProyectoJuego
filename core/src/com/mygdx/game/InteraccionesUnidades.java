package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface InteraccionesUnidades<T>{
    
    public void draw(SpriteBatch batch, PantallaJuego juego);
    
    boolean checkCollision(T other);
}
