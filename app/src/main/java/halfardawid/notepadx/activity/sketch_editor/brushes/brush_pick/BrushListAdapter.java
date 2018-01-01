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

package halfardawid.notepadx.activity.sketch_editor.brushes.brush_pick;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushType;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushTypes;

class BrushListAdapter implements ListAdapter {

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return BrushTypes.values().length;
    }

    @Override
    public Object getItem(int position) {
        return BrushTypes.values()[position].getType();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_brush_list_element,null);
        }
        Object object=getItem(position);
        TextView tv = (TextView) convertView;
        if(object instanceof Class<?>){
            Class<?> c= (Class<?>) object;
            tv.setText(c.getAnnotation(BrushType.class).name());
        }else {
            tv.setText(R.string.sorry_error);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount()==0;
    }
}
