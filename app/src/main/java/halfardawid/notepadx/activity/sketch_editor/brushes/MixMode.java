package halfardawid.notepadx.activity.sketch_editor.brushes;

import android.support.annotation.Nullable;

import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-12-16.
 */

public interface MixMode {
    @Nullable
    Integer mixColors(float smoothing, final int x, final int y, final Vector2i pos, int[] bitmap, int index, int a, int r, int g, int b, int color);
}
