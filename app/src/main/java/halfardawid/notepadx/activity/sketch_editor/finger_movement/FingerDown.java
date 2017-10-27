package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Dawid on 2017-10-27.
 */
class FingerDown extends SingleFingerMovement {
    private Fingers fingers;

    public FingerDown(Fingers fingers, MotionEvent me) {
        super(me);
        this.fingers = fingers;
    }

    @Override
    public void run() {
        fingers.add(new Finger(last_pos));
        fingers.fingerMoved(id, last_pos);
        if(Fingers.DEBUG_SPAM)Log.d(Fingers.TAG, "Another finger, now with id " + id + " touched the screen for first time on " + last_pos);
    }
}
