package halfardawid.notepadx.activity.sketch_editor.brushes;

/**
 * Created by Dawid on 2017-10-17.
 */

public class SolidCircleBrush extends Brush {
    public SolidCircleBrush(float spacing, float radius) {
        super(spacing, radius);
    }

    @Override
    protected float smoothing(float distance) {
        return (distance<=radius)?1:0;
    }
}
