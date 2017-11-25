package ru.pfq.stargame.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.sprite.Sprite;



public class Bullet extends Sprite {

    private final Vector2 v = new Vector2();
    private final Vector2 sumV = new Vector2();
    private  Vector2 trakingV ;
    private int damage;
    private Rect worldBounds;
    private Ship owener;
    private Ship ship;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public void set(Ship owener,
                    TextureRegion region,
                    Vector2 pos0,
                    Vector2 v0,
                    float height,
                    Rect worldBounds,
                    int damage){

        this.owener = owener;
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
        this.trakingV = this.owener.v;

    }

    @Override
    public void update(float deltaTime) {
        if (trakingV != null) {
            sumV.setZero().mulAdd(trakingV, 0.2f).rotate(180).add(v);
            pos.mulAdd(sumV, deltaTime);
        } else {
            pos.mulAdd(v, deltaTime);
        }
        if(isOutside(worldBounds)){
            destroy();
        }
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Object getOwener() {
        return owener;
    }

    public void setOwener(Ship owener) {
        this.owener = owener;
    }

    @Override
    public void destroy() {
        super.destroy();
    }


}
