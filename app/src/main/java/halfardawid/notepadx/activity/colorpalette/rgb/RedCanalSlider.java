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

public final class RedCanalSlider extends ColorSlider {
    public RedCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.red(c)/getMaxCanal();
    }
    protected int applyProcessToColor(float process){
        int color=getColor();
        int f=(int) (getMaxCanal()*process);
        if(process==1.0f) Log.d("XD","1.0.f "+f);
        return Color.argb(Color.alpha(color),f,Color.green(color),Color.blue(color));
    }
}
