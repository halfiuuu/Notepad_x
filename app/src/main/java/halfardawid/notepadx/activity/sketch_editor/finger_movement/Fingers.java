package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import halfardawid.notepadx.activity.sketch_editor.SketchCanvas;
import halfardawid.notepadx.activity.sketch_editor.SmartBitmap;
import halfardawid.notepadx.util.vectors.Vector2i;

public class Fingers {
    public static final String TAG = "FINGERS";
    public static final boolean DEBUG_SPAM=true;

    private List<Finger> fingers=new ArrayList<>();
    Lock lock=new ReentrantLock();
    private Lock queue=new ReentrantLock();

    private SmartBitmap bitmap;
    private SketchCanvas canvas;

    private int[] color_fun={
            Color.BLUE,Color.RED, Color.GREEN, Color.MAGENTA,Color.YELLOW
    };

    public Fingers(SketchCanvas canvas){
        this.canvas=canvas;
        bitmap=canvas.getSmartBitmap();
    }


    synchronized void fingerMoved(int id, Vector2i pos) {
        if(DEBUG_SPAM)Log.d(TAG,"fingers "+fingers.size()+" "+fingers);
        if(id>=fingers.size()){
            Log.d(TAG,"No finger data, i dunno");
            return;
        }
        Finger finger=fingers.get(id);
        Vector2i difference=new Vector2i(pos);
        Vector2i lastPosition = finger.lastPosition();
        difference.sub(lastPosition);
        if(DEBUG_SPAM)Log.d(TAG,"Finger "+id+" moved to "+pos+" with a difference of "+difference);
        if(!difference.isNone()){
            finger.addNewPoint(pos);
            finger.newDistance(canvas.getBrush().splatLine(bitmap, lastPosition,pos,getColor(id),finger.latestDistance()));
        }else canvas.getBrush().splat(bitmap,pos,getColor(id));
    }

    public int getColor(int id){
        return (canvas.isErasing())?Color.TRANSPARENT:color_fun[id%color_fun.length];
    }

    public void handleEvent(MotionEvent me) {
        synchronized (queue) {
            if(DEBUG_SPAM)Log.d(TAG, "ev>>" + me);
            new MoveThread(this, this, me).start();
        }
    }


    public synchronized void remove(int id) {
        if(fingers.size()>id)
            fingers.remove(id);
    }

    public synchronized void clear() {
        fingers.clear();
    }

    public synchronized void add(Finger finger) {
        fingers.add(finger);
    }

    public void invalidate(){
        canvas.postInvalidate();
    }

}

/*
 public void move(Vector2i v) {
        Log.d(TAG, action + " " + pos);
        if (action == MotionEvent.ACTION_DOWN) {
            move_vector = new Vector2i(offset);
            move_vector.sub(pos);
        } else if (move_vector == null) {
            Log.wtf(TAG, "logic error for move event...");
            return;
        }
        offset.copy(move_vector);
        offset.add(pos);
        if (action == MotionEvent.ACTION_UP) move_vector = null;
    }
 */
