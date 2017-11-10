package halfardawid.notepadx.activity.generic.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import halfardawid.notepadx.R;

/**
 * Created by Dawid on 2017-11-07.
 */

public class SimpleProgressBar extends View implements Updatable {

    private float progress=0f;
    private Paint p=new Paint();

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
