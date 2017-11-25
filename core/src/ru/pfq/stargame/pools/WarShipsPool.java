package ru.pfq.stargame.pools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.pool.SpritesPool;
import ru.pfq.stargame.objects.Explosion;
import ru.pfq.stargame.objects.MainShip;
import ru.pfq.stargame.objects.Ship;
import ru.pfq.stargame.objects.WarShip;
import ru.pfq.stargame.sprite.Sprite;


public class WarShipsPool extends SpritesPool<WarShip> {

    private static  final  int   WARS_COUNT_MAX  = 50;
    private static  final  float POINT_TO_SPEED_X       = -0.1f;
    private static  final  float BOSS_POINT_SPEED_X     = 0.35f;

    //private final TextureRegion warShipRegion;
    private final Vector2 trakingV;

    //private static   final float reloadIntervalWarShips = 1f;
    //private static   float reloadTimerWarShips = 0f;

    private  final MainShip mainShip;
    private  final BulletPool bulletPool;
    private  final ExplosionPool explosionPool;
    private  final EnemyEmitter enemyEmitter;

    public  boolean hasBossOnTheScreen = false;


    public WarShipsPool(TextureAtlas atlas, MainShip mainShip, Sound bulletSound) {
        //warShipRegion = atlas.findRegion("enemy2");
        this.trakingV = mainShip.getV();
        explosionPool = new ExplosionPool(atlas);
        bulletPool = new BulletPool(mainShip);
        this.mainShip = mainShip;
        this.enemyEmitter = new EnemyEmitter(this,worldBounds,atlas,bulletSound);
    }

    public List<WarShip> getActiveShips(){
        return activeObjects;
    }

    @Override
    protected WarShip newObject() {
        WarShip w = new WarShip(POINT_TO_SPEED_X,BOSS_POINT_SPEED_X,bulletPool,explosionPool,worldBounds,mainShip);
        w.setTrakingV(trakingV);
        return w;
    }

    @Override
    public void updateActiveSprites(float delta) {
        super.updateActiveSprites(delta);
        this.bulletPool.updateActiveSprites(delta);
        this.explosionPool.updateActiveSprites(delta);
    }

    @Override
    public void drawActiveObjects(SpriteBatch batch) {
        super.drawActiveObjects(batch);
        this.bulletPool.drawActiveObjects(batch);
        this.explosionPool.drawActiveObjects(batch);
    }


    @Override
    public void resizeActiveSprites(Rect worldBounds) {
        super.resizeActiveSprites(worldBounds);
    }

    @Override
    public WarShip obtain() {
        WarShip object;
        if(freeObjects.isEmpty()){
            if(activeObjects.size()+freeObjects.size()<WARS_COUNT_MAX) {
                object = newObject();
            }else{object = null;}
        }else{
            object = freeObjects.remove(freeObjects.size()-1);
        }
        if(object != null) {
            object.setTop(worldBounds.getTop() + WarShip.TOP_MARGIN);

            activeObjects.add(object);
        }
        debugLog();

        return object;

    }

    public void proccessWarShips(float deltaTime){
        enemyEmitter.generateEnemies(deltaTime);

    }

    public void checkCollisions(){
        Iterator<WarShip> objectIterator = activeObjects.iterator();
        while (objectIterator.hasNext()) {
            WarShip sprite = objectIterator.next();
            if (!sprite.isDestroyed()&& Sprite.checkCollision(sprite,mainShip)) {
                mainShip.setHP(10);
            }
        }
    }

    @Override
    public void freeAllDestroyedActiveObjects() {
        try {
            Iterator<WarShip> objectIterator = activeObjects.iterator();
            while (objectIterator.hasNext()) {
                WarShip sprite = objectIterator.next();
                if (sprite.isDestroyed()) {
                    if(sprite.isBoss){
                        this.hasBossOnTheScreen = false;
                    }
                    free(sprite);
                    sprite.flushDestoyed();
                }
            }
        }catch (ConcurrentModificationException ex){}
        this.bulletPool.freeAllDestroyedActiveObjects();
        this.explosionPool.freeAllDestroyedActiveObjects();
    }
}
