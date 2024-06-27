package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Enemigo extends Unidad implements InteraccionesUnidades<Bullet> {

    private float velocidadHorizontal; // Velocidad horizontal del enemigo
    private boolean moviendoseDerecha; // Indica si el enemigo se está moviendo hacia la derecha
    
    private static Enemigo enemigo_unico = null;
    
    private Enemigo(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, tx, soundChoque, txBala, soundBala);
    }
    
    public static synchronized Enemigo getEnemigo(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala){
        
        if(enemigo_unico == null){
            enemigo_unico = new Enemigo(x, y, tx, soundChoque, txBala, soundBala);
        }
        
        return enemigo_unico;
    }
    
    public void reset(int x, int y, int vidas){
        spr.setPosition(x, y);
        this.vidas = vidas;
        this.herido = false;
        this.destruida = false;
    }
    
    public void iniciarMovimiento(float velocidadHorizontal) {
        this.velocidadHorizontal = velocidadHorizontal;
        this.moviendoseDerecha = true; // Empezamos moviéndonos hacia la derecha
    }
        
    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
     float x = spr.getX();
     
    if (!herido) {
        
        float deltaTime = Gdx.graphics.getDeltaTime();
        float newX = x + velocidadHorizontal * deltaTime;
        
        // que se mantenga dentro de los bordes de la ventana
            if (newX < 0) {
                velocidadHorizontal = Math.abs(velocidadHorizontal); // Hacer la velocidad positiva
                newX = 0;
            } else if (newX + spr.getWidth() > Gdx.graphics.getWidth()) {
                velocidadHorizontal = -Math.abs(velocidadHorizontal); // Hacer la velocidad negativa
                newX = Gdx.graphics.getWidth() - spr.getWidth();
            }
        spr.setPosition(newX, spr.getY());
        spr.draw(batch);
    } else {
        float posX = spr.getX(); // Guardar la posición original en X
        spr.setX(posX + MathUtils.random(-2, 2)); // Modificar la posición en X
        spr.draw(batch);
        spr.setX(posX); // Restaurar la posición original en X
        tiempoHerido--;
        if (tiempoHerido <= 0) herido = false;
    }
}

    @Override
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
