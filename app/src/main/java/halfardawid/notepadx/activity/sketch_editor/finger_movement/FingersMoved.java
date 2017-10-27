package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-27.
 */
class FingersMoved extends FingerMovement {
    private Fingers fingers;
    List<Vector2i> new_positions = new ArrayList<>();

    public FingersMoved(Fingers fingers, MotionEvent me) {
        this.fingers = fingers;
        for (int a = me.getActionIndex(); a < me.getPointerCount(); a++)
            new_positions.add(new Vector2i((int) me.getX(a), (int) me.getY(a)));
    }

    @Override
    public void run() {
        for (int i = 0; i < new_positions.size(); i++)
            fingers.fingerMoved(i, new_positions.get(i));
    }
}
