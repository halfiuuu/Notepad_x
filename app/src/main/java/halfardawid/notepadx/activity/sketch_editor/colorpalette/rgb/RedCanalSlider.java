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

package halfardawid.notepadx.activity.sketch_editor.colorpalette.rgb;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import halfardawid.notepadx.activity.sketch_editor.colorpalette.ColorSlider;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

/**
 * Created by Dawid on 2017-11-12.
 */

public final class RedCanalSlider extends ColorSlider {
    public RedCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.red(c)/getMaxCanal();
    }
    protected int applyProcessToColor(float process){
        int color=getColor();
        return Color.argb(Color.alpha(color),(int) (getMaxCanal()*process),Color.green(color),Color.blue(color));
    }
}
