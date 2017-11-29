package ru.pfq.stargame.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.actions.ButtonActionListener;
import ru.pfq.stargame.engine.Base2DScreen;
import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.objects.CoreBackground;
import ru.pfq.stargame.objects.Button;
import ru.pfq.stargame.objects.MainShip;
import ru.pfq.stargame.objects.Ship;
import ru.pfq.stargame.pools.BulletPool;
import ru.pfq.stargame.pools.ExplosionPool;
import ru.pfq.stargame.pools.AlientShipsPool;

public class GameScreen extends Base2DScreen implements ButtonActionListener {



    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlasGame;
    private Button btnBack;
    private CoreBackground backgroundObj = CoreBackground.getInstance();
    private Music music;
    private MainShip mainShip;
    private Sound soundLaser;
    private Sound soundBallet;
    private Sound soundExplosion;
    private int frags;


    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;
    private Button btnNewGame;

    private enum State { PLAYING, GAME_OVER }

    private State state;



    private  final BulletPool bulletPool;
    private  ExplosionPool explosionPool;
    private AlientShipsPool alientShipsPool;


    public GameScreen(Game game) {

        super(game);
        this.textureAtlasGame = new TextureAtlas("gameTextures.pack");
        this.explosionPool = new ExplosionPool(textureAtlasGame);
        this.bulletPool= new BulletPool();
    }

    @Override
    public void show() {
        super.show();

        this.music           = Gdx.audio.newMusic(Gdx.files.internal("sounds/fone.mp3"));
        this.soundBallet     = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        this.soundLaser      = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.soundExplosion  = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        music.setVolume(0.2f);
        music.setLooping(true);


        this.textureAtlas = backgroundObj.getTextureAtlas();
        TextureRegion[] regionBack = new TextureRegion[2];
        regionBack[0] = textureAtlas.findRegion("replay");
        regionBack[1] = textureAtlas.findRegion("replay_press");
        this.btnBack = new Button(regionBack,-0.4f,0.4f,0.1f,this);
        mainShip = new MainShip(textureAtlasGame,bulletPool,explosionPool,worldBounds,soundLaser);
        backgroundObj.setTrakingV(mainShip.getV());
        this.alientShipsPool = new AlientShipsPool(textureAtlasGame,mainShip,soundBallet);
        music.play();
        this.messageGameOver = new MessageGameOver(textureAtlasGame);
        this.buttonNewGame = new ButtonNewGame(textureAtlasGame,this);
        startNewGame();

    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        deleteAllDestoyed();
        draw();

    }

    @Override
    protected void resize(Rect worldBounds) {
        backgroundObj.resize(worldBounds);
        mainShip.resize(worldBounds);
        alientShipsPool.resizeActiveSprites(worldBounds);
        btnBack.resize(worldBounds);
        buttonNewGame.resize(worldBounds);

    }

    public void update(float delta){

        backgroundObj.update(delta);
        switch (state) {
            case PLAYING:
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
                this.mainShip.update(delta);
                break;
            case GAME_OVER:
                break;
        }

        this.alientShipsPool.proccessWarShips(delta);
        this.bulletPool.updateActiveSprites(delta);

        this.alientShipsPool.updateActiveSprites(delta);
        this.alientShipsPool.colisionBetween(delta);

        this.explosionPool.updateActiveSprites(delta);



    }

    public void checkCollisions(){
        for (Ship ship: alientShipsPool.getActiveShips()) {
            this.bulletPool.set(ship);
        }
        alientShipsPool.checkCollisions();

    }

    public void deleteAllDestoyed(){

        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        alientShipsPool.freeAllDestroyedActiveObjects();
    }

    public void draw(){
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 0.7f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundObj.draw(batch);
        mainShip.draw(batch);
        alientShipsPool.drawActiveObjects(batch);
        bulletPool.drawActiveObjects(batch);
        explosionPool.drawActiveObjects(batch);
        btnBack.draw(batch);
        if (state == State.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        music.stop();
        textureAtlasGame.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        alientShipsPool.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {

        btnBack.touchDown(touch,pointer);
        mainShip.touchDown(touch,pointer);
        buttonNewGame.touchDown(touch,pointer);


    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {

        btnBack.touchUp(touch,pointer);
        mainShip.touchUp(touch,pointer);
        buttonNewGame.touchUp(touch,pointer);
    }

    @Override
    public void actionPerformed(Object src) {
        System.out.println(src);
        if(src == btnBack){
            game.setScreen(new MenuScreen(game));
        }else if(src == buttonNewGame){
            startNewGame();
        }else{
            throw new RuntimeException("Unknown src = " + src);
        }
    }



    @Override
    public boolean keyDown(int keycode) {
        return mainShip.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return mainShip.keyUp(keycode);
    }

    private void startNewGame() {
        System.out.println("startNewGame");
        state = State.PLAYING;
        frags = 0;
        mainShip.setToNewGame();
        bulletPool.freeAllActiveObjects();
        alientShipsPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
    }

}
