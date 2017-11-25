package ru.pfq.stargame.engine.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.pfq.stargame.engine.math.Rect;
import ru.pfq.stargame.engine.math.Utils;
import ru.pfq.stargame.sprite.Sprite;

public abstract class SpritesPool <T extends Sprite> {

    protected final List<T> activeObjects = new LinkedList<T>();
    protected final List<T> freeObjects = new LinkedList<T>();

    protected Rect worldBounds ;


    protected abstract T newObject();


    public T obtain(){
        T object;
        if(freeObjects.isEmpty()){
            object = newObject();
        }else{
            object = freeObjects.remove(freeObjects.size()-1);
        }
        activeObjects.add(object);
        debugLog();
        return object;
    }

    public void updateActiveSprites(float delta){
        Iterator<T> objectIterator = activeObjects.iterator();
        while (objectIterator.hasNext()) {
            Sprite sprite = objectIterator.next();
            if(sprite.isDestroyed()){
               // throw new RuntimeException("An attempt was made to update an object marked for deletion");
            }

            sprite.update(delta);
        }
    }

    public void resizeActiveSprites(Rect worldBounds){
        this.worldBounds = worldBounds;
        Iterator<T> objectIterator = activeObjects.iterator();
        while (objectIterator.hasNext()) {
            Sprite sprite = objectIterator.next();
            if(sprite.isDestroyed()){
                // throw new RuntimeException("An attempt was made to update an object marked for deletion");
            }
            sprite.resize(worldBounds);
        }
    }

    public void drawActiveObjects(SpriteBatch batch){
        Iterator<T> objectIterator = activeObjects.iterator();
        while (objectIterator.hasNext()) {
            Sprite sprite = objectIterator.next();
            if(sprite.isDestroyed()){
              //  throw new RuntimeException("An attempt was made to draw an object marked for deletion");
            }
            sprite.draw(batch);
        }

    }

    public void freeAllDestroyedActiveObjects(){
        try {
            Iterator<T> objectIterator = activeObjects.iterator();
            while (objectIterator.hasNext()) {
                T sprite = objectIterator.next();
                if (sprite.isDestroyed()) {
                    free(sprite);
                    sprite.flushDestoyed();
                }
            }
        }catch (ConcurrentModificationException ex){}
    }

    public void free(T object){
        if(!activeObjects.remove(object)){
            throw new RuntimeException("Attempt to delete a nonexistent object");
        }
        freeObjects.add(object);
        debugLog();
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose(){
        activeObjects.clear();
        freeObjects.clear();
    }

    public void colisionBetween(float delta){
        for (int i = 0; i<activeObjects.size(); i++) {
            for(int j=activeObjects.size()-1;j>=0;j--){
                if(!activeObjects.get(i).equals(activeObjects.get(j))){
                    if(Sprite.checkCollision(activeObjects.get(i),activeObjects.get(j))){
                        if(activeObjects.get(i).colisionBetween != activeObjects.get(j) && activeObjects.get(j).colisionBetween != activeObjects.get(i)) {
                            activeObjects.get(i).rotate();
                            activeObjects.get(i).colisionBetween = activeObjects.get(j);
                            activeObjects.get(i).reloadColisionBetween = 0.5f;

                            activeObjects.get(j).rotate();
                            activeObjects.get(j).colisionBetween = activeObjects.get(i);
                            activeObjects.get(j).reloadColisionBetween = 0.5f;

                          // System.out.println(Sprite.checkCollision(activeObjects.get(i), activeObjects.get(j)));
                        }
                    }
                    //System.out.println(SpritesPool.checkForCollision(activeObjects.get(i),activeObjects.get(j)));
                }

            }

            if(Utils.isFloatingEqual(activeObjects.get(i).reloadColisionBetween,0f)){
                activeObjects.get(i).colisionBetween = null;
            }else{
                activeObjects.get(i).reloadColisionBetween += 0.02f;
            }

        }
    }



    public void debugLog(){

    }
}
