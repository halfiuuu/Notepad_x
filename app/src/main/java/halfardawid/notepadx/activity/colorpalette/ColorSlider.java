package halfardawid.notepadx.activity.colorpalette;

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


    @Override
    public void onDraw(Canvas c){
        final int f=c.getWidth();
        final int h=c.getHeight();
        //final int h_cut=h-cutout;
        final int estimate_pos = estimatePointerPosition(f);

        drawBackground(c);

        final float step=1f/(float)f;

        int[] bitmap=new int[f*h];
        float pro=0;
        for(int i=0;i<f;i++,pro+=step)
            for(int y=0,color=applyProcessToColor(pro);y<h;y++)
                bitmap[y*f+i]=color;

        c.drawBitmap(Bitmap.createBitmap(bitmap,f,h, Bitmap.Config.ARGB_8888),0,0,null);
        Paint p=new Paint();
        p.setColor(POINTERCOLOR);
        c.drawRect(new RectF(estimate_pos,0,estimate_pos+POINTERWIDTH,h),p);
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
