package ru.pfq.stargame.objects;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.pools.BulletPool;
import ru.pfq.stargame.pools.ExplosionPool;
import ru.pfq.stargame.sprite.Sprite;

public class Ship extends Sprite {

    protected static  final  int   LEFT  = -1;
    protected static  final  int   RIGHT =  1;
    protected static  final  int   STOP  =  0;

    protected  int hp;

    protected final Vector2 v0   = new Vector2(0.5f,0.0f);
    protected final Vector2 v = new Vector2(); // speed
    protected Rect worldBounds; // borders of the world

    protected final ExplosionPool  explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected final Vector2 bulletV = new Vector2();
    protected float bulletHeight;
    protected int bulletDamage;

    protected  float reloadInterval;
    protected  float reloadTimer;

    protected  float reloadIntervalDamage = 1f;
    protected  float reloadTimerDamage = 0f;

    protected Sound bulletSound;


    protected  int k = STOP;

    public Ship(TextureRegion region, int rows, int cols, int frame,BulletPool bulletPool,ExplosionPool explosionPool, Rect worldBounds) {
        super(region, rows, cols, frame);
        this.bulletPool    = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds   = worldBounds;
    }

    public Ship(BulletPool bulletPool,ExplosionPool explosionPool, Rect worldBounds) {
        this.bulletPool    = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds   = worldBounds;

    }


    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected void shoot(){

        Bullet bullet = bulletPool.obtain();
        bullet.set(this,bulletRegion,
                pos,
                bulletV,
                bulletHeight,
                worldBounds,
                bulletDamage);
        if(bulletSound.play(0.1f) == -1){
           // throw new RuntimeException("Cant play Sound");
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(frame == 1) {
            reloadTimerDamage += deltaTime;
            if (reloadTimerDamage >= reloadIntervalDamage) {
                reloadTimerDamage = 0f;
                frame = 0;
            }
        }
    }

    @Override
    public void rotate(){
        v.set(v0).rotate(180);
    }

    public Rect getWorldBounds() {
        return worldBounds;
    }

    public void setHP(int hp){
        if(this.hp<=0 && !this.isDestroyed){
            System.out.println("Game Over");
            Explosion expo =  explosionPool.obtain();
            expo.set(0.1f,pos);
            this.destroy();
        }else if(!this.isDestroyed){
                this.hp -= hp;
                System.out.println(this.hp);
                frame = 1;
        }
    }
}
