package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface InteraccionesEnPantalla {
    
    
    public void draw(SpriteBatch batch, PantallaJuego juego);
    
    public boolean checkCollision(Ball2 o);
    
    
}
