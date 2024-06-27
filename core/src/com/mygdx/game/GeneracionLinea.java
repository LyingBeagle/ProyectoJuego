package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public class GeneracionLinea implements EstrategiaGeneracion{
    
    @Override
    public List<RegularAsteroid> generarAsteroides(int cantidad, int x, int y, int velX, int velY, GameFactory factory){
        List<RegularAsteroid> asteroides = new ArrayList<>();
        
        int offsetY = 50; //Separacion de asteriodes
        for(int i=0; i < cantidad; i++){
            int newY = y - i * offsetY;
            asteroides.add(factory.createAsteroid(x, newY, 20, 0, -Math.abs(velY), new MovimientoVertical()));
        }
        
        return asteroides;
    }
    
}
