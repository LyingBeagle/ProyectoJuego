package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Jugador extends Unidad implements InteraccionesUnidades<RegularProjectile> {
    
    private static Jugador jugador_unico = null;
    
    private float xVel = 0;
    private float yVel = 0;
    private final float speed = 350; // Velocidad constante
    private final float dashSpeed = 5000; // Velocidad de dash
    private final float dashCooldown = 1f; // Tiempo de enfriamiento entre dashes en segundos
    private float timeSinceLastDash = 0f;

    private Jugador(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, tx, soundChoque, txBala, soundBala);
    }
    
    public static synchronized Jugador getJugador(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala){
        
        if(jugador_unico == null){
            jugador_unico = new Jugador(x, y, tx, soundChoque, txBala, soundBala);
        }
        
        return jugador_unico;
    }
    
    public static synchronized void resetJugador() {
        jugador_unico = null;
    }
    
    public void reset(int x, int y, int vidas){
        spr.setPosition(x, y);
        this.vidas = vidas;
        this.herido = false;
        this.destruida = false;
    }
    
    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            // Resetear velocidades
            xVel = 0;
            yVel = 0;

            // que se mueva con teclado
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) xVel = -speed;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) xVel = speed;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) yVel = -speed;
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) yVel = speed;

            // Manejar dash
            timeSinceLastDash += Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && timeSinceLastDash >= dashCooldown) {
                xVel *= dashSpeed / speed;
                yVel *= dashSpeed / speed;
                timeSinceLastDash = 0f;
            }

            // Actualizar la posiciÃ³n de la nave
            float deltaTime = Gdx.graphics.getDeltaTime();
            float newX = x + xVel * deltaTime;
            float newY = y + yVel * deltaTime;

            // que se mantenga dentro de los bordes de la ventana
            if (newX < 0) {
                newX = 0;
            } else if (newX + spr.getWidth() > Gdx.graphics.getWidth()) {
                newX = Gdx.graphics.getWidth() - spr.getWidth();
            }

            if (newY < 0) {
                newY = 0;
            } else if (newY + spr.getHeight() > Gdx.graphics.getHeight()) {
                newY = Gdx.graphics.getHeight() - spr.getHeight();
            }

            spr.setPosition(newX, newY);
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    public boolean checkCollision(RegularProjectile b) {
        
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getYSpeed() / 2;
            if (b.getYSpeed() == 0) b.setYSpeed(b.getYSpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setYSpeed(-b.getYSpeed());

            // actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0)
                destruida = true;
            return true;
        }
        return false;
    }
}