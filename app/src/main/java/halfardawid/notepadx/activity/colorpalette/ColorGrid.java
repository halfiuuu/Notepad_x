package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.graphics.Bitmap;
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
        int color=getColor();
        int alpha=Color.alpha(color);
        float[] hsv=new float[3];
        Color.colorToHSV(color,hsv);
        float x_step=1f/(float)mx;
        float y_step=1f/(float)my;
        hsv[1]=0;
        int[] bitmap=new int[mx*my];
        for(int x=0;x<mx;x++,hsv[1]+=x_step) {
            hsv[2]=0;
            for (int y = 0; y < my; y++, hsv[2] += y_step)
                bitmap[x*my+y]=Color.HSVToColor(alpha,hsv);
        }
        c.drawBitmap(Bitmap.createBitmap(bitmap,mx,my, Bitmap.Config.ARGB_8888),0,0,null);
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
