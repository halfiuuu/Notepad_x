package halfardawid.notepadx.activity.colorpicker;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.ColorUtils;


public class CircleColorItem extends AppCompatTextView {
    private int id;
    private int value;

    public CircleColorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public void onMeasure(int x,int y){
        super.onMeasure(x,x);
    }

    public void initColor(int id){
        this.id=id;
        value=getResources().getIntArray(R.array.color_base)[id];
        setText(getResources().getStringArray(R.array.color_names)[id]);
        Drawable d=ContextCompat.getDrawable(this.getContext(),R.drawable.circle_button);
        d.setColorFilter(new PorterDuffColorFilter(value, PorterDuff.Mode.SRC_IN));
        setBackground(d);
        setTextColor(ResourcesCompat.getColor(this.getResources(), ColorUtils.calcContrast(value),getContext().getTheme()));
    }

}
