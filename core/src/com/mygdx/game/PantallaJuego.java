package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego implements Screen {

    private PuertaMagica game;
    private OrthographicCamera camera;    
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides; 
    private int velYAsteroides; 
    private int cantAsteroides;
    
    private Jugador jugador;
    private Enemigo enemigo;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(PuertaMagica game, int ronda, int vidasJugador, int vidasEnemigo, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        
        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        //inicializar assets; musica de fondo y efectos de sonido
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1,0f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.1f);
        gameMusic.play();
        
        // cargar imagen de la nave, 64x64   
        //Se crea un unico Jugador con Singleton y se le da sus caracteristicas
        jugador =  Jugador.getJugador(Gdx.graphics.getWidth()/2-50, 30, new Texture(Gdx.files.internal("MainShip3.png")),
                        Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                        new Texture(Gdx.files.internal("Rocket2.png")), 
                        Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        
        //Se resetea los datos del jugado r para que se mantenga constante cuando muera y continue de ronda
        jugador.reset(Gdx.graphics.getWidth()/2-50, 30, vidasJugador);

        // Crear enemigo
        enemigo = new Enemigo(Gdx.graphics.getWidth()/2-50, Gdx.graphics.getHeight()-100, 
                              new Texture(Gdx.files.internal("temp_enemy.jpg")),
                              Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                              new Texture(Gdx.files.internal("Rocket2.png")), 
                              Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        
        enemigo.setVidas(vidasEnemigo);
        enemigo.iniciarMovimiento(200);

        // Crear asteroides
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            EstrategiaMovimiento estrategia;
             switch (r.nextInt(2)) { // Elige un patrón de movimiento aleatorio
        case 0:
            estrategia = new EstrategiaMovimientoZigZag();
            break;
        case 1:
            estrategia = new EstrategiaMovimientoAbajo();
            break;
        default:
            estrategia = new EstrategiaMovimientoAbajo();
            break;
    }
            Ball2 bb = new Ball2(r.nextInt((int)Gdx.graphics.getWidth()),
                    50 + r.nextInt((int)Gdx.graphics.getHeight() - 50),
                    20 + r.nextInt(10), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4), 
                    new Texture(Gdx.files.internal("aGreyMedium4.png")),estrategia);     
            balls1.add(bb);
            balls2.add(bb);
        }
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + jugador.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();
        if (!jugador.estaHerido()) {
            // Colisiones entre balas y asteroides y su destrucción  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {    
                    if (b.checkCollision(balls1.get(j))) {          
                        explosionSound.play();
                        balls1.remove(j);
                        balls2.remove(j);
                        j--;
                        score += 10;
                    }      
                }
                // Colisión con enemigo
                if (enemigo.checkCollision(b)) {
                    balas.remove(b);
                    i--;
                    score += 50; // Puntaje por destruir al enemigo
                }
                if (b.isDestroyed()) {
                    balas.remove(b);
                    i--; // Para no saltarse 1 tras eliminar del arraylist
                }
            }
            // Actualizar movimiento de asteroides dentro del área
            for (Ball2 ball : balls1) {
                ball.update();
            }
            // Colisiones entre asteroides y sus rebotes  
            for (int i = 0; i < balls1.size(); i++) {
                Ball2 ball1 = balls1.get(i);   
                for (int j = 0; j < balls2.size(); j++) {
                    Ball2 ball2 = balls2.get(j); 
                    if (i < j) {
                        ball1.checkCollision(ball2);
                    }
                }
            } 
        }
        // Dibujar balas
        for (Bullet b : balas) {       
            b.draw(batch);
        }
        jugador.draw(batch, this);
        enemigo.draw(batch, this);
        // Dibujar asteroides y manejar colisión con nave
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            b.draw(batch);
            // Perdió vida o game over
            if (jugador.checkCollision(b)) {
                // Asteroide se destruye con el choque             
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }      
        }
        if (jugador.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
            
        }
        batch.end();
        // Nivel completado
        if (enemigo.estaDestruido()) {
            avanzarARondaSiguiente();
        }
    }
    
    private void avanzarARondaSiguiente(){
        Screen ss = new PantallaJuego(game, ronda + 1, jugador.getVidas(), 3, score, 
                    velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        gameMusic.play();
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
        explosionSound.dispose();
        gameMusic.dispose();
    }
}