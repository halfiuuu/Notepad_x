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

import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import halfardawid.notepadx.activity.sketch_editor.brushes.types.BubbleBrush;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.RectBrush;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SoftTipCircle;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SolidCircleBrush;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SquareBrush;

//Lists out possible brushes for BrushesActivity or whatever i've called it.
public enum BrushTypes {
    SOFT_TIP_CIRCLE(SoftTipCircle.class),
    SOLID_CIRCLE_BRUSH(SolidCircleBrush.class),
    BUBBLE_BRUSH(BubbleBrush.class),
    SQUARE_BRUSH(SquareBrush.class),
    RECT_BRUSH(RectBrush.class);

    private final Class<? extends Brush> type;
    BrushTypes(Class<? extends Brush> type) {
        this.type = type;
    }
    public Class<? extends Brush> getType(){
        return type;
    }
    public Brush getInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return type.getConstructor().newInstance();
    }

    public static BrushTypes getEnum(Brush latestBrush) {
        if(latestBrush==null)return null;
        for(BrushTypes type:values())
            if(type.getType().equals(latestBrush.getClass()))return type;
        Log.wtf("BrushTypes","Non registered brush type! Wtf, "+latestBrush);
        return null;
    }
}
