package halfardawid.notepadx.activity.sketch_editor;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.util.vectors.Vector2i;

public class Fingers {
    private static final String TAG = "FINGERS";
    List<Finger> fingers=new ArrayList<>();
    public Fingers(){};
    public void handleEvent(MotionEvent me) {
        Log.d(TAG,"ev>>"+me);
    }
}

/*
 public void move(Vector2i v) {
        Log.d(TAG, action + " " + pos);
        if (action == MotionEvent.ACTION_DOWN) {
            move_vector = new Vector2i(offset);
            move_vector.sub(pos);
        } else if (move_vector == null) {
            Log.wtf(TAG, "logic error for move event...");
            return;
        }
        offset.copy(move_vector);
        offset.add(pos);
        if (action == MotionEvent.ACTION_UP) move_vector = null;
    }
 */
