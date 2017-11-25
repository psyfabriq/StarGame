package ru.pfq.stargame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.pfq.stargame.engine.Sprite2DTexture;
import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Rnd;

public class BackgroundForAllScreens {
    private static BackgroundForAllScreens instance;
    public static final int STAR_COUNT = 500;

    private int start_count = 200;
    private Sprite2DTexture textureBackground;
    private TextureAtlas textureAtlas;
    private Background background;
    private TextureRegion[] regionStar;
    private List<Star> stars = new ArrayList<Star>();

    private BackgroundForAllScreens() {
    }

    public static BackgroundForAllScreens getInstance(){
        if(instance == null) instance = new BackgroundForAllScreens();
        return instance;
    }

    public void setTextureBackground(String nameBacground){
        textureBackground = new Sprite2DTexture(nameBacground);
        background = new Background(new TextureRegion(textureBackground));
    }

    public void setTextureAtlas(String nameTextureAtlas){
        textureAtlas = new TextureAtlas(nameTextureAtlas);
    }

    public void setTextureAtlas(TextureAtlas textureAtlas){
        this.textureAtlas = textureAtlas;
    }

    public TextureAtlas getTextureAtlas(){
        return textureAtlas;
    }

    public void setAtlasRegions(String... regions){
        regionStar = new TextureRegion[regions.length];
        for (int i = 0; i<regions.length;i++) {
            regionStar[i]=textureAtlas.findRegion(regions[i]);
        }

    }
    public void initStars(){
        for(int i = 0;i<STAR_COUNT;i++) {
            stars.add(new Star(regionStar, Rnd.nextFloat(-0.01f, 0.01f), Rnd.nextFloat(-0.2f, -0.1f), Rnd.nextFloat(-0.01f, 0.01f)));

        }
    }

    public void initStars(int colStars){
        start_count = colStars;
        initStars();
    }


    public void dispose(){
        textureBackground.dispose();
        textureAtlas.dispose();
        Iterator<Star> starIterator = stars.iterator();
    }

    public void resize(Rect worldBounds){
        background.resize(worldBounds);
        Iterator<Star> starIterator = stars.iterator();
        while (starIterator.hasNext()) {
            starIterator.next().resize(worldBounds);
        }
    }

    public void draw(SpriteBatch batch) {
        background.draw(batch);
        Iterator<Star> starIterator = stars.iterator();
        while (starIterator.hasNext()) {
            starIterator.next().draw(batch);
        }
    }

    public void update(float delta){
        Iterator<Star> starIterator = stars.iterator();
        while (starIterator.hasNext()) {
            starIterator.next().update(delta);
        }
    }

    public void setTrakingV(Vector2 trakingV) {
        Iterator<Star> starIterator = stars.iterator();
        while (starIterator.hasNext()) {
            starIterator.next().setTrakingV(trakingV);
        }
    }

    public void destroyTrakingV(){
        Iterator<Star> starIterator = stars.iterator();
        while (starIterator.hasNext()) {
            starIterator.next().destroy();
        }
    }
}
