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

import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushParameter;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushTypes;

public class BrushDetailFragment extends Fragment {
    public static final int MAX = 1000;
    private BrushTypes selected;
    private List<ParameterReferences> parameterReferencesList=null;
    private LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brush_detail, null);
        layout = (LinearLayout) view.findViewById(R.id.fbd_content);
        populateParameterList();
        return view;
    }

    private void populateParameterList() {
        if(selected==null)return;
        parameterReferencesList=new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        Field[] fields = selected.getType().getFields();

        for (Field field:fields) {
            BrushParameter param = field.getAnnotation(BrushParameter.class);
            if(param==null)continue;
            ParameterReferences references = new ParameterReferences(inflater);
            parameterReferencesList.add(references);
            setupText(param, references);
            setupEdit(param, references);
            setupSeek(param, references);
        }
    }

    private void setupText(BrushParameter param, ParameterReferences references) {
        references.getTextView().setText(param.name());
    }

    private void setupEdit(BrushParameter param, final ParameterReferences references) {
        references.getEdit().setHint(param.name());
        references.getEdit().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                references.updateSeek(Float.parseFloat(v.getText().toString()));
                return false;
            }
        });
    }

    private void setupSeek(final BrushParameter param, final ParameterReferences references) {
        references.getSeek().setMax(MAX);
        final float range=param.max()-param.min();
        references.getSeek().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser)return;
                Log.d("seek","progress:"+progress);
                references.updateText((progress*range)/MAX+param.min());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setSelected(BrushTypes selected) {
        this.selected = selected;
    }

    private class ParameterReferences{

        private final TextView textView;
        private final EditText edit;
        private final SeekBar seek;
        private final View view;

        ParameterReferences(LayoutInflater inflater){
            view = inflater.inflate(R.layout.fragment_brush_detail_element, null);
            textView = (TextView) view.findViewById(R.id.fbde_text);
            edit = (EditText) view.findViewById(R.id.fbde_edit);
            seek = (SeekBar) view.findViewById(R.id.fbde_slide);
            layout.addView(view);
        }

        public TextView getTextView() {
            return textView;
        }

        public EditText getEdit() {
            return edit;
        }

        public SeekBar getSeek() {
            return seek;
        }

        public View getRoot() {
            return view;
        }

        public void updateSeek(float v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                seek.setProgress((int) (seek.getMax()*v),true);
            else
                seek.setProgress((int) (seek.getMax()*v));
        }

        public void updateText(float v) {
            v=((float)(int)(v*100))/100;
            edit.setText(String.format("%s", v));
        }
    }
}

