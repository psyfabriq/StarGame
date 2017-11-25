package ru.pfq.stargame.pools;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.pfq.stargame.engine.pool.SpritesPool;
import ru.pfq.stargame.objects.Explosion;
import ru.pfq.stargame.sprite.Sprite;

public class ExplosionPool extends SpritesPool<Explosion> {

    private final TextureRegion explosionRegion;

    public ExplosionPool(TextureAtlas atlas) {
        explosionRegion = atlas.findRegion("explosion");
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(explosionRegion ,9,9,74);
    }

    @Override
    public void debugLog() {
        System.out.println("Explosion active/free "+activeObjects.size() + "/"+ freeObjects.size());
    }
}
