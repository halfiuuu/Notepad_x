package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
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

public abstract class ColorSlider extends View {
    public static final int POINTERCOLOR = Color.BLACK;
    private static final int POINTERWIDTH = 5;
    private final int cutout=5;
    ColorSliderInterface palette=null;

    protected float process=0;

    public ColorSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
        tiePalette(context);
        guessProcess();
    }

    private void tiePalette(Context context) {
        if(context instanceof ColorSliderInterface) palette= (ColorSliderInterface)context;
        else palette= new NonexistentPalette();
        palette.addToRefresher(this);
    }

    @Override
    public void onDraw(Canvas c){
        final int f=c.getWidth();
        final int h=c.getHeight();
        final int h_cut=h-cutout;
        final int estimate_pos = estimatePointerPosition(f);

        drawBackground(c);

        Paint p=new Paint();
        for(int i=0;i<f;i++,p.setColor(applyProcessToColor((float)i/(float)f))){
            c.drawLine(i,cutout,i,h_cut,p);
        }
        p.setColor(ColorUtils.calcContrast(applyProcessToColor(0)));
        c.drawText("SLIDER",0,0,p);
        p.setColor(POINTERCOLOR);
        c.drawRect(new RectF(estimate_pos,0,estimate_pos+POINTERWIDTH,h),p);
    }

    protected void drawBackground(Canvas c) {
        c.drawColor(Color.TRANSPARENT);
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent me){
        process=me.getX()/getWidth();
        Log.d("XD",me.toString()+" "+me.getX()+" "+getWidth()+" "+process);
        if(process<0)process=0;
        else if(process>1)process=1;
        Log.d("XD",me.toString()+" "+me.getX()+" "+getWidth()+" "+process);
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

    protected int getColor(){
        return palette.getColor();
    }

    protected void applyColor(){
        Log.d("CDS","ojewio "+" "+getMaxCanal()+" process:"+process);
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

    private static class NonexistentPalette implements ColorSliderInterface {
        @Override public void addToRefresher(View colorSlider) {}
        @Override public int getColor() {return Color.RED;}
        @Override public void setColor(int i) {}
    }

    @Override
    public void invalidate(){
        guessProcess();
        super.invalidate();
    }

}
