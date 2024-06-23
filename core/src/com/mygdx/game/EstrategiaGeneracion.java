package com.mygdx.game;

import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public interface EstrategiaGeneracion {
    List<Ball2> generarAsteroides(int cantidad, int x, int y, Texture tx, int velocidadX, int velocidadY);
}
