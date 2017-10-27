package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Dawid on 2017-10-27.
 */
class WtfDidJustMove extends FingerMovement {
    public WtfDidJustMove(MotionEvent me) {
        Log.wtf(Fingers.TAG, "Yea... What is this? " + me);
    }

    @Override
    public void run() {
    }
}
