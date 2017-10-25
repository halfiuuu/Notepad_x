package halfardawid.notepadx.activity.generic.layouts.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import halfardawid.notepadx.R;


public class CircleColorItem extends AppCompatTextView {
    private int id;
    private int value;

    public CircleColorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public void onMeasure(int x,int y){
        super.onMeasure(x,x);
    }

    public void initColor(Context con, int id){
        this.id=id;
        value=con.getResources().getIntArray(R.array.color_base)[id];
        setText(con.getResources().getStringArray(R.array.color_names)[id]);
        Drawable d=ContextCompat.getDrawable(con,R.drawable.circle_button);
        d.setColorFilter(new PorterDuffColorFilter(value, PorterDuff.Mode.SRC_IN));
        setBackground(d);
    }
}
