package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class EstrategiaMovimientoZigZag implements EstrategiaMovimiento {

    private int direction = 1; // Variable para alternar la dirección del movimiento

    @Override
    public void mover(Ball2 ball) {
        ball.setY(ball.getY() - ball.getYSpeed());
        ball.setX(ball.getX() + ball.getXSpeed() * direction);

        // Cambiar dirección cuando se alcanzan los límites de la pantalla
        if (ball.getX() < 0 || ball.getX() + ball.getSize() > Gdx.graphics.getWidth()) {
            direction *= -1;
        }
    }
}

