package games.snake;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import static java.lang.Math.random;

public class Snake extends Canvas{
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;

    GraphicsContext gc;
    int fieldSize, screenSize;
    private GameEntity[][] field;
    private int direction;
    public Snake(int fieldSize, int screenSize){
        super(screenSize, screenSize);
        this.fieldSize = fieldSize;
        this.screenSize = screenSize;
        this.direction = RIGHT;
        gc = this.getGraphicsContext2D();
        create_initial_field();
        draw_field();
    }

    public String update(char c){
        if (c == 'w') {
            this.direction = UP;
        } else if (c == 'a') {
            this.direction = LEFT;
        } else if (c == 's') {
            this.direction = DOWN;
        } else if (c == 'd') {
            this.direction = RIGHT;
        }
        return "test";
    }

    private void create_initial_field(){
        this.field = new GameEntity[this.fieldSize][this.fieldSize];
        for (int row = 0; row < this.fieldSize; row++) {
            for (int col = 0; col < this.fieldSize; col++) {
                field[row][col] = new EmptyField(row, col, this.fieldSize, this.screenSize);
            }
        }
        int startRow = (int) (random()* this.fieldSize);
        int startCol = (int) (random()* this.fieldSize);
        field[startRow][startCol] = new SnakeField(startRow, startCol, this.fieldSize, this.screenSize);

        spawn_fruit();
    }

    protected void spawn_fruit(){
        if(isFull()){
            return;
        }
        int row, col;
        do{
            row = (int) (random()* this.fieldSize);
            col = (int) (random()* this.fieldSize);
        }while(!(this.field[row][col] instanceof EmptyField));
        this.field[row][col] = new FruitField(row, col, this.fieldSize, this.screenSize);
    }

    private boolean isFull(){
        for (GameEntity[] gameEntities : this.field) {
            for (GameEntity gameEntity : gameEntities) {
                if (!(gameEntity instanceof SnakeField)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void draw_field(){
        gc.clearRect(0, 0, screenSize, screenSize);
        for (GameEntity[] gameEntities : field) {
            for (GameEntity gameEntity : gameEntities) {
                gameEntity.draw_me(this.gc);
            }
        }
    }
}
