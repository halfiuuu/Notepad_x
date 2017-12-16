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
        final int local_width=c.getWidth();
        final int local_height=c.getHeight();

        final int color_nullified=applyProcessToColor(.5f);

        if(local_width!=onDraw_latestF||local_height!=onDraw_latestH||onDraw_bitmap_array==null){
            onDraw_latestF=local_width;
            onDraw_latestH=local_height;
            onDraw_bitmap_array=new int[local_width*local_height];
            if(onDraw_bitmap!=null) {
                onDraw_bitmap.recycle();
                onDraw_bitmap = null;
            }
            if(local_width==local_height&&local_width==0)
                onDraw_bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
            else
                onDraw_bitmap = Bitmap.createBitmap(local_width, local_height, Bitmap.Config.ARGB_8888);
        }

        final boolean horizontal=local_width>local_height;
        if(!(onDraw_latest_color_nullified_initialized&&onDraw_latest_color_nullified==color_nullified)) {
            onDraw_latest_color_nullified = color_nullified;
            if (!onDraw_latest_color_nullified_initialized) {
                onDraw_latest_color_nullified_initialized = true;
            }
            float pro = 0;

            if(horizontal) {
                final float step = 1f / (float) local_width;
                for (int i = 0; i < local_width; i++, pro += step)
                    for (int y = 0, color = applyProcessToColor(pro); y < local_height; y++)
                        onDraw_bitmap_array[y * local_width + i] = color;
            }else{
                final float step = 1f / (float) local_height;
                for (int i = 0; i < local_height; i++, pro += step)
                    for (int y = 0, color = applyProcessToColor(pro); y < local_width; y++)
                        onDraw_bitmap_array[i * local_width + y] = color;
            }

            onDraw_bitmap.setPixels(onDraw_bitmap_array, 0, local_width, 0, 0, local_width, local_height);
        }

        drawBackground(c);
        c.drawBitmap(onDraw_bitmap,0,0,null);
        onDraw_paint.setColor(POINTERCOLOR);
        if(horizontal) {
            final int estimate_pos = estimatePointerPosition(local_width);
            c.drawRect(estimate_pos, 0, estimate_pos + POINTERWIDTH, local_height, onDraw_paint);
        }else{
            final int estimate_pos = estimatePointerPosition(local_height);
            c.drawRect(0, estimate_pos, local_width, estimate_pos + POINTERWIDTH, onDraw_paint);
        }
    }

    protected void drawBackground(Canvas c) {
        c.drawColor(Color.TRANSPARENT);
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent me){
        final int width = getWidth();
        final int height = getHeight();
        final boolean horizontal= width > height;
        process=(horizontal?(me.getX()/ width):(me.getY()/ height));
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
        Log.d("ColorSliderGeneric","Guessed progress to be "+process+" from "+getColor());
    }

    protected abstract float getCanalProcess(int c);

    protected float getMaxCanal() {
        return 255;
    }

}
