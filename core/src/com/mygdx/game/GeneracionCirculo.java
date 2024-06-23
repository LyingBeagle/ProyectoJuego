package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import java.util.List;
import java.util.ArrayList;

public class GeneracionCirculo implements EstrategiaGeneracion{
    
    @Override
    public List<Ball2> generarAsteroides(int cantidad, int x, int y, Texture tx, int velocidadX, int velocidadY){
        List<Ball2> asteroides = new ArrayList<>();
        int radio = 100;
        for(int i = 0; i < cantidad ; i++){
            double angle = 2 * Math.PI * i / cantidad;
            int newX = x + (int) (radio * Math.cos(angle));
            int newY = y + (int) (radio * Math.sin(angle));
            asteroides.add(new Ball2(newX, newY, 20, 0, -Math.abs(velocidadY), tx, new MovimientoVertical()));
        }
        
        return asteroides;
    }
}
