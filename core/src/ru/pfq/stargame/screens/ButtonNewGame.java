package ru.pfq.stargame.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.pfq.stargame.actions.ButtonActionListener;
import ru.pfq.stargame.sprite.Sprite;

/**
 * Created by root on 11/25/17.
 */

public class ButtonNewGame extends Sprite {

    private static final float WIDTH = 0.5f;
    private static final float BOTTOM_MARGIN = 0.01f;

    private boolean pressed;
    private ButtonActionListener actionListener;


    public ButtonNewGame(TextureAtlas atlas, ButtonActionListener actionListener) {
        super(atlas.findRegion("new_game"));
        setWithProportion(WIDTH);
        setBottom(BOTTOM_MARGIN);
        this.actionListener = actionListener;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {

        if(!pressed && isMe(touch)) {
            //frame = 1;
            pressed = true;
            return true;
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        //frame = 0;
        if(pressed && isMe(touch)) {
            //frame = 0;
            pressed = false;
            actionListener.actionPerformed(this);
            return true;
        }
        pressed = false;
        return super.touchUp(touch, pointer);
    }
}
