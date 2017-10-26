package halfardawid.notepadx.activity.sketch_editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import halfardawid.notepadx.util.vectors.Vector2i;

import static android.R.attr.bitmap;


public class SketchCanvas extends View {
    private static final String TAG = "SKETCH_CANVAS";
    private Fingers history=new Fingers();

    public SketchCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        image=new SmartBitmap();
    }



    public void loadSettings(Bundle s) {
        mode=new SketchMode(s);
        image.loadSettings(s);
    }
    public void saveSettings(Bundle s) {
        mode.saveSetting(s);
        image.saveSettings(s);
    }

    public boolean isErasing(){
        return mode.isEraser();
    }

    public boolean isMoveMode(){
        return mode.isMove();
    }

    SketchMode mode=new SketchMode();

    private SmartBitmap image;

    @Override
    protected void onDraw(Canvas c){
        c.save();
        c.drawColor(Color.TRANSPARENT);
        image.drawOnCanvas(c);
        c.restore();
    }
    @Override
    public boolean onTouchEvent(MotionEvent me){
        history.handleEvent(me);
        Log.d(TAG,me+" event");
        return true;
    }

    public void toggleMove() {
        mode.toggleMove();
    }

    public Bitmap getBitmap(){
        return image.getData();
    }

    public void setBitmap(Bitmap arg0){
        if(arg0==null)return;
        image.clone(arg0);
    }
}
