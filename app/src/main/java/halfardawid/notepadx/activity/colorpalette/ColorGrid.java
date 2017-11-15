package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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

    private float[] onDraw_hsv =new float[3];
    private int onDraw_latest_bitmap_x=-1;
    private int onDraw_latest_bitmap_y=-1;
    private Bitmap onDraw_bitmap=null;
    private int[] onDraw_bitmap_array=null;
    private float onDraw_latestSaturation=-1;

    @Override
    public synchronized void onDraw(Canvas c){
        int mx=c.getWidth();
        int my=c.getHeight();
        int color=getColor();
        int alpha=Color.alpha(color);
        if(onDraw_latest_bitmap_x!=mx||onDraw_latest_bitmap_y!=my){
            onDraw_latest_bitmap_x=mx;
            onDraw_latest_bitmap_y=my;
            onDraw_bitmap_array=new int[mx*my];
            if(onDraw_bitmap!=null)onDraw_bitmap.recycle();
            onDraw_bitmap=Bitmap.createBitmap(mx,my, Bitmap.Config.ARGB_8888);
        }
        Color.colorToHSV(color, onDraw_hsv);
        if(onDraw_latestSaturation!=onDraw_hsv[0]) {
            onDraw_latestSaturation=onDraw_hsv[0];
            float onDraw_x_step = 1f / (float) mx;
            float onDraw_y_step = 1f / (float) my;
            onDraw_hsv[1] = 0;
            for (int x = 0; x < mx; x++, onDraw_hsv[1] += onDraw_x_step) {
                onDraw_hsv[2] = 0;
                for (int y = 0; y < my; y++, onDraw_hsv[2] += onDraw_y_step)
                    onDraw_bitmap_array[y * mx + x] = Color.HSVToColor(alpha, onDraw_hsv);
            }
            onDraw_bitmap.setPixels(onDraw_bitmap_array,0,mx,0,0,mx,my);
        }
        c.drawBitmap(onDraw_bitmap,0,0,null);
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
