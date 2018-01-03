/*
 * Copyright (c) 2018 anno Domini.
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

@BrushType(name = R.string.square_brush)
public class SquareBrush extends Brush {
    @Override
    protected float smoothing(Vector2i position) {
        Vector2i absolute=new Vector2i(position).abs();
        return (absolute.x<=radius&&absolute.y<=radius)?1:0;
    }
}
