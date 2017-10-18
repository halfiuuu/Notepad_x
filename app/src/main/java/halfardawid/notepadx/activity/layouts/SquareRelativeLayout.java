package halfardawid.notepadx.activity.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Dawid on 2017-10-02.
 */

public class SquareRelativeLayout extends RelativeLayout {
    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override public void onMeasure(int x,int y){
        super.onMeasure(x,x);
    }
}
