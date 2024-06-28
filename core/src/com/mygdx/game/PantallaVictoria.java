package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaVictoria implements Screen{
    
    private PuertaMagica game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture victoriaTexture;
    
    public PantallaVictoria(PuertaMagica game){
        this.game = game;
        
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        
        victoriaTexture = new Texture(Gdx.files.internal("victoria.png"));
    }
    
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.draw(victoriaTexture, 0, 0);
        batch.end();
        
        if (Gdx.input.isTouched()|| Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PantallaMenu(game));
            dispose();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        //victoriaTexture.dispose();
    }
}
