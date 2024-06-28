package com.mygdx.game;

import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public interface EstrategiaGeneracion {
    List<RegularProjectile> generarAsteroides(int cantidad, int x, int y, int velX, int velY, GameFactory factory);
}