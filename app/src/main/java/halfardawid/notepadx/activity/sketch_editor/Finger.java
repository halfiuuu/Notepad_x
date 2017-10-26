package halfardawid.notepadx.activity.sketch_editor;

import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-26.
 */

public class Finger {
    List<Vector2i> history=new ArrayList<>();
    public Finger(Vector2i down){
        history.add(down);
    }
}
