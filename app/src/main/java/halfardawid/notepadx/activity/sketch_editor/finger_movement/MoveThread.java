package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.util.Log;
import android.view.MotionEvent;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Dawid on 2017-10-27.
 */

class MoveThread extends Thread{
    private static final String TAG="MOVE_THREAD";

    private Fingers fingers;
    BlockingQueue<FingerMovement> queue;

    private void logThread(){
        Log.d(TAG,Thread.currentThread()+" thread...");
    }

    public MoveThread(Fingers fingers) {
        this.fingers = fingers;
        queue=new LinkedBlockingQueue<>();
    }

    public void add(MotionEvent me){
        try {
            queue.put(getFingerMovement(me));
        } catch (InterruptedException e) {
            Log.d(TAG,"interrupted adding");
        }
    }

    @Override
    public void run() {
        try {
            for(;;){
                    queue.take().run();
                }
            }
        catch (InterruptedException e1) {
            Log.d(TAG,"interrupted");
        }
    }

    private FingerMovement getFingerMovement(MotionEvent me) {
        switch (me.getActionMasked()) {
            case ACTION_UP:
                return new LastFingerUp(fingers, me);
            case ACTION_POINTER_UP:
                return new FingerUp(fingers, me);
            case ACTION_MOVE:
                return new FingersMoved(fingers, me);
            case ACTION_DOWN:
                return new FirstFingerDown(fingers, me);
            case ACTION_POINTER_DOWN:
                return new FingerDown(fingers, me);
            default:
                return new WtfDidJustMove(me);
        }
    }
}
