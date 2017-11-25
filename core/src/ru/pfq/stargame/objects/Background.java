package ru.pfq.stargame.objects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.sprite.Sprite;


public class Background extends Sprite {

    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}

