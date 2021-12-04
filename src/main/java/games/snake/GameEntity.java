package games.snake;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameEntity {
    private static final int PADDING = 3;
    protected double x, y, r;
    public  GameEntity(int x, int y, int tot, int screenRect){
        this.r = (screenRect-(tot+1)*PADDING)/(double) tot;
        this.x = (x+1)*PADDING+x*this.r;
        this.y = (y+1)*PADDING+y*this.r;
    }

    abstract public void draw_me(GraphicsContext gc);
}
