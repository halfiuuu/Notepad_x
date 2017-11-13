package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dawid on 2017-11-13.
 */

public class ColorGrid extends ColorSliderGeneric {
    ColorSliderResponseInterface palette=null;
    public static final int POINTERCOLOR = Color.BLACK;
    private static final int POINTERWIDTH = 5;
    private final int cutout=5;

    float saturation;
    float value;

    public ColorGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void guessProcess() {

    }

    @Override
    protected void onMeasure(int x, int y){
        super.onMeasure(x,x);
    }

    @Override
    public void onDraw(Canvas c){
        int mx=c.getWidth();
        int my=c.getHeight();
        Paint p=new Paint();
        int color=getColor();
        int alpha=Color.alpha(color);
        float[] hsv=new float[3];
        Color.colorToHSV(color,hsv);
        for(int x=0;x<mx;x++,hsv[1]=(float)x/(float)mx)
            for(int y=0;y<my;y++,hsv[2]=(float)y/(float)my,p.setColor(Color.HSVToColor(alpha,hsv)))
                c.drawPoint(x,y,p);
    }

    private int estimateColor(float s, float v) {
        int color=getColor();
        float[] hsv=new float[3];
        Color.colorToHSV(color,hsv);
        hsv[1]=s;
        hsv[2]=v;
        return Color.HSVToColor(Color.alpha(color),hsv);
    }

}
