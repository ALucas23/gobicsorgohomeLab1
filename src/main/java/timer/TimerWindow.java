package timer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pdfsam.rxjavafx.observables.JavaFxObservable;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.util.Objects;

public class TimerWindow extends Stage {
    private Label label;
    private Button button;
    Integer[] time;
    Observable<ActionEvent> buttonObservable;
    BehaviorSubject<Integer[]> timeSource;
    ReplaySubject<Boolean> finishedSource;
    Boolean firstTime;
    public TimerWindow(Integer[] time){
        this.setTitle("Timer");

        this.firstTime = true;
        this.time = time;
        this.timeSource = BehaviorSubject.create();
        this.finishedSource = ReplaySubject.create();
        this.label = new Label(getText(time));
        this.label.setId("label");
        update(time);
        this.button = new Button("Stop Timer");
        this.button.setId("button");
        this.buttonObservable = JavaFxObservable.actionEventsOf(button);
        this.buttonObservable
                .observeOn(JavaFxScheduler.platform())
                .subscribe(e -> {
                    triggerFinished();
                    this.close();
                });

        BorderPane bPane = new BorderPane();
        bPane.setId("mainWindow");
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(label);
        box.getChildren().add(button);

        bPane.setCenter(box);
        Scene scene = new Scene(bPane, 200,200);

        finishedSource
                .observeOn(JavaFxScheduler.platform())
                .subscribe(b -> {
                    if(b) {
                        this.setTitle("Pasta is ready!");
                        this.setAlwaysOnTop(true);
                        this.setAlwaysOnTop(false);
                    }
                });

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("timerStylesheet.css")).toExternalForm());

        this.setResizable(false);
        this.setScene(scene);
        this.show();
    }

    public Observable<Integer[]> getTimeOnChange(){
        return Observable.wrap(timeSource);
    }

    public Observable<Boolean> getFinished(){
        return Observable.wrap(finishedSource);
    }

    public void update(Integer[] time){
        this.label.setText(this.getText(time));
        timeSource.onNext(time);
        if(time[0] == 0 && time[1] == 0 && time[2] == 0) {
            triggerFinished();
        }
    }

    private void triggerFinished(){
        finishedSource.onNext(this.firstTime);
        this.firstTime = false;
    }

    public void aSecondPassedFrom(Integer[] time){
        if(time[2]-1 < 0){
            if(time[1]-1 < 0){
                if(time[0]-1 < 0) {
                    this.update(new Integer[]{0, 0, 0});
                }else{
                    this.update(new Integer[]{time[0] - 1, 59, 59});
                }
            }else{
                this.update(new Integer[]{time[0], time[1]-1, 59});
            }
        }else{
            this.update(new Integer[]{time[0], time[1], time[2]-1});
        }
    }

    private String getText(Integer[] time){
        String s = "";
        s += time[0] + " : ";
        if(time[1] < 10){
            s += "0" + time[1] + " : ";
        }else{
            s += time[1] + " : ";
        }
        if(time[2] < 10){
            s += "0" + time[2];
        }else{
            s += time[2];
        }
        return s;
    }
}
