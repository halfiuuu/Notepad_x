package halfardawid.notepadx.activity.colorpalette.rgb;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import halfardawid.notepadx.activity.colorpalette.ColorSlider;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

/**
 * Created by Dawid on 2017-11-12.
 */

public final class BlueCanalSlider extends ColorSlider {
    public BlueCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.blue(c)/getMaxCanal();
    }
    protected int applyProcessToColor(float process){
        int color=getColor();

        //Log.d("CDS","ojewio "+" "+getMaxCanal()+" process:"+process+" "+(getMaxCanal()*process)+" o_O");
        return Color.argb(Color.alpha(color),Color.red(color),Color.green(color),(int) (getMaxCanal()*process));
    }
}
