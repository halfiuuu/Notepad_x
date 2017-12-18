package halfardawid.notepadx.activity.sketch_editor.colorpalette.alpha;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.colorpalette.ColorSlider;
import halfardawid.notepadx.util.exceptions.InvalidContextException;

/**
 * Created by Dawid on 2017-11-12.
 */

public final class AlphaCanalSlider extends ColorSlider {
    public AlphaCanalSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
    }

    @Override
    protected float getCanalProcess(int c) {
        return Color.alpha(c)/getMaxCanal();
    }
    protected int applyProcessToColor(float process){
        int color=getColor();
        return Color.argb((int) (getMaxCanal()*process),Color.red(color),Color.green(color),Color.blue(color));
    }

    @Override
    protected void drawBackground(Canvas c){
        final int spacing=10;
        int mx=c.getWidth();
        int my=c.getHeight();
        int check=0;
        Paint[] paints=new Paint[]{new Paint(),new Paint()};
        paints[0].setColor(ResourcesCompat.getColor(getContext().getResources(), R.color.checkers_0,getContext().getTheme()));
        paints[1].setColor(ResourcesCompat.getColor(getContext().getResources(), R.color.checkers_1,getContext().getTheme()));
        for(int x=0;x<mx;x+=spacing,check++) {
            int incheck=check;
            for (int y = 0; y < my; y += spacing,incheck++)
                c.drawRect(x,y,x+spacing,y+spacing,paints[incheck%2]);
        }
    }
}
