package halfardawid.notepadx.activity.colorpalette;

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
