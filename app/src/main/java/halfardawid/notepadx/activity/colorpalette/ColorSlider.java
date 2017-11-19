package halfardawid.notepadx.activity.colorpalette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import halfardawid.notepadx.util.ColorUtils;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

public abstract class ColorSlider extends ColorSliderGeneric {
    public static final int POINTERCOLOR = Color.BLACK;
    private static final int POINTERWIDTH = 5;
    private final int cutout=5;

    protected float process=0;

    public ColorSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Paint onDraw_paint=new Paint();
    private Bitmap onDraw_bitmap=null;
    private int onDraw_latestF=-1;
    private int onDraw_latestH=-1;
    private int[] onDraw_bitmap_array=null;
    private int onDraw_latest_color_nullified=0;
    private boolean onDraw_latest_color_nullified_initialized=false;
    @Override
    public synchronized void onDraw(Canvas c){
        final int f=c.getWidth();
        final int h=c.getHeight();

        final int color_nullified=applyProcessToColor(.5f);

        if(f!=onDraw_latestF||h!=onDraw_latestH||onDraw_bitmap_array==null){
            onDraw_latestF=f;
            onDraw_latestH=h;
            onDraw_bitmap_array=new int[f*h];
            if(onDraw_bitmap!=null) {
                onDraw_bitmap.recycle();
                onDraw_bitmap = null;
            }
            onDraw_bitmap = Bitmap.createBitmap(f, h, Bitmap.Config.ARGB_8888);
        }

        if(onDraw_latest_color_nullified_initialized&&onDraw_latest_color_nullified==color_nullified) {
            drawBackground(c);
            //c.drawRect(0,0,20,20,onDraw_paint);
            c.drawBitmap(onDraw_bitmap,0,0,null);
            onDraw_paint.setColor(POINTERCOLOR);
            final int estimate_pos = estimatePointerPosition(f);
            c.drawRect(estimate_pos,0,estimate_pos+POINTERWIDTH,h,onDraw_paint);
            c.drawRect(0,0,50,50,onDraw_paint);
            return;
        }

        onDraw_latest_color_nullified = color_nullified;
        if(!onDraw_latest_color_nullified_initialized) {
            onDraw_latest_color_nullified_initialized = true;
        }

        //final int h_cut=h-cutout;
        final int estimate_pos = estimatePointerPosition(f);

        drawBackground(c);

        final float step=1f/(float)f;

        float pro=0;
        for(int i=0;i<f;i++,pro+=step)
            for(int y=0,color=applyProcessToColor(pro);y<h;y++)
                onDraw_bitmap_array[y*f+i]=color;

        onDraw_bitmap.setPixels(onDraw_bitmap_array,0,f,0,0,f,h);
        c.drawBitmap(onDraw_bitmap,0,0,null);
        onDraw_paint.setColor(POINTERCOLOR);
        c.drawRect(estimate_pos,0,estimate_pos+POINTERWIDTH,h,onDraw_paint);
    }

    protected void drawBackground(Canvas c) {
        c.drawColor(Color.TRANSPARENT);
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent me){
        process=me.getX()/getWidth();
        if(process<0)process=0;
        else if(process>1)process=1;
        applyColor();
        //invalidate(); Every single one will be invalidated anyway
        return true;
    }

    private int estimatePointerPosition(int f) {
        int estimate_pos=(int)(f*process);
        if(estimate_pos<0)estimate_pos=0;
        else if(estimate_pos>f)estimate_pos=f-POINTERWIDTH;
        return estimate_pos;
    }

    protected void applyColor(){
        palette.setColor(applyProcessToColor(process));
    }

    protected abstract int applyProcessToColor(float process);

    protected final void guessProcess() {
        process=getCanalProcess(getColor());
    }

    protected abstract float getCanalProcess(int c);

    protected float getMaxCanal() {
        return 255;
    }

}
