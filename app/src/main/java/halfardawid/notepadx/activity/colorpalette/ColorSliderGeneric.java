package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dawid on 2017-11-13.
 */

public abstract class ColorSliderGeneric extends View{

    protected ColorSliderResponseInterface palette=null;

    public ColorSliderGeneric(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tiePalette(context);
        guessProcess();
    }


    private void tiePalette(Context context) {
        if(context instanceof ColorSliderResponseInterface) palette= (ColorSliderResponseInterface)context;
        else palette= new NonexistentPalette();
        palette.addToRefresher(this);
    }

    @Override
    public void invalidate(){
        guessProcess();
        super.invalidate();
    }

    protected int getColor(){
        return palette.getColor();
    }

    abstract protected void guessProcess();

    private static class NonexistentPalette implements ColorSliderResponseInterface {
        @Override public void addToRefresher(View colorSlider) {}
        @Override public int getColor() {return Color.BLUE;}
        @Override public void setColor(int i) {}
    }

}
