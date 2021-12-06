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
    private int totLength;
    public Snake(int fieldSize, int screenSize){
        super(screenSize, screenSize);
        this.fieldSize = fieldSize;
        this.screenSize = screenSize;
        this.direction = RIGHT;
        this.totLength = 1;
        gc = this.getGraphicsContext2D();
        this.setOnMousePressed(e -> this.requestFocus());
        reset();
        drawField();
    }

    private void reset(){
        createInitialField();
    }

    private void updateDirection(char c){
        if (c == 'w') {
            this.direction = UP;
        } else if (c == 'a') {
            this.direction = LEFT;
        } else if (c == 's') {
            this.direction = DOWN;
        } else if (c == 'd') {
            this.direction = RIGHT;
        }
    }

    public int update(char c){
        if(!this.isFocused()) {
            return 0;
        }
        try {
            updateDirection(c);
            int[] posHead = moveSnakeTail();
            moveSnakeHead(posHead[0], posHead[1]);
            drawField();


            if (isFull()) {
                // TODO implement cool animation,
                //  idea pop up a nwe screen with a trophy and we are the champions song
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }

    private void moveSnakeHead(int hRow, int hCol){
        int nextRow = hRow + ((this.direction == RIGHT)?1:
                (this.direction == LEFT)?-1:0);
        int nextCol = hCol + ((this.direction == DOWN)?1:
                (this.direction == UP)?-1:0);
        if(nextRow < 0 || nextCol < 0 || nextRow >= fieldSize || nextCol >= fieldSize){
            reset();
            return;
        }
        if(field[nextRow][nextCol] instanceof SnakeField){
            reset();
            return;
        }
        if(field[nextRow][nextCol] instanceof FruitField){
            extendLife();
            spawnFruit();
        }
        createSnakeField(nextRow, nextCol);
    }

    private void extendLife(){
        this.totLength++;
        for (int row = 0; row < this.fieldSize; row++) {
            for (int col = 0; col < this.fieldSize; col++) {
                if(field[row][col] instanceof SnakeField snakeField){
                    snakeField.extendLife();
                }
            }
        }
    }

    private int[] moveSnakeTail(){
        int hRow = -1, hCol = -1;
        for (int row = 0; row < this.fieldSize; row++) {
            for (int col = 0; col < this.fieldSize; col++) {
                if (field[row][col] instanceof SnakeField snakeField) {
                    if(snakeField.isHead()){
                        hRow = row;
                        hCol = col;
                    }
                    if (!snakeField.move()) {
                        field[row][col] = new EmptyField(row, col, this.fieldSize, this.screenSize);
                    }
                }
            }
        }
        return new int[]{hRow, hCol};
    }

    private void createInitialField(){
        this.field = new GameEntity[this.fieldSize][this.fieldSize];
        for (int row = 0; row < this.fieldSize; row++) {
            for (int col = 0; col < this.fieldSize; col++) {
                field[row][col] = new EmptyField(row, col, this.fieldSize, this.screenSize);
            }
        }
        int startRow = (int) (random()* this.fieldSize);
        int startCol = (int) (random()* this.fieldSize);
        this.totLength = 1;
        createSnakeField(startRow, startCol);

        spawnFruit();
    }

    private void createSnakeField(int startRow, int startCol){
        field[startRow][startCol] = new SnakeField(startRow, startCol, this.fieldSize, this.screenSize, this.totLength);
    }

    protected void spawnFruit(){
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

    private void drawField(){
        gc.clearRect(0, 0, screenSize, screenSize);
        for (GameEntity[] gameEntities : field) {
            for (GameEntity gameEntity : gameEntities) {
                gameEntity.draw_me(this.gc);
            }
        }
    }
}
