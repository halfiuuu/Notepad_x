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

package halfardawid.notepadx.activity.generic.layouts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import halfardawid.notepadx.R;

/**
 * Created by Dawid on 2017-11-07.
 */

public class SimpleProgressBar extends View implements Updatable {

    private float progress=0f;
    private Paint p=new Paint();
    private String alert=null;

    public SimpleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        p.setColor(getColorAccent());
    }

    public synchronized void updateWithValue(float f){
        progress=f;
        postInvalidate();
    }

    @Override
    public synchronized void onDraw(Canvas c){
        c.drawColor(Color.TRANSPARENT);
        c.drawRect(new RectF(0,0,c.getWidth()*progress,c.getHeight()),p);
    }

    private int getColorAccent() {
        return ResourcesCompat.getColor(getResources(), R.color.colorAccent,getContext().getTheme());
    }
}
