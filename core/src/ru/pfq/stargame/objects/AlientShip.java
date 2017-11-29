package ru.pfq.stargame.objects;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Rnd;
import ru.pfq.stargame.pools.BulletPool;
import ru.pfq.stargame.pools.ExplosionPool;
import ru.pfq.stargame.sprite.Sprite;

public class AlientShip extends Ship {


    public  static  final  float TOP_MARGIN    = 0.2f;
    private final float poin_to_speed;
    private final float boss_poin_speed;
    private final Vector2 sumV = new Vector2();
    private  Vector2 trakingV ;
    public boolean isBoss = false;
    private final MainShip mainShip;



    public AlientShip(float poin_to_speed, float boss_poin_speed, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, MainShip mainShip) {
        super(bulletPool,explosionPool,worldBounds);
        v.set(v0);
        this.poin_to_speed = poin_to_speed;
        this.boss_poin_speed = boss_poin_speed;
        this.mainShip = mainShip;

    }

    public void set(
        TextureRegion[] regions,
        Vector2 v0,
        TextureRegion bulletRegion,
        float bulletHeight,
        float bulletVY,
        int bulletDamage,
        float reloadInterval,
        Sound bulletSound,
        float height,
        int hp,
        boolean isBoss

    ){
        this.regions        = regions;
        this.v0.set(v0);
        this.bulletRegion   = bulletRegion;
        this.bulletHeight   = bulletHeight;
        this.bulletV.set(0f,bulletVY);
        this.bulletDamage   = bulletDamage;
        this.reloadInterval = reloadInterval;
        super.bulletSound    = bulletSound;

        this.hp             = hp;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        v.set(v0);
        this.isBoss = isBoss;




    }

    @Override
    public void resize(Rect worldBounds) {
      //  super.resize(worldBounds);
        setTop(worldBounds.getTop() + TOP_MARGIN);
    }

    @Override
    public void update(float deltaTime) {
        if(isOutside(worldBounds,NON_BOTTOM,NON_LEFT,NON_RIGHT)){
            destroy();
        }else {
            correct();
            if (trakingV != null) {
                sumV.setZero().mulAdd(trakingV, 0.2f).rotate(180).add(v);
                pos.mulAdd(sumV, deltaTime);
            } else {
                pos.mulAdd(v, deltaTime);
            }
        }
        if((isBoss && pos.y > boss_poin_speed) || (pos.y <= poin_to_speed && isBoss) ){
            sumV.setZero().set(0f,v.y*80f);
            pos.mulAdd(sumV, deltaTime);
        }else if(pos.y <= poin_to_speed && !isBoss){
            sumV.setZero().set(v.x,v.y*1.5f);
            pos.mulAdd(sumV, deltaTime);
        }else{
            check_shoot(deltaTime);
        }
        super.update(deltaTime);
    }

    private void check_shoot(float deltaTime){
        if(Sprite.checkCollision(mainShip,this,Sprite.COLISION_ONLY_X) && !mainShip.isDestroyed()){
            reloadTimer += deltaTime;
            if(reloadTimer>= reloadInterval){
                shoot();
                reloadTimer = 0f;
            }
        }
    }


    private void correct(){
        v0.set(v.x,-v.y);

        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            rotate();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            rotate();
        }
    }

    @Override
    public void killed(){

            destroy();
            mainShip.setFrags(1);
    }

    @Override
    public void destroy() {
        super.destroy();

    }


    public void setTrakingV(Vector2 trakingV) {
        this.trakingV = trakingV;
    }

}
