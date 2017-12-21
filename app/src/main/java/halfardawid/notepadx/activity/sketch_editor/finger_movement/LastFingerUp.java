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

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Dawid on 2017-10-27.
 */
class LastFingerUp extends SingleFingerMovement {
    private Fingers fingers;

    public LastFingerUp(Fingers fingers, MotionEvent me) {
        super(me);
        this.fingers = fingers;
    }

    @Override
    public void run() {
        if(Fingers.DEBUG_SPAM)Log.d(Fingers.TAG, "Last finger has been lifted from " + last_pos);
        fingers.fingerMoved(id, last_pos);
        fingers.clear();
    }
}
