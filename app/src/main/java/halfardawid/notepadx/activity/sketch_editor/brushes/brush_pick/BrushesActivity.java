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

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushTypes;

public class BrushesActivity extends AppCompatActivity implements BrushFlowManager{

    public static final int CODE = 525;

    @IdRes private static final int master=R.id.ab_master;
    @IdRes private static final int detail=R.id.ab_detail;
    public static final String BRUSH = "BRUSH";
    boolean doubleScreen;
    boolean finishOnHome;
    private BrushTypes selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brushes);
        doubleScreen=(findViewById(detail)!=null);
        selected=BrushTypes.SOFT_TIP_CIRCLE;
        goToList();
        if(doubleScreen)goToDetail(selected);
    }

    @Override
    public void goToList() {
        BrushListFragment frag=new BrushListFragment();
        frag.setCallback(this);
        if(selected!=null)frag.setSelected(selected);
        getFragmentManager().beginTransaction().replace(master,frag).commit();
        finishOnHome=true;
    }

    @Override
    public void goToDetail(BrushTypes brushType) {
        selected = brushType;
        BrushDetailFragment frag=new BrushDetailFragment();
        frag.setCallback(this);
        frag.setSelected(brushType);
        getFragmentManager().beginTransaction().replace(doubleScreen?detail:master,frag).commit();
        finishOnHome=false;
    }

    @Override
    public void returnResult(Brush brush) {
        Intent intent=new Intent();
        intent.putExtra(BRUSH, brush);
        setResult(RESULT_OK,intent);
        quit();
    }

    public void onHomeClick(View v){
        if(finishOnHome||doubleScreen)
            quit();
        else
            goToList();
    }

    private void quit() {
        finish();
    }
}
