package halfardawid.notepadx.activity.sketch_editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import halfardawid.notepadx.util.vectors.Vector2i;

import static halfardawid.notepadx.util.vectors.Vector2i.OUT_OF_BOUNDS.BOTTOM;
import static halfardawid.notepadx.util.vectors.Vector2i.OUT_OF_BOUNDS.TOP;
import static halfardawid.notepadx.util.vectors.Vector2i.OUT_OF_BOUNDS.LEFT;
import static halfardawid.notepadx.util.vectors.Vector2i.OUT_OF_BOUNDS.RIGHT;
import static halfardawid.notepadx.util.vectors.Vector2i.OUT_OF_BOUNDS.NONE;

public class SketchCanvas extends View {
    private static final String TAG = "SKETCH_CANVAS";
    private final Lock sketch_lock=new ReentrantLock();

    public SketchCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap=makeClearBitmap(size);
    }

    private Bitmap makeClearBitmap(Vector2i s){
        Bitmap b=Bitmap.createBitmap(s.x,s.y, Bitmap.Config.ARGB_8888);
        /*for(int x=0;x<s.x;x++){
            for(int y=0;y<s.y;y++){
                b.setPixel(x,y,Color.BLUE);
            }
        }*/
        return b;
    }

    private boolean moving=false;
    private boolean erasing=false;

    private int brush_width=10;

    private Vector2i size=new Vector2i(1000,1000);
    private int steps=500;

    private Bitmap bitmap;
    private Vector2i offset=new Vector2i(0,0);

    @Override
    protected void onDraw(Canvas c){
        c.save();
        c.drawColor(Color.BLUE);
        c.drawBitmap(bitmap,offset.x,offset.y,null);
        c.restore();
    }
    @Override
    public boolean onTouchEvent(MotionEvent me){
        new Thread(new Touched(moving,new Vector2i((int)me.getX(),(int)me.getY()),me.getAction())).start();
        return true;
    }

    private final class Touched implements Runnable{
        private boolean mov;
        private Vector2i pos;
        private int action;

        public Touched(boolean mov, Vector2i pos, int action){
            this.mov=mov;
            this.pos=pos;
            this.action=action;
        }
        @Override
        public void run() {
            synchronized(sketch_lock) {
                if (moving) moveEvent(pos, action);
                else drawEvent(pos, action);
                postInvalidate();
            }
        }
    }

    private void drawEvent(Vector2i pos, int action) {
        Log.d(TAG,"Drawing "+action+" on "+pos.toString());
        for(int x=-brush_width;x<brush_width;x++){
            for(int y=-brush_width;y<brush_width;y++){
                Vector2i p=new Vector2i(pos);
                p.add(new Vector2i(x,y));
                drawPixel(p,Color.BLACK);
            }
        }
        switch(action){
            case MotionEvent.ACTION_DOWN:
                break;
        }
    }

    private void drawPixel(Vector2i pos, int c) {
        Vector2i pob = expandIfNeeded(pos);
        bitmap.setPixel(pob.x, pob.y, c);
    }

    @NonNull
    private Vector2i expandIfNeeded(Vector2i pos) {
        Vector2i pob;
        Vector2i expansion=null;
        for (; ; ) {
            pob = new Vector2i(pos);
            pob.add(offset);
            Vector2i.OUT_OF_BOUNDS bounds = pob.checkInBounds(size);
            if (bounds == NONE) break;
            Vector2i e=calc_expand(bounds);
            if(expansion==null)expansion=e;
            else expansion.add(e);
            //Log.d(TAG,"Bitmap expansion needed, "+expansion);
        }
        if(expansion!=null) {
            Bitmap b = makeClearBitmap(size);
            copyOnBitmap(expansion, b);
            bitmap = b;
            Log.d(TAG,"Bitmap expanded");
        }
        return pob;
    }

    private Vector2i calc_expand(Vector2i.OUT_OF_BOUNDS bounds) {
            if (bounds == TOP || bounds == BOTTOM) {
                size.y += steps;
            } else if (bounds == LEFT || bounds == RIGHT) {
                size.x += steps;
            }
            return getOffsetBitmap(bounds);
    }

    private void copyOnBitmap(Vector2i o, Bitmap b) {
        for(int x=0;x<bitmap.getWidth();x++)
            for(int y=0;y<bitmap.getHeight();y++)
                b.setPixel(x+o.x,y+o.y,bitmap.getPixel(x,y));
    }

    private Vector2i getOffsetBitmap(Vector2i.OUT_OF_BOUNDS bounds) {
        switch(bounds){
            case TOP:
                offset.y+=steps;
                return new Vector2i(0,steps);
            case LEFT:
                offset.x+=steps;
                return new Vector2i(steps,0);
            default:
                return new Vector2i(0,0);
        }
    }

    private void moveEvent(Vector2i pos, int action) {

    }

    public Bitmap getBitmap(){
        return Bitmap.createBitmap(bitmap);
    }

    public void setBitmap(Bitmap arg0){
        if(arg0==null)return;
        bitmap=arg0.copy(Bitmap.Config.ARGB_8888,true);
    }
}
