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
    public static final boolean DEBUG_SPAM=false;

    private List<Finger> fingers=new ArrayList<>();

    private SmartBitmap bitmap;
    private SketchCanvas canvas;

    private MoveThread moveThread;

    private int[] color_fun={
            Color.BLUE,Color.RED, Color.GREEN, Color.MAGENTA,Color.YELLOW
    };

    public Fingers(SketchCanvas canvas){
        this.canvas=canvas;
        bitmap=canvas.getSmartBitmap();
        moveThread=new MoveThread(this);
        moveThread.start();
    }


    void fingerMoved(int id, Vector2i pos) {
        if(DEBUG_SPAM)Log.d(TAG,"fingers "+fingers.size()+" "+fingers);
        if(id>=fingers.size()){
            Log.d(TAG,"No finger data, i dunno");
            return;
        }

        Finger finger = fingers.get(id);
        Vector2i difference = new Vector2i(pos);
        Vector2i lastPosition = finger.lastPosition();
        difference.sub(lastPosition);

        if(!difference.isNone())finger.addNewPoint(pos);

        if(canvas.isMoveMode()){
            if(fingers.size()==2){
                Vector2i current_position_change=new Vector2i(fingers.get(id==1?0:1).lastPosition());
                Vector2i last_position_change=new Vector2i(current_position_change);
                current_position_change.sub(pos);
                last_position_change.sub(lastPosition);
                float change=(current_position_change.pythagoras()-last_position_change.pythagoras());
                bitmap.zoom(change/1000);
                Log.d(TAG,"zoom mode "+change);
            }else{
                bitmap.move(difference);
                Log.d(TAG,"move mode "+difference);
            }
        }else {
            if (DEBUG_SPAM)
                Log.d(TAG, "Finger " + id + " moved to " + pos + " with a difference of " + difference);
            if (!difference.isNone()) {
                finger.newDistance(canvas.getBrush().splatLine(bitmap, lastPosition, pos, getColor(), finger.latestDistance()));
            }else if(finger.timesTouched()==1){
                canvas.getBrush().splat(bitmap, pos, getColor());
                finger.newDistance(canvas.getBrush().getSpacing(bitmap));
            }
        }
    }

    public int getColor(){
        return (canvas.isErasing())?Color.TRANSPARENT:color_fun[(int)(Math.random()*100)%color_fun.length];
    }

    public void handleEvent(MotionEvent me) {
        if(DEBUG_SPAM)Log.d(TAG, "ev>>" + me);
        moveThread.add(me);
        //new MoveThread(this, this, me).start();
    }

    public void remove(int id) {
        if(fingers.size()>id)
            fingers.remove(id);
    }

    public void clear() {
        fingers.clear();
    }

    public void add(Finger finger) {
        fingers.add(finger);
    }

    public void invalidate(){
        canvas.postInvalidate();
    }

    public void finalize(){
        moveThread.interrupt();
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
