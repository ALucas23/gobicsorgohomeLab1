package games.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FruitField extends GameEntity{
    final static Color COLOR = Color.CYAN;

    public FruitField(int x, int y, int tot, int screenRect) {
        super(x, y, tot, screenRect);
    }

    @Override
    public void draw_me(GraphicsContext gc) {
        gc.setFill(COLOR);
        gc.fillRect(x, y, r, r);
    }
}
