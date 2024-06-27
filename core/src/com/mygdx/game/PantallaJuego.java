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
import com.badlogic.gdx.utils.TimeUtils;
import java.util.List;

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
    
    private GameFactory factory;
    
    private Jugador jugador;
    private Enemigo enemigo;
    private ArrayList<RegularAsteroid> balls = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();
    
    private int vidasEnemigo = 5;
    
    private EstrategiaGeneracion estrategiaGeneracion;
    private long lastStrategyChangeTime;
    private long lastSpeedIncreaseTime;

    public PantallaJuego(PuertaMagica game, int ronda, int vidasJugador, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.factory = new PuertaMagicaFactory();
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
        enemigo = Enemigo.getEnemigo(Gdx.graphics.getWidth()/2-50, Gdx.graphics.getHeight()-100, 
                              new Texture(Gdx.files.internal("temp_enemy.jpg")),
                              Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                              new Texture(Gdx.files.internal("Rocket2.png")), 
                              Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        
        enemigo.reset(Gdx.graphics.getWidth()/2-50, Gdx.graphics.getHeight()-100, vidasEnemigo);
        
        enemigo.setVidas(vidasEnemigo);
        enemigo.iniciarMovimiento(200);
        
        estrategiaGeneracion = new GeneracionCirculo();
        lastStrategyChangeTime = TimeUtils.nanoTime();
        generarAsteroides();

     }
    
    private void generarAsteroides(){
        List<RegularAsteroid> nuevosAsteroides = estrategiaGeneracion.generarAsteroides(cantAsteroides, enemigo.getX(), enemigo.getY(), 
                velXAsteroides, velYAsteroides, factory);
        balls.addAll(nuevosAsteroides);
    }
    
    private void cambiarEstrategia(){
        //Cambiar la estrategia cada cierto tiempo
        Random r = new Random();
        switch (r.nextInt(2)) {
            case 0:
                estrategiaGeneracion = new GeneracionCirculo();
                break;
            case 1:
                estrategiaGeneracion = new GeneracionLinea();
                break;
        }
        generarAsteroides();
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + jugador.getVidas();
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }
    
    private void aumentarVelocidadAsteroides() {
        for (RegularAsteroid ball : balls) {
            ball.setYSpeed(ball.getYSpeed() - 1); // Incrementa la velocidad hacia abajo
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();
        
        // Cambiar la estrategia de generación cada cierto tiempo (ejemplo cada 5 segundos)
        if (TimeUtils.nanoTime() - lastStrategyChangeTime > 5_000_000_000L) {
        cambiarEstrategia();
        lastStrategyChangeTime = TimeUtils.nanoTime();
        }
        
        // Aumentar la velocidad de los asteroides cada 10 segundos
        if (TimeUtils.nanoTime() - lastSpeedIncreaseTime > 10_000_000_000L) {
            aumentarVelocidadAsteroides();
            lastSpeedIncreaseTime = TimeUtils.nanoTime();
        }
        
        if (!jugador.estaHerido()) {
            // Colisiones entre balas y asteroides y su destrucción  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls.size(); j++) {    
                    if (b.checkCollision(balls.get(j))) {          
                        explosionSound.play();
                        balls.remove(j);
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
            // Actualizar movimiento de asteroides dentro del área y eliminarlos si salen de la pantalla
            for (int i = 0; i < balls.size(); i++) {
            RegularAsteroid ball = balls.get(i);
            ball.update();
            // Eliminar asteroides que salgan de la pantalla
            if (ball.getX() < 0 || ball.getX() > Gdx.graphics.getWidth() || ball.getY() < 0 || ball.getY() > Gdx.graphics.getHeight()) {
                balls.remove(i);
                i--; // Ajustar índice después de eliminación
            }
            }
            // Colisiones entre asteroides y sus rebotes  
            for (int i = 0; i < balls.size(); i++) {
                RegularAsteroid ball1 = balls.get(i);   
                for (int j = 0; j < balls.size(); j++) {
                    RegularAsteroid ball2 = balls.get(j); 
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
        for (int i = 0; i < balls.size(); i++) {
            RegularAsteroid b = balls.get(i);
            b.draw(batch);
            // Perdió vida o game over
            if (jugador.checkCollision(b)) {
                // Asteroide se destruye con el choque             
                balls.remove(i);
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
        
        //Nuevo - Ahora juego termina cuando enemigo esta destruido
        if(enemigo.estaDestruido()){
            if(score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaVictoria(game);
            ss.resize(1200,800);
            game.setScreen(ss);
            dispose();
        }
        
        batch.end();
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