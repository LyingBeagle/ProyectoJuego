package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Unidad {
    
    protected boolean destruida = false;
    protected int vidas = 3;
    protected Sprite spr;
    protected Sound sonidoHerido;
    protected Sound soundBala;
    protected Texture txBala;
    protected boolean herido = false;
    protected int tiempoHeridoMax = 50;
    protected int tiempoHerido;
    
    public Unidad(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala){
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.spr.setBounds(x, y, 45, 45);
    }
    
    
    
}
