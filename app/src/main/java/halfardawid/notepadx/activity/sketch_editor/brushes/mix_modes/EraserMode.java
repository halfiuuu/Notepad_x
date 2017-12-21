package halfardawid.notepadx.activity.sketch_editor.brushes.mix_modes;

import android.graphics.Color;
import android.support.annotation.Nullable;

import halfardawid.notepadx.activity.sketch_editor.brushes.MixMode;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-12-16.
 */

public class EraserMode implements MixMode {
    @Nullable
    @Override
    public Integer mixColors(float smoothing, int x, int y, Vector2i pos, int[] bitmap, int index, int a, int r, int g, int b, int color) {
        if(smoothing ==1&&a>253)
            return Color.TRANSPARENT;
        if(smoothing==0)
            return null;

        int alpha=(int)(a*smoothing);
        int base_color=bitmap[index];
        int base_alpha= Color.alpha(base_color);
        int base_r=Color.red(base_color),base_g=Color.green(base_color),base_b=Color.blue(base_color);
        return Color.argb(
                Math.max((base_alpha*(255-alpha))>>8,0),
                base_r,base_g,base_b

        );
    }
}
