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

import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-26.
 */

public class Finger {
    private List<Vector2i> history=new ArrayList<>();
    private float distance =0;

    public Finger(Vector2i down){
        history.add(down);
    }

    public Vector2i lastPosition() {
        return history.get(history.size()-1);
    }

    public void addNewPoint(Vector2i pos) {
        history.add(pos);
    }

    public float latestDistance() {
        return distance;
    }


    public void newDistance(float arg0) {
        distance =arg0;
    }

    public int timesTouched() {
        return history.size();
    }
}
