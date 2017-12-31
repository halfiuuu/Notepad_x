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
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushTypes;

public class BrushesActivity extends AppCompatActivity implements BrushFlowManager{

    public static final int CODE = 525;

    @IdRes private static final int master=R.id.ab_master;
    @IdRes private static final int detail=R.id.ab_detail;
    boolean doubleScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brushes);
        doubleScreen=(findViewById(detail)!=null);
        goToList();
    }

    @Override
    public void goToList() {
        BrushListFragment frag=new BrushListFragment();
        frag.setCallback(this);
        getFragmentManager().beginTransaction().replace(master,frag).commit();
    }

    @Override
    public void goToDetail(BrushTypes brushType) {
        BrushDetailFragment frag=new BrushDetailFragment();
        frag.setSelected(brushType);
        getFragmentManager().beginTransaction().replace(doubleScreen?detail:master,frag).commit();
    }
}
