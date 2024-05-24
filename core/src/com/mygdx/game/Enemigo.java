package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemigo extends Unidad implements InteraccionesUnidades {

    public Enemigo(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, tx, soundChoque, txBala, soundBala);
    }

    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        if (!destruida) {
            spr.draw(batch);
        }
    }

    @Override
    public boolean checkCollision(Ball2 o) {
        // El enemigo no interactúa con los asteroides
        return false;
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
