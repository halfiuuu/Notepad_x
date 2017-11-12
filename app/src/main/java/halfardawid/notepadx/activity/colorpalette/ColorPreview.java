package halfardawid.notepadx.activity.colorpalette;

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
