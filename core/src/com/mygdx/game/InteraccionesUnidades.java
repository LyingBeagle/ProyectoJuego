package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface InteraccionesUnidades {
    
    
    public void draw(SpriteBatch batch, PantallaJuego juego);
    
    public boolean checkCollision(Ball2 o);
    
}
