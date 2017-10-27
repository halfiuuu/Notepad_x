package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.view.MotionEvent;

import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-27.
 */
abstract class SingleFingerMovement extends FingerMovement {
    protected int id;
    protected Vector2i last_pos;

    public SingleFingerMovement(MotionEvent me) {
        this.id = me.getActionIndex();
        this.last_pos = new Vector2i((int) me.getX(id), (int) me.getY(id));
    }
}
