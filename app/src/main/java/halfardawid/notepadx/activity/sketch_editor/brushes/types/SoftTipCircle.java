package halfardawid.notepadx.activity.sketch_editor.brushes.types;

import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;

/**
 * Created by Dawid on 2017-10-27.
 */

public class SoftTipCircle extends Brush {
    public SoftTipCircle(float spacing, float radius) {
        super(spacing, radius);
    }

    @Override
    protected float smoothing(float distance) {
        return (distance<=radius)?1F-distance/radius:0;
    }
}
