package halfardawid.notepadx.activity.generic.colorpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ColorPickerGrid extends GridView {

    public static final String TAG = "COLOR_PG";

    public ColorPickerGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapter(new ColorPickerGridAdapter(context));
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
                Log.w(TAG,id+" picked");
                Context c=getContext();
                if(c instanceof ColorPickReaction)
                    ((ColorPickReaction)c).applyColorPick(id);
            }
        });
    }

}
