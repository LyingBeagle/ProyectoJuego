package com.mygdx.game;

public class EstrategiaMovimientoAbajo implements EstrategiaMovimiento {

    @Override
    public void mover(Ball2 ball) {
        ball.setY(ball.getY() - ball.getYSpeed());
    }
}
