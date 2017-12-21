/*
 * Copyright (c) 2017 anno Domini.
 *
 * Code below is a part of
 * https://github.com/halfiuuu/Notepad_x
 * available for use under the
 * GNU Affero General Public License v3.0
 * as stated in
 * https://github.com/halfiuuu/Notepad_x/blob/master/LICENSE
 *
 * Created by Dawid Halfar
 * contact possible via halfardawid@gmail.com
 *
 * I'm not sure what else this thing should state... Whatever.
 */

package halfardawid.notepadx.activity.sketch_editor.colorpalette;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


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
        @Override public void clearRefreshers() {}
        @Override public void addToRefresher(View colorSlider) {}
        @Override public void refreshAll(){}
        @Override public int getColor() {return Color.BLUE;}
        @Override public void setColor(int i) {}
    }

}
