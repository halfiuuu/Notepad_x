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

package halfardawid.notepadx.activity.sketch_editor.brushes.types;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushParameter;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushType;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * This is pretty much just a test
 */

@BrushType(name = R.string.bubble_brush)
public class BubbleBrush extends Brush{
    @BrushParameter(name=R.string.hollow, min=0, max=1, percent=true)
    public Float hollow=.5f;

    public BubbleBrush(float spacing, float radius) {
        super(spacing, radius);
    }

    @Override
    protected float smoothing(Vector2i position) {
        float distance=position.pythagoras();
        return (distance<=radius&&distance>= radius*hollow)?1:0;
    }
}
