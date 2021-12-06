package timer;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

public class TimerTextField extends TextField {
    String timeFrame;
    public TimerTextField(String s, String timeFrame){
        super(s);
        this.timeFrame = timeFrame;
        this.setMaxWidth(30);
        this.setTextFormatter(new TextFormatter<String>(forceTwoInts));
    }

    UnaryOperator<TextFormatter.Change> forceTwoInts = inp -> {
        if (inp.getText().matches("")){
            if(inp.getControlNewText().matches("")){
                inp.setText("00");
            }else if(inp.getControlNewText().matches(".")){
                inp.setText("0");
            }
            return inp;
        }
        if (inp.getText().matches("[0-9]+")) {
            if (this.timeFrame.equals("hours")) {
                return processHours(inp);
            }else if(this.timeFrame.equals("mins") || this.timeFrame.equals("secs")){
                return processMinsOrSecs(inp);
            }
        }
        return null;
    };

    TextFormatter.Change processHours(TextFormatter.Change c){
        String nText = c.getControlNewText();
        int len = nText.length();
        if (len == 0){
            c.setText("00");
        }else if(len == 1){
            c.setText("0" + nText);
        }else{
            c.setText(nText.substring(len-2,len));
            c.setRange(0, 2);
        }
        c.selectRange(c.getControlNewText().length(),c.getControlNewText().length());
        return c;
    }

    TextFormatter.Change processMinsOrSecs(TextFormatter.Change c){
        String nText = c.getControlNewText();
        int len = nText.length();
        if (len == 0){
            c.setText("00");
        }else if(len == 1){
            c.setText("0" + nText);
        }else{
            String toSet = nText.substring(len-2,len);
            if(!toSet.matches("[0-5][0-9]")){
                toSet = "0" + toSet.substring(1,2);
            }
            c.setText(toSet);
            c.setRange(0, 2);
        }
        c.selectRange(c.getControlNewText().length(),c.getControlNewText().length());
        return c;
    }
}
