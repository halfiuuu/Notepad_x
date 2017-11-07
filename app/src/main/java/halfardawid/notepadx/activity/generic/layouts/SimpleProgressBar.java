package halfardawid.notepadx.activity.generic.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import halfardawid.notepadx.R;

/**
 * Created by Dawid on 2017-11-07.
 */

public class SimpleProgressBar extends View {
    public SimpleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas c){
        c.drawColor(ResourcesCompat.getColor(getResources(),R.color.colorAccent,getContext().getTheme()));
    }
}
