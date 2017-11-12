package halfardawid.notepadx.activity.colorpalette;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.util.exceptions.InvalidContextException;

public class ColorSlider extends View {
    ColorPaletteActivity palette=null;

    public ColorSlider(Context context, @Nullable AttributeSet attrs) throws InvalidContextException {
        super(context, attrs);
        if(!(context instanceof ColorPaletteActivity))throw new InvalidContextException();
        palette= (ColorPaletteActivity) context;
    }



}
