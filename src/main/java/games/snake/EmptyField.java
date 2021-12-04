package games.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EmptyField extends GameEntity{
    final static Color COLOR = Color.CYAN;
    final static double PERCENTAGE = 0.1;

    private final double tweakedX, tweakedY, tweakedR;

    public EmptyField(int x, int y, int tot, int screenRect){
        super(x, y, tot, screenRect);
        this.tweakedX = this.x+this.r*PERCENTAGE;
        this.tweakedY = this.y+this.r*PERCENTAGE;
        this.tweakedR = this.r*(1-2*PERCENTAGE);
    }

    @Override
    public void draw_me(GraphicsContext gc) {
        gc.setStroke(COLOR);
        gc.strokeRect(tweakedX, tweakedY, tweakedR, tweakedR);
    }
}
