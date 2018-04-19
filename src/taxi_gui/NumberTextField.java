package taxi_gui;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{
    
    private int x;
    private int y;
    
    public NumberTextField(int x,int y){
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
    }
    
    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }
}
