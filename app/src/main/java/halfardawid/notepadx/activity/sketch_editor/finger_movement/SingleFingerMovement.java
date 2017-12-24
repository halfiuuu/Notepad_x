/*
 * Copyright (c) 2017 anno Domini.
 *
 * Code below is a part of
 * https://github.com/halfiuuu/Notepad_x
 * available for use under the
 * GNU Affero General Public License v3.0
 * as stated in
 * https://github.com/halfiuuu/Notepad_x/blob/master/LICENSE
 *
 * Created by Dawid Halfar
 * contact possible via halfardawid@gmail.com
 *
 * I'm not sure what else this thing should state... Whatever.
 */

package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.view.MotionEvent;

import halfardawid.notepadx.activity.sketch_editor.brushes.SplatFailed;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-27.
 */
abstract class SingleFingerMovement extends FingerMovement {
    protected int id;
    protected Vector2i last_pos;

    public SingleFingerMovement(Fingers fingers, MotionEvent me) {
        super(fingers);
        this.id = me.getActionIndex();
        this.last_pos = new Vector2i((int) me.getX(id), (int) me.getY(id));
    }

    public void fingerMoved() {
        try {
            fingers.fingerMoved(id, last_pos);
        } catch (SplatFailed splatFailed) {
            fingers.reportError(splatFailed);
        }
    }
}
