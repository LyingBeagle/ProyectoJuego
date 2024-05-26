package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Enemigo extends Unidad implements Interacciones<Bullet> {

    public Enemigo(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, tx, soundChoque, txBala, soundBala);
    }

    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        
        float x = spr.getX();
        float y = spr.getY();
        
        if(!herido){
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
        
    }

    public boolean checkCollision(Bullet b) {
        
        if (!destruida && b.getSpr().getBoundingRectangle().overlaps(spr.getBoundingRectangle())) {
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) {
                destruida = true;
            }
            return true;
        }
        return false;
    }
}
