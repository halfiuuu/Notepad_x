package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.util.exceptions.InvalidContextException;

public abstract class ColorSlider extends View {
    public static final int POINTERCOLOR = Color.BLACK;
    private static final int POINTERWIDTH = 5;
    ColorPaletteActivity palette=null;

    protected float process=0;

    public ColorSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
        if(!(context instanceof ColorPaletteActivity))throw new InvalidContextException();
        palette= (ColorPaletteActivity) context;
        palette.addToRefresher(this);
        guessProcess();
    }

    @Override
    public void onDraw(Canvas c){
        final int cutout=20;
        final int f=c.getWidth();
        final int h=c.getHeight();
        final int h_cut=h-cutout;
        final int estimate_pos = estimatePosition(f);

        c.drawColor(Color.TRANSPARENT);

        Paint p=new Paint();
        for(int i=0;i<f;i++,p.setColor(applyProcessToColor((float)i/(float)f))){
            c.drawLine(i,cutout,i,h_cut,p);
        }
        p.setColor(POINTERCOLOR);
        c.drawRect(new RectF(estimate_pos,0,estimate_pos+POINTERWIDTH,h),p);
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent me){
        process=me.getX()/getWidth();
        if(process<0)process=0;
        else if(process>1)process=1;
        applyColor();
        invalidate();
        return true;
    }

    private int estimatePosition(int f) {
        int estimate_pos=(int)(f*process);
        if(estimate_pos<0)estimate_pos=0;
        else if(estimate_pos>f)estimate_pos=f-POINTERWIDTH;
        return estimate_pos;
    }

    protected int getColor(){
        return palette.getColor();
    }

    protected void applyColor(){
        palette.setColor(applyProcessToColor(process));
    }

    protected abstract int applyProcessToColor(float process);

    protected final void guessProcess() {
        process=getCanalProcess(getColor())/getMaxCanal();
    }

    protected abstract float getCanalProcess(int c);

    protected float getMaxCanal() {
        return 256;
    }
}
