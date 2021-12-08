import games.snake.Snake;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import timer.TimerSpawner;
import timer.TimerWindow;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class App extends Application {
    static final long SPEED = 1000;

    Observable<Long> globalTicker;
    java.util.List<Observable<Boolean>> timerListener;
    Lock timerListenerLock;
    @Override
    public void start(Stage stage){
        timerListener = new java.util.ArrayList<>();
        timerListenerLock = new ReentrantLock();
        Snake snake = new Snake(5, 6*3+5*35);
        TimerSpawner timer = new TimerSpawner();
        snake.requestFocus();


        //Changing TextLabel + ImageView
        final String[] names = {"PF3's gonna be very funny.", "Florian: I'm a Hackerman.", "JavaFX UI Implementation = Magic", "I love Ocaml.", "Re-explain everything starting from PF2?", "So did I pass Lab1?"};
        Observable<String> nameObservable = Observable
                .interval(2, TimeUnit.SECONDS)
                .map(Long::intValue)
                .map(i -> names[i % names.length]);

        Label changeString = new Label();
        changeString.setId("changeString");
        changeString.setTextFill(Color.web("#CCCCCC", 1));

        nameObservable
                .observeOn(JavaFxScheduler.platform())
                .subscribe(changeString::setText);

        ImageView imageView = new ImageView();
        imageView.setId("imageView");
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);


        changeString.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (Objects.equals(changeString.getText(), "PF3's gonna be very funny.")) {
                    Image image = new Image("Funny.png");
                    imageView.setImage(image);
                } else if (Objects.equals(changeString.getText(), "Florian: I'm a Hackerman.")){
                    Image image = new Image("Hackerman.png");
                    imageView.setImage(image);
                } else if (Objects.equals(changeString.getText(), "JavaFX UI Implementation = Magic")){
                    Image image = new Image("Magic.png");
                    imageView.setImage(image);
                }else if (Objects.equals(changeString.getText(), "I love Ocaml.")){
                    Image image = new Image("Pierre.png");
                    imageView.setImage(image);
                }else if (Objects.equals(changeString.getText(), "Re-explain everything starting from PF2?")) {
                    Image image = new Image("Questions.png");
                    imageView.setImage(image);
                } else {
                    Image image = new Image("BelugaJR.png");
                    imageView.setImage(image);
                }
            }
        });

        //Display Time as Observable
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");

        Observable<String> currentTimeDisplay = Observable
                .interval( 1 , TimeUnit.SECONDS)
                .map(now -> LocalDateTime.now() )
                .map(now -> now.format(dateTimeFormatter) ) ;

        Label timecurrent = new Label();
        timecurrent.setId("time");
        timecurrent.setTextFill(Color.web("#CCCCCC", 1));

        currentTimeDisplay
                .observeOn(JavaFxScheduler.platform())
                .subscribe(timecurrent::setText);

        HBox timeBox = new HBox();
        timeBox.getChildren().add(timecurrent);

        //VBox for textImage
        VBox textImageVBox = new VBox();
        textImageVBox.getChildren().addAll(imageView, changeString);
        textImageVBox.setMaxHeight(Double.MAX_VALUE);
        textImageVBox.setMaxWidth(Double.MAX_VALUE);
        textImageVBox.setAlignment(Pos.BASELINE_CENTER);
        textImageVBox.setSpacing(20);

        //Title VBox (2 Labels)
        Label bigTitle = new Label("Welcome to the University of Luxembourg.");
        bigTitle.setId("bigTitle");
        Label smallTitle = new Label("BiCS - Bachelor in Computer Science");
        smallTitle.setId("smallTitle");
        smallTitle.setTextFill(Color.web("#ffffff", 0.8));
        VBox titleVBox = new VBox();

        //Add Title to VBox
        titleVBox.getChildren().addAll(bigTitle, smallTitle);
        titleVBox.setMaxHeight(Double.MAX_VALUE);
        titleVBox.setMaxWidth(Double.MAX_VALUE);
        titleVBox.setAlignment(Pos.BASELINE_CENTER);
        titleVBox.setSpacing(10);

        //Put Snake in a Box
        VBox snakeBox = new VBox();
        snakeBox.setMaxHeight(Double.MAX_VALUE);
        snakeBox.setMaxWidth(Double.MAX_VALUE);
        ObservableList<Node> topElemsList = snakeBox.getChildren();
        topElemsList.add(snake);
        snakeBox.setAlignment(Pos.BASELINE_CENTER);
        snakeBox.setSpacing(30);

        //BorderPane

        BorderPane borderPane = new BorderPane();
        Insets insets = new Insets(10);

        borderPane.setId("pane");

        BorderPane.setMargin(titleVBox, insets);
        BorderPane.setMargin(snakeBox, insets);
        BorderPane.setMargin(textImageVBox, insets);


        HBox bottomBox = new HBox();
        HBox leftBottom = new HBox();
        HBox rightBottom = new HBox();
        leftBottom.getChildren().add(timeBox);
        rightBottom.getChildren().add(timer);

        leftBottom.setAlignment(Pos.CENTER_LEFT);
        rightBottom.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(leftBottom, Priority.ALWAYS);
        HBox.setHgrow(rightBottom, Priority.ALWAYS);
        bottomBox.getChildren().add(leftBottom);
        bottomBox.getChildren().add(rightBottom);

        borderPane.setLeft(snakeBox);
        borderPane.setTop(titleVBox);
        borderPane.setBottom(bottomBox);
        borderPane.setRight(textImageVBox);



        Scene scene = new Scene(borderPane, 750, 464);

        stage.setTitle("The World Wonders of Programming Fundamentals 3");
        //END OF LAYOUT CODE



        globalTicker = Observable.interval(10, TimeUnit.MILLISECONDS)
                .map(i -> i%(24*60*60*100/SPEED));

        PublishSubject<Character> globalKeyPress = PublishSubject.create();
        snake.setOnKeyTyped(e -> globalKeyPress.onNext(e.getCharacter().charAt(0)));
        globalKeyPress.subscribe(System.out::println);

        globalTicker
                .filter(i -> i%50==0)
                .observeOn(JavaFxScheduler.platform())
                .withLatestFrom(globalKeyPress, (t, key) -> snake.update(key))
                .subscribe();


        timer.getButtonObservableOnClick()
                .subscribe(i -> System.out.println("TIMER WAS CREATED! " + i[0] + " : " + i[1] + " : " + i[2]));
        timer.getButtonObservableOnClick()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::createNewTimerWindow);


        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("stylesheet.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("pasta maker 3000");
        stage.setResizable(false);
        stage.show();
    }

    private Disposable disposable;
    private void createNewTimerWindow(Integer[] time){
        TimerWindow timer = new TimerWindow(time);
        globalTicker
                .map(i -> 1)
                .scan(0, (acc, i) -> acc+1)
                .map(i -> i%100)
                .filter(i -> i==0)
                .withLatestFrom(timer.getTimeOnChange(), (tick, t) -> t)
                .observeOn(JavaFxScheduler.platform())
                .subscribe(timer::aSecondPassedFrom);
        try {
            timerListenerLock.lock();
            Observable<Boolean> timerObservable = timer.getFinished();
            timerListener.add(timerObservable);
            timer.getFinished().subscribe(b -> {
                try {
                    timerListenerLock.lock();
                    timerListener.remove(timerObservable);
                }finally {
                    timerListenerLock.unlock();
                }
            });
            try {
                disposable.dispose();
            }catch (NullPointerException ignored){}
            disposable = Observable
                    .combineLatest(timerListener, b -> b)
                    .subscribe(b -> {
                        try {
                            timerListenerLock.lock();
                            if (Arrays.stream(b).anyMatch(a -> (Boolean) a)) {
                                playSound();
                            }
                        }finally {
                            timerListenerLock.unlock();
                        }
                    });
        }finally {
            timerListenerLock.unlock();
        }
    }

    private void playSound(){
        Toolkit.getDefaultToolkit().beep();
    }

    public static void main(String ... args) {
        launch();
    }



}
