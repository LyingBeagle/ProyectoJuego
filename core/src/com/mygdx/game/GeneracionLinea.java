package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public class GeneracionLinea implements EstrategiaGeneracion{
    
    @Override
    public List<Ball2> generarAsteroides(int cantidad, int x, int y, Texture tx, int velocidadX, int velocidadY){
        List<Ball2> asteroides = new ArrayList<>();
        
        int offsetY = 50; //Separacion de asteriodes
        for(int i=0; i < cantidad; i++){
            int newY = y - i * offsetY;
            asteroides.add(new Ball2(x, newY, 20, 0, -Math.abs(velocidadY), tx, new MovimientoVertical()));
        }
        
        return asteroides;
    }
    
}
