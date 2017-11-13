package halfardawid.notepadx.activity.colorpalette.hsv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.colorpalette.ColorSlider;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

/**
 * Created by Dawid on 2017-11-12.
 */

public final class HSVCanalSlider extends ColorSlider {
    int tabValue=0;
    float maxValue=1;
    public HSVCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
        applyAttr(context, attrs);
    }

    private void applyAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HSVCanalSlider,
                0, 0);
        try {
            tabValue = a.getInteger(R.styleable.HSVCanalSlider_type, 0);
            if(tabValue==0)maxValue=359;
        } finally {
            a.recycle();
        }
    }

    @Override
    protected float getCanalProcess(int c) {
        float[] hsv=new float[3];
        Color.colorToHSV(getColor(),hsv);
        return hsv[tabValue]/getMaxCanal();
    }

    protected int applyProcessToColor(float process){
        float[] hsv=new float[3];
        int color = getColor();
        Color.colorToHSV(color,hsv);
        hsv[tabValue]=(getMaxCanal()*process);
        return Color.HSVToColor(Color.alpha(color),hsv);
    }

    @Override
    protected float getMaxCanal() {
        return maxValue;
    }
}
