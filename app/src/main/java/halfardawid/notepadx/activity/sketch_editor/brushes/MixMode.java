/*
 * Copyright (c) 2017 anno Domini.
 *
 * Code below is a part of
 * https://github.com/halfiuuu/Notepad_x
 * available for use under the
 * GNU Affero General Public License v3.0
 * as stated in
 * https://github.com/halfiuuu/Notepad_x/blob/master/LICENSE
 *
 * Created by Dawid Halfar
 * contact possible via halfardawid@gmail.com
 *
 * I'm not sure what else this thing should state... Whatever.
 */

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
