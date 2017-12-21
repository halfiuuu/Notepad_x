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

package halfardawid.notepadx.activity.sketch_editor.colorpalette;

import android.view.View;

/**
 * Created by Dawid on 2017-11-12.
 */

interface ColorSliderResponseInterface {
    void clearRefreshers();
    void addToRefresher(View colorSlider);
    void refreshAll();
    int getColor();
    void setColor(int i);
}
