package halfardawid.notepadx.activity.sketch_editor.brushes.painting_modes;

import android.graphics.Color;
import android.support.annotation.Nullable;

import halfardawid.notepadx.activity.sketch_editor.brushes.MixMode;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-12-16.
 */

public final class NormalMixMode implements MixMode {
    @Nullable
    @Override
    public Integer mixColors(float smoothing, int x, int y, Vector2i pos, int[] bitmap, int index, int a, int r, int g, int b, int color) {
        if(smoothing ==1&&a>254)
            return color;
        if(smoothing==0)
            return null;

        int alpha=(int)(a*smoothing);
        int base_color=bitmap[index];
        int base_alpha= Color.alpha(base_color);
        int wa = alpha + base_alpha;
        if(wa==0)return null;
        int base_r=Color.red(base_color),base_g=Color.green(base_color),base_b=Color.blue(base_color);
        int inverted_base_alpha = 255 - base_alpha;
        return Color.argb(
                Math.min(base_alpha+((inverted_base_alpha*alpha)>>8),255),
                ((r*alpha)+(base_r*base_alpha))/ wa,
                ((g*alpha)+(base_g*base_alpha))/ wa,
                ((b*alpha)+(base_b*base_alpha))/ wa

        );
    }
}
