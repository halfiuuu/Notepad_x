package halfardawid.notepadx.activity.sketch_editor.brushes;

/**
 * Created by Dawid on 2017-10-17.
 */

public abstract class Brush {
    protected float spacing;
    protected float radius;
    abstract protected float smoothing(float distance);
    protected static final int expand=3;
    protected Brush(float spacing, float radius){
        this.spacing=spacing;
        this.radius=radius;
    }
}
