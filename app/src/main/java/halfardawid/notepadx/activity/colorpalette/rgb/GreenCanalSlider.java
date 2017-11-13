package halfardawid.notepadx.activity.colorpalette.rgb;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import halfardawid.notepadx.activity.colorpalette.ColorSlider;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

/**
 * Created by Dawid on 2017-11-12.
 */

public final class GreenCanalSlider extends ColorSlider {
    public GreenCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.green(c)/getMaxCanal();
    }
    protected int applyProcessToColor(float process){
        int color=getColor();
        return Color.argb(Color.alpha(color),Color.red(color),(int) (getMaxCanal()*process),Color.blue(color));
    }
}
