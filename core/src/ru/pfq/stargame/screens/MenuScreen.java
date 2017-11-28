package ru.pfq.stargame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.actions.ButtonActionListener;
import ru.pfq.stargame.engine.Base2DScreen;
import ru.pfq.stargame.engine.Sprite2DTexture;
import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Rnd;
import ru.pfq.stargame.objects.Background;
import ru.pfq.stargame.objects.BackgroundForAllScreens;
import ru.pfq.stargame.objects.Button;
import ru.pfq.stargame.objects.Star;



public class MenuScreen extends Base2DScreen implements ButtonActionListener {

    private Button btnPlay;
    private Button btnExit;
    private TextureAtlas textureAtlas;

    BackgroundForAllScreens backgroundObj = BackgroundForAllScreens.getInstance();

    public MenuScreen(Game game) {
        super(game);


        textureAtlas = backgroundObj.getTextureAtlas();

        TextureRegion[] regionPlay = new TextureRegion[2];
        TextureRegion[] regionExit = new TextureRegion[2];
        regionPlay[0] = textureAtlas.findRegion("play");
        regionPlay[1] = textureAtlas.findRegion("play_press");
        regionExit[0] = textureAtlas.findRegion("cancel");
        regionExit[1] = textureAtlas.findRegion("cancel_press");

        btnPlay = new Button(regionPlay,-0.4f,-0.34f,0.08f,this);
        btnExit = new Button(regionExit,0.4f,-0.34f,0.08f,this);
    }

    @Override
    public void show () {
        super.show();

    }

    @Override
    public void render (float delta) {
        backgroundObj.update(delta);
        draw();
    }
    public void draw() {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 0.7f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundObj.draw(batch);
        btnPlay.draw(batch);
        btnExit.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        super.dispose();
    }

    @Override
    protected void resize(Rect worldBounds) {
        backgroundObj.resize(worldBounds);
        btnPlay.resize(worldBounds);
        btnExit.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        btnExit.touchDown(touch,pointer);
        btnPlay.touchDown(touch,pointer);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        btnExit.touchUp(touch,pointer);
        btnPlay.touchUp(touch,pointer);
    }

    @Override
    public void actionPerformed(Object src) {

        if(src == btnExit){
            backgroundObj.dispose();
            Gdx.app.exit();
        }else if (src == btnPlay) {
            game.setScreen(new GameScreen(game));
        }else{
            throw new RuntimeException("Unknown src = " + src);
        }
    }
}

