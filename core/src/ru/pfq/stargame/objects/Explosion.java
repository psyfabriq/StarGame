package ru.pfq.stargame.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.sprite.Sprite;

public class Explosion extends Sprite {

    protected  float animateInterval = 0.017f;
    protected  float animateTimer;


    public Explosion(TextureRegion region, int rows, int cols, int frame) {
        super(region, rows, cols, frame);
    }

    public void set(float height, Vector2 pos){
        this.pos.set(pos);
        setHeightProportion(height);
    }

    @Override
    public void update(float deltaTime) {
        animateTimer +=deltaTime;
        if(animateTimer >= animateInterval){
            animateTimer = 0f;
            if(++frame == regions.length)
            destroy();;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
