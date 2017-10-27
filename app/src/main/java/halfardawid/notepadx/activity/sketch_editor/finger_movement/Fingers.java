package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import static android.view.MotionEvent.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import halfardawid.notepadx.activity.sketch_editor.SketchCanvas;
import halfardawid.notepadx.activity.sketch_editor.SmartBitmap;
import halfardawid.notepadx.util.vectors.Vector2i;

public class Fingers {
    public static final String TAG = "FINGERS";
    public static final boolean DEBUG_SPAM=false;

    private List<Finger> fingers=new ArrayList<>();
    private Lock lock=new ReentrantLock();
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
        if(fingers.isEmpty()){
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
            finger.newDistance(canvas.getBrush().splatLine(bitmap, lastPosition,pos,color_fun[id%color_fun.length],finger.latestDistance()));
        }else canvas.getBrush().splat(bitmap,pos,color_fun[id%color_fun.length]);
    }

    public void handleEvent(MotionEvent me) {
        synchronized (queue) {
            if(DEBUG_SPAM)Log.d(TAG, "ev>>" + me);
            new MoveThread(getFingerMovement(me)).start();
        }
    }

    private class MoveThread extends Thread{
        FingerMovement fm;
        public MoveThread(FingerMovement fm) {
            this.fm = fm;
        }
        @Override
        public void run(){
            synchronized (lock){
                fm.run();
            }
        }
    }

    private FingerMovement getFingerMovement(MotionEvent me) {
        switch(me.getActionMasked()){
            case ACTION_UP:
                return new LastFingerUp(this, me);
            case ACTION_POINTER_UP:
                return new FingerUp(this, me);
            case ACTION_MOVE:
                return new FingersMoved(this, me);
            case ACTION_DOWN:
                return new FirstFingerDown(this, me);
            case ACTION_POINTER_DOWN:
                return new FingerDown(this, me);
            default:
                return new WtfDidJustMove(me);
        }
    }

    public synchronized void remove(int id) {
        fingers.remove(id);
    }

    public synchronized void clear() {
        fingers.clear();
    }

    public synchronized void add(Finger finger) {
        fingers.add(finger);
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
