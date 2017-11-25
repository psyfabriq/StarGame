package ru.pfq.stargame.engine.math;


import com.badlogic.gdx.math.Vector2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rect {

    protected static final String NON_TOP    = "nontop";
    protected static final String NON_BOTTOM = "nonbottom";
    protected static final String NON_LEFT   = "nonleft";
    protected static final String NON_RIGHT  = "nonright";

    public final Vector2 pos = new Vector2();
    protected float halfWidth;
    protected float halfHeight;

    public Rect() {

    }

    public Rect(Rect from) {
        this(from.pos.x, from.pos.y, from.getHalfWidth(), from.getHalfHeight());
    }

    public Rect(float x, float y, float halfWidth, float halfHeight) {
        pos.set(x, y);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    public float getLeft() {
        return pos.x - halfWidth;
    }

    public float getTop() {
        return pos.y + halfHeight;
    }

    public float getRight() {
        return pos.x + halfWidth;
    }

    public float getBottom() {
        return pos.y - halfHeight;
    }

    public float getHalfWidth() {
        return halfWidth;
    }

    public float getHalfHeight() {
        return halfHeight;
    }

    public float getWidth() {
        return halfWidth * 2f;
    }

    public float getHeight() {
        return halfHeight * 2f;
    }

    public void set(Rect from) {
        pos.set(from.pos);
        halfWidth = from.halfWidth;
        halfHeight = from.halfHeight;
    }

    public void setLeft(float left) {
        pos.x = left + halfWidth;
    }

    public void setTop(float top) {
        pos.y = top - halfHeight;
    }

    public void setRight(float right) {
        pos.x = right - halfWidth;
    }

    public void setBottom(float bottom) {
        pos.y = bottom + halfHeight;
    }

    public void setWidth(float width) {
        this.halfWidth = width / 2f;
    }

    public void setHeight(float height) {
        this.halfHeight = height / 2f;
    }

    public void setSize(float width, float height) {
        this.halfWidth = width / 2f;
        this.halfHeight = height / 2f;
    }

    public boolean isMe(Vector2 touch) {
        return touch.x >= getLeft() && touch.x <= getRight() && touch.y >= getBottom() && touch.y <= getTop();
    }

    public boolean isOutside(Rect other, String... non) {
        boolean result = false;
          try {
              if (non.length == 0) {
                  return getLeft() > other.getRight() || getRight() < other.getLeft() || getBottom() > other.getTop() || getTop() < other.getBottom();
              } else {
                  List<String> list = new ArrayList<String>(Arrays.asList(non));
                  if (!list.contains(NON_TOP)) {
                     // System.out.println(getTop());
                      result = getTop() < other.getBottom();
                  } else if (!list.contains(NON_BOTTOM) && !result) {
                      result = getBottom() > other.getTop();
                  } else if (!list.contains(NON_LEFT) && !result) {
                      result = getLeft() > other.getRight();
                  } else if (!list.contains(NON_RIGHT) && !result) {
                      result = getRight() < other.getLeft();
                  }

              }
          }catch (RuntimeException ex){}

        return result;
    }

    @Override
    public String toString() {
        return "Rectangle: pos" + pos + " size(" + getWidth() + ", " + getHeight() + ")";
    }
}
