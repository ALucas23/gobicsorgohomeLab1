package games.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class SnakeField extends GameEntity{
    final static Color COLOR = Color.LIME;

    List<LinkedList> snakeTiles;
    public SnakeField(int x, int y, int tot, int screenRect) {
        super(x, y, tot, screenRect);
    }

    @Override
    public void draw_me(GraphicsContext gc) {
        gc.setFill(COLOR);
        gc.fillRect(x, y, r, r);
    }
}
