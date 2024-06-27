package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import java.util.List;
import java.util.ArrayList;

public class GeneracionCirculo implements EstrategiaGeneracion{
    
    @Override
    public List<RegularAsteroid> generarAsteroides(int cantidad, int x, int y, int velX, int velY, GameFactory factory){
        List<RegularAsteroid> asteroides = new ArrayList<>();
        int radio = 100;
        for(int i = 0; i < cantidad ; i++){
            double angle = 2 * Math.PI * i / cantidad;
            int newX = x + (int) (radio * Math.cos(angle));
            int newY = y + (int) (radio * Math.sin(angle));
            asteroides.add(factory.createAsteroid(newX, newY - 100, 20, 0, -Math.abs(velY), new MovimientoVertical()));
        }
        
        return asteroides;
    }
}
