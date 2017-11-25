package ru.pfq.stargame.sprite;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.utils.Regions;

public class Sprite extends Rect  {

   // public final static int COLISION_ALL   =0;
    public final static int COLISION_ONLY_X=1;
    public final static int COLISION_ONLY_Y=2;
   // public final static int COLISION_HALF_X=3;
   // public final static int COLISION_HALF_Y=4;


    protected float angle;
    protected float scale = 1f;
    protected TextureRegion[] regions;
    public int frame;
    protected boolean isDestroyed;

    public  float     reloadColisionBetween = 0f;
    public  Sprite    colisionBetween = null;

    public Sprite(){

    }
    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new NullPointerException("Create Sprite with null region");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public Sprite(TextureRegion[] regions) {
        this.regions = regions;
    }

    public Sprite(TextureRegion region, int rows, int cols, int frame){
        this.regions = Regions.split(region,rows,cols,frame);
    }

    public void draw(SpriteBatch batch) {
            if(!isDestroyed() && regions[frame] !=null) {
                batch.draw(
                        regions[frame], // текущий регион
                        getLeft(), getBottom(), // точка отрисовки
                        halfWidth, halfHeight, // точка вращения
                        getWidth(), getHeight(), // ширина и высота
                        scale, scale, // масштаб по оси x и оси y
                        angle // угол вращения
                );
            }
    }

    /**
     * Считает соотношение сторон textureRegion и устанавливает with и посчитанный height
     * @param with - ширина
     */
    public void setWithProportion(float with) {
        setWidth(with);
        float aspect =  regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setHeight(with / aspect);
    }

    /**
     * Считает соотношение сторон textureRegion и устанавливает with и посчитанный height
     * @param height - высота
     */
    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect =  regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth((height / aspect)-(height / aspect)*0.25f);
    }

    public void resize(Rect worldBounds) {

    }

    public boolean touchDown(Vector2 touch, int pointer) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    public boolean keyDown(int keycode){
        return false;
    }

    public boolean keyUp( int keycode){
        return false;
    }

    public void update(float deltaTime) {}

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void destroy(){
        this.isDestroyed = true;
        this.colisionBetween = null;
        this.reloadColisionBetween = 0f;
    }

    public void rotate(){}

    public void flushDestoyed(){
        this.isDestroyed = false;
    }
    @Override
    public String toString() {
        return "Sprite{" +
                "angle=" + angle +
                ", scale=" + scale +
                ", regions=" + Arrays.toString(regions) +
                ", frame=" + frame +
                '}';
    }

    public static <E extends Sprite> Boolean  checkCollision(E obj1, E obj2,int... settings){
        boolean result = false;
        if(settings.length == 0) {
            result = obj1.pos.x >= obj2.getLeft() && obj1.pos.x <= obj2.getRight() && obj1.pos.y >= obj2.getBottom() && obj1.pos.y <= obj2.getTop();
        }else{
            for (int s: settings) {
                if(s == COLISION_ONLY_X){
                    result = obj1.pos.x >= obj2.getLeft() && obj1.pos.x <= obj2.getRight();
                }else if(s == COLISION_ONLY_Y){
                    result =  obj1.pos.y >= obj2.getBottom() && obj1.pos.y <= obj2.getTop();
                }
                /*
                else if(s == COLISION_HALF_X){
                    result = obj1.pos.x >= obj2.getLeft()/2 && obj1.pos.x <= obj2.getRight()/2 && obj1.pos.y >= obj2.getBottom() && obj1.pos.y <= obj2.getTop();
                }
                else if(s == COLISION_HALF_Y){
                    result = obj1.pos.x >= obj2.getLeft() && obj1.pos.x <= obj2.getRight() && obj1.pos.y >= obj2.getBottom()+obj2.getBottom() && obj1.pos.y <= obj2.getTop()*0.5;
                }
                */
            }
        }
        return result;
    }

    public boolean isDestroyed() {
        return  isDestroyed;
    }
}