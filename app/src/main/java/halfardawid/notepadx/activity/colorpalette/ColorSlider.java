package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.util.exceptions.InvalidContextException;

public abstract class ColorSlider extends View {
    ColorPaletteActivity palette=null;

    protected float process=0;

    public ColorSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
        if(!(context instanceof ColorPaletteActivity))throw new InvalidContextException();
        palette= (ColorPaletteActivity) context;
        palette.addToRefresher(this);
        guessProcess();
    }

    protected int getColor(){
        return palette.getColor();
    }

    protected void applyColor(){
        palette.setColor(applyProcessToColor());
    }

    protected abstract int applyProcessToColor();

    protected final void guessProcess() {
        process=getCanalProcess(getColor())/getMaxCanal();
    }

    protected abstract float getCanalProcess(int c);

    protected float getMaxCanal() {
        return 256;
    }
}
