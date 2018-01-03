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
import android.widget.Toast;

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
    private Brush latestBrush;
    private BrushListFragment brushListFragment;
    private BrushDetailFragment brushDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brushes);
        doubleScreen=(findViewById(detail)!=null);
        latestBrush = (Brush) getIntent().getSerializableExtra(BRUSH);
        if(latestBrush!=null)
            selected=BrushTypes.getEnum(latestBrush);
        goToList();
        if(doubleScreen)goToDetail(selected);
    }

    @Override
    public synchronized void goToList() {
        brushListFragment = new BrushListFragment();
        updateBrush();
        brushListFragment.setSelected(selected);
        brushListFragment.setCallback(this);
        getFragmentManager().beginTransaction().replace(master, brushListFragment).commit();
        finishOnHome=true;
    }

    private void updateBrush() {
        if(brushDetailFragment==null)return;
        Brush temp = brushDetailFragment.getBrushIfYouCan();
        if(temp!=null) {
            latestBrush = temp;
            selected = BrushTypes.getEnum(temp);
        }
    }

    @Override
    public synchronized void goToDetail(BrushTypes brushType) {
        if(doubleScreen){
            updateBrush();
        }
        selected = brushType;
        brushDetailFragment = new BrushDetailFragment();
        brushDetailFragment.setCallback(this);
        brushDetailFragment.setSelected(brushType);
        if(latestBrush!=null)
            brushDetailFragment.copyParameters(latestBrush);
        getFragmentManager().beginTransaction().replace(doubleScreen?detail:master, brushDetailFragment).commit();
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
