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

package halfardawid.notepadx.activity.generic.colorpicker;

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
        ccb.initColor(position);
        //ccb.setOnClickListener(new ColorOnClickListener(position));
        return conv;
    }
}
