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


public class SketchCanvas extends View {
    private static final String TAG = "SKETCH_CANVAS";
    private final Lock sketch_lock=new ReentrantLock();

    public SketchCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap=makeClearBitmap(default_size);
    }

    private Bitmap makeClearBitmap(Vector2i s){
        Bitmap b=Bitmap.createBitmap(s.x,s.y, Bitmap.Config.ARGB_8888);
        /*for(int x=0;x<s.x;x++){
            for(int y=0;y<s.y;y++){
                b.setPixel(x,y,Color.RED);
            }
        }*/
        return b;
    }

    private boolean moving=false;
    private boolean erasing=false;

    private int brush_width=10;

    private int steps=250;

    private Bitmap bitmap;
    private Vector2i offset=new Vector2i(0,0);
    private static final Vector2i default_size=new Vector2i(1000,1000);

    private Vector2i move_vector=null;


    @Override
    protected void onDraw(Canvas c){
        c.save();
        c.drawColor(Color.TRANSPARENT);
        c.drawBitmap(bitmap,offset.x,offset.y,null);
        c.restore();
    }
    @Override
    public boolean onTouchEvent(MotionEvent me){
        new Thread(new Touched(moving,new Vector2i((int)me.getX(),(int)me.getY()),me.getAction())).start();
        return true;
    }

    public void toggleMove() {
        moving=!moving;
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
        Vector2i np=new Vector2i(pos);
        np.sub(offset);
        expandIfNeeded(np);
        np=new Vector2i(pos);
        np.sub(offset);
        bitmap.setPixel(np.x, np.y, c);
    }

    @NonNull
    private void expandIfNeeded(Vector2i pos) {
        Vector2i changes = pos.checkInBounds(new Vector2i(bitmap),steps);
        if(changes.isNone())return;
        //Log.d(TAG,"Changes needed for "+pos+" as "+changes);
        Vector2i addSize=new Vector2i(changes);
        addSize.abs();
        addSize.add(new Vector2i(bitmap));
        //Log.d(TAG,"sum size "+addSize);
        Bitmap b = makeClearBitmap(addSize);
        Vector2i f=new Vector2i(changes);
        f.cutAllPositive();
        f.abs();
        //Log.d(TAG,"new offset "+f);
        copyOnBitmap(f, b);
        bitmap = b;
        //Log.d(TAG,"Bitmap expanded");
        offset.sub(f);
    }


    private void copyOnBitmap(Vector2i o, Bitmap b) {
        //Log.d(TAG, b.getWidth()+"/"+b.getHeight()+" new bitmap");
        //Log.d(TAG, bitmap.getWidth()+"/"+bitmap.getHeight()+" old bitmap +"+size);
        for(int x=0;x<bitmap.getWidth();x++)
            for(int y=0;y<bitmap.getHeight();y++) {
                //Log.d(TAG,"copy "+x+"/"+y+" to "+(x+o.x)+"/"+(y+o.y));
                b.setPixel(x + o.x, y + o.y, bitmap.getPixel(x, y));
            }
    }

    private void moveEvent(Vector2i pos, int action) {
        if(action==MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "down ");
            move_vector=new Vector2i(offset);
            move_vector.sub(pos);
        }else if(move_vector==null){
            Log.wtf(TAG,"logic error for move event...");
            return;
        }
        offset.copy(move_vector);
        offset.add(pos);
        if(action==MotionEvent.ACTION_UP)move_vector=null;
    }

    public Bitmap getBitmap(){
        return Bitmap.createBitmap(bitmap);
    }

    public void setBitmap(Bitmap arg0){
        if(arg0==null)return;
        bitmap=arg0.copy(Bitmap.Config.ARGB_8888,true);
    }
}
