package ru.pfq.stargame.objects;



import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.actions.ButtonActionListener;
import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Rnd;
import ru.pfq.stargame.sprite.Sprite;


public class Button extends Sprite  {

    private final Vector2 v = new Vector2();
    private Rect worldBounds;
    private boolean pressed;
    private ButtonActionListener actionListener;



    public Button(TextureRegion[] regions, float vx, float vy, float width , ButtonActionListener actionListener){
        super(regions);
        pressed = false;
        v.set(vx, vy);
        setWithProportion(width);
        this.actionListener = actionListener;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        if (v.x < worldBounds.pos.x) {
            //Left
            setLeft(worldBounds.getLeft());
        }else{
            //Right
            setRight(worldBounds.getRight());
        }
        if (v.y > worldBounds.pos.y) {
            //Top
            setTop(worldBounds.getTop());
        }else{
            //Bottom
            setBottom(worldBounds.getBottom());
        }


    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(!pressed && isMe(touch)) {
            frame = 1;
            pressed = true;
            return true;
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        frame = 0;
        if(pressed && isMe(touch)) {
            frame = 0;
            pressed = false;
            actionListener.actionPerformed(this);
            return true;
        }
        pressed = false;
        return super.touchUp(touch, pointer);
    }
}
