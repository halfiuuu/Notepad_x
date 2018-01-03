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

import java.util.Objects;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushParameter;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushType;
import halfardawid.notepadx.util.vectors.Vector2i;

@BrushType(name = R.string.rect_brush)
public class RectBrush extends Brush {

    @BrushParameter(name=R.string.ratio, min=-0.99f, max=0.99f, percent=true)
    Float ratio=0f;

    private transient Float latest_ratio=null;
    private transient Vector2i smoothing_ratio_radius=null;
    @Override
    protected float smoothing(Vector2i position) {
        if(latest_ratio==null|| !Objects.equals(latest_ratio, ratio)){
            latest_ratio=ratio;
            smoothing_ratio_radius =new Vector2i((int)(float)radius);
            boolean top=true;
            float inner_ratio=ratio;
            if(inner_ratio<0) {
                top = false;
                inner_ratio*=-1;
            }
            inner_ratio=1f-inner_ratio;
            if(top)
                smoothing_ratio_radius.x*=inner_ratio;
            else
                smoothing_ratio_radius.y*=inner_ratio;
        }
        Vector2i absolute=new Vector2i(position).abs();
        return (absolute.x<= smoothing_ratio_radius.x&&absolute.y<= smoothing_ratio_radius.y)?1:0;
    }
}
