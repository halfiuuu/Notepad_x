package halfardawid.notepadx.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Dawid on 2017-10-02.
 */
@Deprecated
public class SquareView extends GridView {
    public SquareView(Context context) {
        super(context);
    }
    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /*@Override public void onMeasure(int x, int y){
        super.onMeasure(x,x);
    }*/
}
