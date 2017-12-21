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

package halfardawid.notepadx.activity.sketch_editor.colorpalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

public class ColorPreview extends View {

    private AtomicInteger color= new AtomicInteger(Color.BLACK);

    public ColorPreview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int c){
        color.set(c);
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas c){
        c.drawColor(color.get());
    }
}
