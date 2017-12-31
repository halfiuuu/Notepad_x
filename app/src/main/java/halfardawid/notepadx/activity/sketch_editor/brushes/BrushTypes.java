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

package halfardawid.notepadx.activity.sketch_editor.brushes;

import halfardawid.notepadx.activity.sketch_editor.brushes.types.BubbleBrush;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SoftTipCircle;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SolidCircleBrush;

//Lists out possible brushes for BrushesActivity or whatever i've called it.
public enum BrushTypes {
    SOFT_TIP_CIRCLE(SoftTipCircle.class),
    SOLID_CIRCLE_BRUSH(SolidCircleBrush.class),
    BUBBLE_BRUSH(BubbleBrush.class);

    private final Class<? extends Brush> type;
    BrushTypes(Class<? extends Brush> type) {
        this.type = type;
    }
    public Class<? extends Brush> getType(){
        return type;
    }
}
