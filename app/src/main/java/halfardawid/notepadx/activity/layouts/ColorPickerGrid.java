package halfardawid.notepadx.activity.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

import halfardawid.notepadx.R;

public class ColorPickerGrid extends GridView {
    public ColorPickerGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapter(new ColorPickerGridAdapter(context));
    }

}
