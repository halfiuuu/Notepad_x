package halfardawid.notepadx.activity.sketch_editor.brushes;

import android.graphics.Color;
import android.support.annotation.Nullable;

import halfardawid.notepadx.activity.sketch_editor.brushes.painting_modes.EraserMode;
import halfardawid.notepadx.activity.sketch_editor.brushes.painting_modes.NormalMixMode;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-12-16.
 */

public enum PAINTING_MODE {
    NORMAL(new NormalMixMode()),
    ERASE(new EraserMode());

    private MixMode mode;
    PAINTING_MODE(MixMode arg0) {
        mode=arg0;
    }
    @Nullable
    public Integer mix(float smoothing, int x, int y, Vector2i pos, int[] bitmap, int index, int a, int r, int g, int b, int color){
        return mode.mixColors(smoothing,x,y,pos,bitmap,index,a,r,g,b,color);
    }
}
