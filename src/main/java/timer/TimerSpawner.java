package timer;

import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.pdfsam.rxjavafx.observables.JavaFxObservable;


public class TimerSpawner extends HBox {
    private ObservableList<Node> elems;
    private TextField hoursText;
    private TextField minsText;
    private TextField secsText;
    private Observable<ActionEvent> buttonObservable;
    public TimerSpawner(){
        this.setAlignment(Pos.CENTER);
        elems = this.getChildren();
        this.hoursText = new TimerTextField("00", "hours");
        elems.add(hoursText);
        elems.add(new Label(":"));
        this.minsText = new TimerTextField("00", "mins");
        elems.add(minsText);
        elems.add(new Label(":"));
        this.secsText = new TimerTextField("00", "secs");
        elems.add(secsText);
        Button button = new Button("Start Timer");
        button.setId("timerButton");
        elems.add(button);

        buttonObservable = JavaFxObservable.actionEventsOf(button);
    }

    public Observable<Integer[]> getButtonObservableOnClick(){
        return this.buttonObservable.map(e -> new Integer[]{Integer.parseInt(hoursText.getText()),
                Integer.parseInt(minsText.getText()), Integer.parseInt(secsText.getText())});
    }
}
