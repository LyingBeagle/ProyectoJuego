package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 {
    
    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private float speed = 2; // Velocidad normal de movimiento
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;

    private int dashCooldown = 100; // Cooldown en frames
    private int dashDuration = 10; // Duración del dash en frames
    private float dashSpeed = 10; // Velocidad del dash
    private int dashTimer = 0;
    private int dashCooldownTimer = 0;
    private boolean isDashing = false;

    private int lastKey = -1; // Última tecla presionada
    private int lastKeyTimer = 0;
    private int doubleTapInterval = 20; // Intervalo para detectar doble pulsación

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            if (dashCooldownTimer > 0) {
                dashCooldownTimer--;
            }
            if (dashTimer > 0) {
                dashTimer--;
                if (dashTimer == 0) {
                    isDashing = false;
                    dashCooldownTimer = dashCooldown;
                }
            }

            if (!isDashing) {
                xVel = 0;
                yVel = 0;
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    handleDoubleTap(Input.Keys.A);
                    if (!isDashing) xVel -= speed;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    handleDoubleTap(Input.Keys.D);
                    if (!isDashing) xVel += speed;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    handleDoubleTap(Input.Keys.S);
                    if (!isDashing) yVel -= speed;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    handleDoubleTap(Input.Keys.W);
                    if (!isDashing) yVel += speed;
                }
            } else {
                if (lastKey == Input.Keys.A) xVel = -dashSpeed;
                if (lastKey == Input.Keys.D) xVel = dashSpeed;
                if (lastKey == Input.Keys.S) yVel = -dashSpeed;
                if (lastKey == Input.Keys.W) yVel = dashSpeed;
            }

            // Mantener dentro de los bordes de la ventana
            if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
                xVel *= -1;
            if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
                yVel *= -1;

            spr.setPosition(x + xVel, y + yVel);
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // Disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    private void handleDoubleTap(int key) {
        if (lastKey == key && lastKeyTimer > 0 && lastKeyTimer < doubleTapInterval) {
            if (dashCooldownTimer <= 0 && dashTimer <= 0) {
                dashTimer = dashDuration;
                isDashing = true;
            }
        } else {
            lastKey = key;
            lastKeyTimer = doubleTapInterval;
        }
        lastKeyTimer--;
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // Rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            // Actualizar vidas y herir
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

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vidas2) {
        vidas = vidas2;
    }
}

