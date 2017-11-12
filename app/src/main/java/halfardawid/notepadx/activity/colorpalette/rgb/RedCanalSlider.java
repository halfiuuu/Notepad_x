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

public class RedCanalSlider extends CanalSlider {
    public RedCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.red(c);
    }
    protected int applyProcessToColor(){
        int color=getColor();
        return Color.argb(Color.alpha(color),(int) (getMaxCanal()*process),Color.green(color),Color.blue(color));
    }
}
