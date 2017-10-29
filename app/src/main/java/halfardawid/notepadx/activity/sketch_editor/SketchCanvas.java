package halfardawid.notepadx.activity.sketch_editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.SoftTipCircle;
import halfardawid.notepadx.activity.sketch_editor.brushes.SolidCircleBrush;
import halfardawid.notepadx.activity.sketch_editor.finger_movement.Fingers;


public class SketchCanvas extends View {
    private static final String TAG = "SKETCH_CANVAS";
    private Fingers controller;
    private boolean move=false;
    private boolean erase=false;
    private Brush brush=new SolidCircleBrush(50,25);

    public SketchCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        image=new SmartBitmap();
        controller =new Fingers(this);
    }



    public void loadSettings(Bundle s) {
        image.loadSettings(s);
    }
    public void saveSettings(Bundle s) {
        image.saveSettings(s);
    }

    public boolean isErasing(){
        return erase;
    }

    public boolean isMoveMode(){
        return move;
    }

    private SmartBitmap image;

    @Override
    protected void onDraw(Canvas c){
        c.save();
        c.drawColor(Color.TRANSPARENT);
        image.drawOnCanvas(c);
        c.restore();
    }
    @Override
    public boolean onTouchEvent(final MotionEvent me){
        controller.handleEvent(me);
        postInvalidate();
        return true;
    }

    public boolean toggleMove() {
        return move=!move;
    }

    public Bitmap getBitmap(){
        return image.getData();
    }

    public void setBitmap(Bitmap arg0){
        if(arg0==null)return;
        image.clone(arg0);
    }

    public SmartBitmap getSmartBitmap() {
        return image;
    }

    public Brush getBrush() {
        return brush;
    }

    public boolean toggleErase() {
        return erase=!erase;
    }
}
