package games.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class SnakeField extends GameEntity{
    final static Color COLOR = Color.LIME;

    private int ttl;
    private boolean isHead;
    public SnakeField(int x, int y, int tot, int screenRect, int ttl) {
        super(x, y, tot, screenRect);
        this.ttl = ttl;
        this.isHead = true;
    }

    public boolean isHead(){
        return this.isHead;
    }

    public boolean move(){
        this.ttl--;
        this.isHead = false;
        return !isDead();
    }

    public boolean isDead(){
        return this.ttl <= 0;
    }

    public void extendLife(){
        this.ttl++;
    }

    @Override
    public void draw_me(GraphicsContext gc) {
        gc.setFill(COLOR);
        gc.fillRect(x, y, r, r);
    }
}
