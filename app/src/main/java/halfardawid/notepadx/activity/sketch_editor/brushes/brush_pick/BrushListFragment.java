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

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushTypes;

public class BrushListFragment extends Fragment {

    private BrushFlowManager brushFlowManager=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_brush_list,null);
        ListView listView = (ListView) v.findViewById(R.id.fbl_list);
        listView.setAdapter(new BrushListAdapter());
        listView.setOnItemClickListener(new LocalOnItemClickListener());
        return v;
    }

    public void setCallback(BrushFlowManager flow){
        brushFlowManager=flow;
    }

    private class LocalOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(brushFlowManager==null)return;
            brushFlowManager.goToDetail(BrushTypes.values()[(int) id]);
        }
    }
}
