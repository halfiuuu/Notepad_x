package halfardawid.notepadx.activity.sketch_editor.colorpalette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by Dawid on 2017-11-13.
 */

public class ColorGrid extends ColorSliderGeneric {
    public static final int POINTER_COLOR = Color.BLACK;
    private static Paint POINTER_PAINT =new Paint();
    static{
        POINTER_PAINT.setColor(POINTER_COLOR);
        POINTER_PAINT.setStyle(Paint.Style.STROKE);
        POINTER_PAINT.setStrokeWidth(10f);
    }
    private static final int POINTER_WIDTH = 20;
    private final int cutout=5;
    private static String TAG="COLOR_GRID";

    float saturation;
    float value;

    public ColorGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void guessProcess() {
        int color=getColor();
        float hsv[]=new float[3];
        Color.colorToHSV(color, hsv);//TODO: Rewrite this too (maybe someday...)
        saturation=hsv[1];
        value=hsv[2];
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent me){
        saturation=me.getX()/getWidth();
        value=me.getY()/getHeight();
        if(saturation<0)saturation=0;
        else if(saturation>1)saturation=1;
        if(value<0)value=0;
        else if(value>1)value=1;
        palette.setColor(estimateColor(saturation,value));
        return true;
    }

    private float[] onDraw_hsv =new float[3];
    private int onDraw_latest_bitmap_x=-1;
    private int onDraw_latest_bitmap_y=-1;
    //private Bitmap onDraw_bitmap=null;
    private float onDraw_latestSaturation=-1;
    private int onDraw_latestAlpha=-1;
    private final static int onDraw_bitmap_x=50;
    private final static int onDraw_bitmap_y=50;
    private final static int onDraw_bitmap_size=onDraw_bitmap_x*onDraw_bitmap_y;
    private final static float onDraw_x_step = 1f / (float) onDraw_bitmap_x;
    private final static float onDraw_y_step = 1f / (float) onDraw_bitmap_y;
    private Bitmap onDraw_bitmap_unscaled=Bitmap.createBitmap(onDraw_bitmap_x,onDraw_bitmap_y, Bitmap.Config.ARGB_8888);

    private int[] onDraw_bitmap_array=new int[onDraw_bitmap_size];

    @Override
    public synchronized void onDraw(Canvas c){
        long startTime = System.nanoTime();
        Log.d(TAG,"[from one line to another...]Time taken: "+(System.nanoTime()-startTime));
        int mx=c.getWidth();
        int my=c.getHeight();
        int color=getColor();
        int alpha=Color.alpha(color);
        Log.d(TAG,"[Initialized variables]Time taken: "+(System.nanoTime()-startTime));
        if(onDraw_latest_bitmap_x!=mx||onDraw_latest_bitmap_y!=my){
            onDraw_latest_bitmap_x=mx;
            onDraw_latest_bitmap_y=my;
            //onDraw_bitmap_array=new int[mx*my];
            //if(onDraw_bitmap!=null)onDraw_bitmap.recycle();
            //onDraw_bitmap=Bitmap.createBitmap(mx,my, Bitmap.Config.ARGB_8888);
            Log.d(TAG,"[Initialized static variables]Time taken: "+(System.nanoTime()-startTime));
        }
        Log.d(TAG,"[Pre]Time taken: "+(System.nanoTime()-startTime));
        Color.colorToHSV(color, onDraw_hsv);//TODO: Rewrite it, maketh faster
        Log.d(TAG,"[Parsed color]Time taken: "+(System.nanoTime()-startTime));
        if(onDraw_latestSaturation!=onDraw_hsv[0]||onDraw_latestAlpha!=alpha) {

            Log.d(TAG,"[Gonna draw manually]Time taken: "+(System.nanoTime()-startTime));
            onDraw_latestSaturation=onDraw_hsv[0];
            onDraw_latestAlpha=alpha;
            onDraw_hsv[1] = 0;

            Log.d(TAG,"[All set]Time taken: "+(System.nanoTime()-startTime));
            for (int x = 0; x < onDraw_bitmap_x; x++, onDraw_hsv[1] += onDraw_x_step) {
                onDraw_hsv[2] = 0;
                for (int y = 0; y < onDraw_bitmap_y; y++, onDraw_hsv[2] += onDraw_y_step)
                    onDraw_bitmap_array[y * onDraw_bitmap_x + x] = Color.HSVToColor(alpha,onDraw_hsv);
            }

            Log.d(TAG,"[bitmap_array initialized, now onto drawing it on the bitmap!]Time taken: "+(System.nanoTime()-startTime));
            onDraw_bitmap_unscaled.setPixels(onDraw_bitmap_array,0,onDraw_bitmap_x,0,0,onDraw_bitmap_x,onDraw_bitmap_y);
            Log.d(TAG,"[bitmap pixels setup done]Time taken: "+(System.nanoTime()-startTime));
        }
        c.drawColor(Color.TRANSPARENT);
        Log.d(TAG,"[Ready to draw]Time taken: "+(System.nanoTime()-startTime));
        c.drawBitmap(Bitmap.createScaledBitmap(onDraw_bitmap_unscaled,mx,my,false),0,0,null);
        Log.d(TAG,"[Finished drawing]Time taken: "+(System.nanoTime()-startTime));
        c.drawCircle(mx*saturation,my*value, POINTER_WIDTH, POINTER_PAINT);
        Log.d(TAG,"[Painted pointer]Time taken: "+(System.nanoTime()-startTime));
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
