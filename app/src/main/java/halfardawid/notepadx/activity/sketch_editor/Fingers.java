package halfardawid.notepadx.activity.sketch_editor;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import static android.view.MotionEvent.*;

import java.util.ArrayList;
import java.util.List;
import halfardawid.notepadx.util.vectors.Vector2i;

public class Fingers {
    private static final String TAG = "FINGERS";

    private List<Finger> fingers=new ArrayList<>();

    private SmartBitmap bitmap=null;
    private SketchMode mode=null;

    private int[] color_fun={
            Color.BLUE,Color.RED, Color.GREEN, Color.MAGENTA,Color.YELLOW
    };

    public Fingers(SmartBitmap image, SketchMode mode){bitmap=image;this.mode=mode;}

    public void handleEvent(MotionEvent me) {
        Log.d(TAG, "ev>>" + me);
        switch(me.getAction()){
            case ACTION_UP:
                Log.d(TAG,"Last finger has been lifted");
                break;
            case ACTION_POINTER_UP:
                Log.d(TAG,"Finger that's currently "+me.getActionIndex()+" has been lifted");
                break;
            case ACTION_MOVE:
                for(int a=me.getActionIndex();a<me.getPointerCount();a++) {
                    Vector2i v=new Vector2i((int) me.getX(a), (int) me.getY(a));
                    Log.d(TAG, "Finger " + a + " move to " + v);
                    /*for(int x=0;x<5;x++)
                        for(int y=0;y<5;y++) {
                            Vector2i nv=new Vector2i(v);
                            nv.add(new Vector2i(x,y));
                            bitmap.drawPixel(nv, color_fun[a % color_fun.length]);
                        }*/
                }
                break;
            case ACTION_DOWN:
                Log.d(TAG,"First finger touches the screen");
                break;
            case ACTION_POINTER_DOWN:
                Log.d(TAG,"Another finger touches, it's id is"+me.getPointerCount());
                break;
        }
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
