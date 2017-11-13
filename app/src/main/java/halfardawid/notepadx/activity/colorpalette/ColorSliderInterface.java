package halfardawid.notepadx.activity.colorpalette;

import android.view.View;

/**
 * Created by Dawid on 2017-11-12.
 */

interface ColorSliderInterface {
    void addToRefresher(View colorSlider);

    int getColor();

    void setColor(int i);
}
