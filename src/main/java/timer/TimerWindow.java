package timer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TimerWindow extends Stage {
    private Label label;
    private Button button;
    public TimerWindow(int[] time){
        this.setTitle("Timer");

        this.label = new Label("" + time[0]);

        BorderPane bPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(label);
        hBox.getChildren().add(button);

        bPane.setCenter(hBox);
        Scene scene = new Scene(bPane, 200,200);

        this.setScene(scene);
        this.show();
    }

    private String getText(int[] time){
        //return String.format("")
        return "";
    }
}
