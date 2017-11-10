package halfardawid.notepadx.activity.colorpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import halfardawid.notepadx.R;


/**
 * Created by Dawid on 2017-10-23.
 */

public class ColorPickerGridAdapter extends BaseAdapter{

    private static final String TAG = "COLOR_PGA";
    final Context context;

    public ColorPickerGridAdapter(Context con){
        context=con;
    }

    @Override
    public int getCount() {
        return context.getResources().getStringArray(R.array.color_names).length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View conv, ViewGroup parent) {
        if(conv==null)conv= LayoutInflater.from(context).inflate(R.layout.colorpicker_circle,null);
        CircleColorItem ccb = (CircleColorItem) conv.findViewById(R.id.cp_button);
        ccb.initColor(context,position);
        //ccb.setOnClickListener(new ColorOnClickListener(position));
        return conv;
    }
}
