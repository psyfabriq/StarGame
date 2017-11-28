package ru.pfq.stargame.pools;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ru.pfq.stargame.engine.pool.SpritesPool;
import ru.pfq.stargame.objects.Bullet;
import ru.pfq.stargame.objects.Explosion;
import ru.pfq.stargame.objects.Ship;
import ru.pfq.stargame.sprite.Sprite;


public class BulletPool extends SpritesPool<Bullet> {


    private final Set<Ship> listShips;
    protected  float reloadInterval = 0.7f;
    protected  float reloadTimer = 0f;;



    public BulletPool(Ship... ships) {
        listShips = new LinkedHashSet<Ship>(Arrays.asList(ships));
    }

    public void set(Ship ship){
            listShips.add(ship);
    }

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    @Override
    public void debugLog() {
        //System.out.println("Bulls active/free "+activeObjects.size() + "/"+ freeObjects.size());
    }

    @Override
    public void updateActiveSprites(float delta) {
        super.updateActiveSprites(delta);
        colisionBetween(delta);
    }


    @Override
    public void colisionBetween(float delta) {
        for (int i = 0; i<activeObjects.size(); i++) {
            Iterator<Ship> objectIterator = listShips.iterator();
            while (objectIterator.hasNext()) {
                Ship sprite = objectIterator.next();
                if(Sprite.checkCollision(activeObjects.get(i),sprite)){
                    if(sprite.frame == 0){
                        activeObjects.get(i).setShip(sprite);
                        sprite.setHP(activeObjects.get(i).getDamage());
                        if(sprite.isDestroyed()){
                            //listShips.remove(sprite);
                            System.out.println(
                                    "dgdfgdfgdfgdfgdfgdfg"
                            );
                        }
                    }
                    activeObjects.get(i).destroy();
                }
                /*
                reloadTimer += delta;
                if(reloadTimer>= reloadInterval){
                    reloadTimer = 0f;
                    sprite.frame = 0;
                }
                */
            }
        }




    }
}
