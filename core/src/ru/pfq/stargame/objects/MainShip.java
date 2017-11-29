package ru.pfq.stargame.objects;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.xpath.internal.ExpressionOwner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.pools.BulletPool;
import ru.pfq.stargame.pools.ExplosionPool;

public class MainShip extends Ship  {

    private static  final  float SHIP_HEIGHT   = 0.1f;
    private static  final  float BOTTOM_MARGIN = 0.05f;
    private static  final  float CORRECT_ANGLE = 0.02f;
    private static  final  int   POINTER_COUNT = 2;



    private final Vector2 v0 = new Vector2(0.5f,0.0f);
    private final Vector2 v = new Vector2();

    private int frags;
    private int level;

    protected  int reloadIntervalLevelUp = 10;
    protected  int reloadTimerLevelUp = 0;




   // private boolean pressedLeft, pressedRight;




    class TouchInfo {
        public int pointer = 0;
        public float x = 0;
    }

    private ArrayList<TouchInfo> touches = new ArrayList<TouchInfo>();

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds,Sound bulletSound) {
        super(atlas.findRegion("main_ship3"),1,2,2,bulletPool,explosionPool,worldBounds);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletRegion = atlas.findRegion("bullet_strip_green");
        super.bulletSound = bulletSound;
        super.bulletSound.setVolume(1,0.05f);
        setToNewGame();
    }

    public void setToNewGame() {
        pos.x = worldBounds.pos.x;
        this.bulletHeight = 0.015f;
        this.bulletV.set(0, 0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
        hp = 100;
        frags = 0;
        level = 1;
        flushDestroy();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        setBottom(worldBounds.getBottom()+BOTTOM_MARGIN);

        if(getRight()>worldBounds.getRight()){
            setRight(worldBounds.getRight()-CORRECT_ANGLE);
        }
        if(getLeft()<worldBounds.getLeft()){
            setLeft(worldBounds.getLeft()+CORRECT_ANGLE);
        }
    }

    @Override
    public void update(float deltaTime) {
         pos.mulAdd(v,deltaTime);
         reloadTimer += deltaTime;
         if(reloadTimer>= reloadInterval){
             shoot();
             reloadTimer = 0f;
         }
        if(touches.size() == 0 && k == STOP ){
            stop();
        }else{
            for(int i = touches.size()-1; i >= 0; i--){
                if (touches.get(i).x < worldBounds.pos.x) {
                    k = LEFT;
                    moveLeft();
                } else {
                    k= RIGHT;
                    moveRight();
                }
                break;
            }
        }

        if(!(worldBounds.getRight()>getRight())  && k == RIGHT) {stop();}
        if(!(worldBounds.getLeft()<getLeft())   && k == LEFT)  {stop();}
        super.update(deltaTime);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        System.out.println(pointer);
        if(pointer < POINTER_COUNT){
            int j = touches.size();
            touches.add(j, new TouchInfo());
            touches.get(j).x = touch.x;
            touches.get(j).pointer = pointer;
        }


        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {

        if(pointer < POINTER_COUNT){
            for(int i = 0; i < touches.size(); i++){
                if(touches.get(i).pointer == pointer){
                    k = STOP;
                    touches.remove(i);
                    break;
                }
            }
        }

        return super.touchUp(touch, pointer);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                k = LEFT;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                k = RIGHT;
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                //pressedLeft = false;
                if(k == RIGHT){
                    moveRight();
                }else{stop();}
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                //pressedRight = false;
                if(k == LEFT){
                    moveLeft();
                }else{stop();}
                break;
            case Input.Keys.UP:

                break;
        }
        return super.keyUp(keycode);
    }



    private void moveRight(){
        if(worldBounds.getRight()>getRight()+CORRECT_ANGLE && k == RIGHT) {
            v.set(v0);
        }
    }

    private void moveLeft(){
        if(worldBounds.getLeft()<getLeft()-CORRECT_ANGLE  && k==LEFT) {
            v.set(v0).rotate(180);
        }
    }

    public int getFrags() {
        return frags;
    }

    public void setFrags(int frags) {
        this.frags += frags;
        this.reloadTimerLevelUp += frags;
        if(reloadTimerLevelUp >= reloadIntervalLevelUp){
            reloadTimerLevelUp = 0;
            reloadIntervalLevelUp += reloadIntervalLevelUp/2;
            level += 1;
            hp += 10;
        }

    }

    public int getLevel() {
        return level;
    }

    public void upLevel() {
        this.level += 1;
    }

    private void stop(){
        v.setZero();
    }

    public Vector2 getV(){
        return v;
    }


}
