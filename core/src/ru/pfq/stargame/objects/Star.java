package ru.pfq.stargame.objects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Rnd;
import ru.pfq.stargame.sprite.Sprite;

public class Star extends Sprite {

    private final Vector2 v = new Vector2();
    private  Vector2 trakingV ;
    private final Vector2 sumV = new Vector2();
    private Rect worldBounds;
    private int countRegions = 0;
    private int rndBlink = 0;
    private int i;


    public Star(TextureRegion[] region, float vx, float vy, float width ) {
        super(region);
        v.set(vx, vy);
        setWithProportion(width);
        int b;
        countRegions = region.length-1;
        rndBlink = Rnd.nextint(10,250);
        frame = Rnd.nextint(0,countRegions);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(this.worldBounds.getLeft(), this.worldBounds.getRight());
        float posY = Rnd.nextFloat(this.worldBounds.getBottom(), this.worldBounds.getTop());
        pos.set(posX, posY);
    }

    @Override
    public void update(float deltaTime) {
        if(trakingV != null) {
            sumV.setZero().mulAdd(trakingV, 0.2f).rotate(180).add(v);
            pos.mulAdd(sumV, deltaTime);
        }else{pos.mulAdd(v, deltaTime);}
        checkAndHandleBounds();
    }

    /**
     * Проверка границ экрана для того чтобы вернуть звезду
     */
    protected void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
        if(rndBlink == i){
            frame = Rnd.nextint(0,countRegions);
            i = 0;
        }else{ ++i;}
    }

    public void setTrakingV(Vector2 trakingV) {
        this.trakingV = trakingV;
    }

    public void destroy(){
        this.trakingV = null;
    }
}
