package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.util.Log;
import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Dawid on 2017-10-27.
 */
class MoveThread extends Thread {
    private Fingers fingers;
    FingerMovement fm;

    private void logThread(){
        Log.d("MoveThread!",Thread.currentThread()+" thread...");
    }

    public MoveThread(Fingers fingers, Fingers f, MotionEvent fm) {
        logThread();
        this.fingers = fingers;
        this.fm = getFingerMovement(f, fm);
    }

    @Override
    public void run() {
        logThread();
        synchronized (fingers.lock) {
            logThread();
            fm.run();
            fingers.invalidate();
        }
    }

    private static FingerMovement getFingerMovement(Fingers t, MotionEvent me) {
        switch (me.getActionMasked()) {
            case ACTION_UP:
                return new LastFingerUp(t, me);
            case ACTION_POINTER_UP:
                return new FingerUp(t, me);
            case ACTION_MOVE:
                return new FingersMoved(t, me);
            case ACTION_DOWN:
                return new FirstFingerDown(t, me);
            case ACTION_POINTER_DOWN:
                return new FingerDown(t, me);
            default:
                return new WtfDidJustMove(me);
        }
    }
}
