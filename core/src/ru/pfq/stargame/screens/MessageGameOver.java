package ru.pfq.stargame.screens;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.pfq.stargame.sprite.Sprite;

public class MessageGameOver extends Sprite {
    private static final float WIDTH = 0.5f;
    private static final float BOTTOM_MARGIN = 0.09f;


    public MessageGameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
        setWithProportion(WIDTH);
        setBottom(BOTTOM_MARGIN);

    }
}
