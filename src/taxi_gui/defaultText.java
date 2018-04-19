package taxi_gui;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class defaultText extends Text {
  
    public defaultText(String text, double x , double y){
        this.setFont(Font.font("Verdana", 24));
        this.setText(text);
        this.setX(x);
        this.setY(y);
    }
    
    
}
