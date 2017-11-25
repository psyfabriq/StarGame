package ru.pfq.stargame.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.actions.ButtonActionListener;
import ru.pfq.stargame.engine.Base2DScreen;
import ru.pfq.stargame.engine.Sprite2DTexture;
import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.objects.Background;
import ru.pfq.stargame.objects.BackgroundForAllScreens;
import ru.pfq.stargame.objects.Button;
import ru.pfq.stargame.objects.Explosion;
import ru.pfq.stargame.objects.MainShip;
import ru.pfq.stargame.objects.Ship;
import ru.pfq.stargame.objects.WarShip;
import ru.pfq.stargame.pools.BulletPool;
import ru.pfq.stargame.pools.ExplosionPool;
import ru.pfq.stargame.pools.WarShipsPool;

public class GameScreen extends Base2DScreen implements ButtonActionListener {



    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlasGame;
    private Button btnBack;
    private BackgroundForAllScreens backgroundObj = BackgroundForAllScreens.getInstance();
    private Music music;
    private MainShip mainShip;
    private Sound soundLaser;
    private Sound soundBallet;
    private Sound soundExplosion;
    private BitmapFont font;
    private int frags;


    private MessageGameOver messageGameOver;

    private enum State { PLAYING, GAME_OVER }

    private State state;

    CharSequence str = "Hello World!";


    private  final BulletPool bulletPool;
    private  ExplosionPool explosionPool;
    private  WarShipsPool  warShipsPool;


    public GameScreen(Game game) {

        super(game);
        this.textureAtlasGame = new TextureAtlas("mainAtlas.tpack");
        this.explosionPool = new ExplosionPool(textureAtlasGame);
        this.bulletPool= new BulletPool();
        this.font = new BitmapFont();
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
        regionBack[0] = textureAtlas.findRegion("menuExit");
        regionBack[1] = textureAtlas.findRegion("menuExit_press");
        this.btnBack = new Button(regionBack,-0.4f,0.4f,0.15f,this);
        mainShip = new MainShip(textureAtlasGame,bulletPool,explosionPool,worldBounds,soundLaser);
        backgroundObj.setTrakingV(mainShip.getV());
        this.warShipsPool  = new WarShipsPool(textureAtlasGame,mainShip,soundBallet);
        music.play();
        this.messageGameOver = new MessageGameOver(textureAtlasGame);
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
        warShipsPool.resizeActiveSprites(worldBounds);
        btnBack.resize(worldBounds);
    }

    public void update(float delta){

        backgroundObj.update(delta);
        switch (state) {
            case PLAYING:
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
                this.warShipsPool.proccessWarShips(delta);
                this.bulletPool.updateActiveSprites(delta);
                this.mainShip.update(delta);
                this.warShipsPool.updateActiveSprites(delta);
                this.warShipsPool.colisionBetween(delta);
                break;
            case GAME_OVER:
                break;
        }

        this.explosionPool.updateActiveSprites(delta);



    }

    public void checkCollisions(){
        for (Ship ship: warShipsPool.getActiveShips()) {
            this.bulletPool.set(ship);
        }
        warShipsPool.checkCollisions();

    }

    public void deleteAllDestoyed(){

        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        warShipsPool.freeAllDestroyedActiveObjects();
    }

    public void draw(){
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 0.7f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundObj.draw(batch);
        mainShip.draw(batch);
        warShipsPool.drawActiveObjects(batch);
        bulletPool.drawActiveObjects(batch);
        explosionPool.drawActiveObjects(batch);
        font.draw(batch, str, 0.0f, 0.0f);
        btnBack.draw(batch);
        if (state == State.GAME_OVER) {
            messageGameOver.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        music.stop();
        textureAtlasGame.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        warShipsPool.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {

        btnBack.touchDown(touch,pointer);
        mainShip.touchDown(touch,pointer);


    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {

        btnBack.touchUp(touch,pointer);
        mainShip.touchUp(touch,pointer);
    }

    @Override
    public void actionPerformed(Object src) {
        if(src == btnBack){
            game.setScreen(new MenuScreen(game));
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
        state = State.PLAYING;
        frags = 0;

        mainShip.setToNewGame();
        bulletPool.freeAllActiveObjects();
        warShipsPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
    }

}
