package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Asteroid {
    protected int x;
    protected int y;
    protected int xSpeed;
    protected int ySpeed;
    protected Sprite spr;
    protected EstrategiaMovimiento estrategiaMovimiento;
    
    public Asteroid(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, EstrategiaMovimiento estrategia) {
        spr = new Sprite(tx);
        this.x = x; 

        // Validar que el borde de la esfera no quede fuera
        if (x - size < 0) this.x = x + size;
        if (x + size > Gdx.graphics.getWidth()) this.x = x - size;
        this.y = y;
        if (y - size < 0) this.y = y + size;
        if (y + size > Gdx.graphics.getHeight()) this.y = y - size;

        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setYSpeed(ySpeed);

        this.estrategiaMovimiento = estrategia;
    }
    
    //Template
    public final void update(){
        move();
        x += getXSpeed();
        y += getYSpeed();
        
        spr.setPosition(x, y);

        checkBounds();
    }
    
    protected abstract void move();
    
    protected void checkBounds() {
        if (x < 0 || x + spr.getWidth() > Gdx.graphics.getWidth() || y < 0 || y + spr.getHeight() > Gdx.graphics.getHeight()) {
            // Marca el asteroide como fuera de la pantalla estableciendo las velocidades a 0
            setXSpeed(0);
            setYSpeed(0);
        }
    }
    
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollisionBall(Asteroid b2) {
        if(spr.getBoundingRectangle().overlaps(b2.spr.getBoundingRectangle())){
        	// rebote
            if (getXSpeed() ==0) setXSpeed(getXSpeed() + b2.getXSpeed()/2);
            if (b2.getXSpeed() ==0) b2.setXSpeed(b2.getXSpeed() + getXSpeed()/2);
        	setXSpeed(- getXSpeed());
            b2.setXSpeed(-b2.getXSpeed());
            
            if (getYSpeed() ==0) setYSpeed(getYSpeed() + b2.getYSpeed()/2);
            if (b2.getYSpeed() ==0) b2.setYSpeed(b2.getYSpeed() + getYSpeed()/2);
            setYSpeed(- getYSpeed());
            b2.setYSpeed(- b2.getYSpeed()); 
        }
    }

    public boolean checkCollision(Asteroid o) {
        return true;
    }

    public int getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getSize() {
        return spr.getWidth();
    }
}
