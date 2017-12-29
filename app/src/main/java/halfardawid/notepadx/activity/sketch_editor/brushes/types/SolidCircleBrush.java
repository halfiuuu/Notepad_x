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
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushType;
import halfardawid.notepadx.util.vectors.Vector2i;

@BrushType(name = R.string.solid_circle)
public class SolidCircleBrush extends Brush {
    public SolidCircleBrush(){super();}
    public SolidCircleBrush(float spacing, float radius) {
        super(spacing, radius);
    }

    @Override
    protected float smoothing(Vector2i a) {
        float distance=a.pythagoras();
        return (distance<=radius)?1:0;
    }
}
