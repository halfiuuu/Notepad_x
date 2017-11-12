package halfardawid.notepadx.activity.colorpalette.rgb;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import halfardawid.notepadx.activity.colorpalette.CanalSlider;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

/**
 * Created by Dawid on 2017-11-12.
 */

public class BlueCanalSlider extends CanalSlider {
    public BlueCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.green(c);
    }
    protected int applyProcessToColor(){
        int color=getColor();
        return Color.argb(Color.alpha(color),Color.red(color),(int) (getMaxCanal()*process),Color.blue(color));
    }
}
