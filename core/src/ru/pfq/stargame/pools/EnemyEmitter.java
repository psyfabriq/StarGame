package ru.pfq.stargame.pools;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Rnd;
import ru.pfq.stargame.engine.utils.Regions;
import ru.pfq.stargame.objects.WarShip;

public class EnemyEmitter {

    private static final float ENEMY_SMALL_HEIGHT = 0.08f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.4f;
    private static final int   ENEMY_SMALL_BULLET_DAMEGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 0.5f;
    private static final int   ENEMY_SMALL_HP = 1;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.1f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_MEDIUM_BULLET_VY = -0.3f;
    private static final int   ENEMY_MEDIUM_BULLET_DAMEGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 0.7f;
    private static final int   ENEMY_MEDIUM_HP = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.3f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_BIG_BULLET_VY = -0.3f;
    private static final int   ENEMY_BIG_BULLET_DAMEGE = 6;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 1f;
    private static final int   ENEMY_BIG_HP = 10;


    private final Rect worlBounds;
    private final Sound bulletSound;

    private  float generateInteval = 2f;
    private  float generateTimer;

    private final TextureRegion[] enemySmallRegion;
    private final TextureRegion[] enemyMediumRegion;
    private final TextureRegion[] enemyBigRegion;

    private final Vector2 enemyV  = new Vector2();


    private final TextureRegion bulletRegion;

    private final WarShipsPool enemyPool;


    public EnemyEmitter(WarShipsPool enemyPool, Rect worlBounds, TextureAtlas atlas, Sound bulletSound) {
        this.enemyPool   = enemyPool;
        this.worlBounds  = worlBounds;
        this.bulletSound = bulletSound;
        this.bulletSound.setVolume(1,0.1f);

        TextureRegion region0 = atlas.findRegion("enemy0");
        TextureRegion region1 = atlas.findRegion("enemy1");
        TextureRegion region2 = atlas.findRegion("enemy2");

        this.enemySmallRegion  = Regions.split(region0,1,2,2);
        this.enemyMediumRegion = Regions.split(region1,1,2,2);
        this.enemyBigRegion    = Regions.split(region2,1,2,2);

        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generateEnemies(float deltatime){
        generateTimer+=deltatime;
        if(generateTimer >=generateInteval){
            generateTimer = 0f;
            float type = (float) Math.random();

            if(type<0.7f){
                WarShip enemy = enemyPool.obtain();
                enemyV.set(Rnd.nextFloat(-0.09f, 0.09f), Rnd.nextFloat(-0.1f, -0.3f));
                enemy.set(enemySmallRegion,
                        enemyV,bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_BULLET_DAMEGE,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        this.bulletSound,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP,
                        false);

            }else if(type<0.9f){
                WarShip enemy = enemyPool.obtain();
                enemyV.set(Rnd.nextFloat(-0.09f, 0.09f), Rnd.nextFloat(-0.04f, -0.08f));
                enemy.set(enemyMediumRegion,
                        enemyV,bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_BULLET_DAMEGE,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        this.bulletSound,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP,
                        false);
            }else if(!enemyPool.hasBossOnTheScreen){
                WarShip enemy = enemyPool.obtain();
                enemyPool.hasBossOnTheScreen = true;
                enemyV.set(Rnd.nextFloat(-0.04f, 0.04f), Rnd.nextFloat(-0.002f, -0.01f));
                enemy.set(enemyBigRegion,
                        enemyV,bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_BULLET_DAMEGE,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        this.bulletSound,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP,
                        true);
            }



        }
    }
}
