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

import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.activity.sketch_editor.brushes.SplatFailed;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-27.
 */
class FingersMoved extends FingerMovement {
    List<Vector2i> new_positions = new ArrayList<>();

    public FingersMoved(Fingers fingers, MotionEvent me) {
        super(fingers);
        for (int a = me.getActionIndex(); a < me.getPointerCount(); a++)
            new_positions.add(new Vector2i((int) me.getX(a), (int) me.getY(a)));
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < new_positions.size(); i++)
                fingers.fingerMoved(i, new_positions.get(i));
        } catch (SplatFailed splatFailed) {
            fingers.reportError(splatFailed);
        }
    }
}
