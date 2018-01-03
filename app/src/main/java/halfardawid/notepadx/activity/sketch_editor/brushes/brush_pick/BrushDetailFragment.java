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
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushParameter;
import halfardawid.notepadx.activity.sketch_editor.brushes.BrushTypes;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.BubbleBrush;

public class BrushDetailFragment extends BrushFlowManagedFragment {
    public static final int MAX = 1000;
    public static final String TAG = "BD_FRAG";
    private BrushTypes selected;
    private Brush template;
    private List<ParameterReferences> parameterReferencesList=null;
    private LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brush_detail, null);
        layout = (LinearLayout) view.findViewById(R.id.fbd_content);
        view.findViewById(R.id.fbd_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyBrush();
            }
        });
        populateParameterList();
        return view;
    }

    public void applyBrush(){
        //Toast.makeText(layout.getContext(), bundle.toString(), Toast.LENGTH_SHORT).show();
        if(validateFields())
            brushFlowManager.returnResult(parseBrush());
    }

    private boolean validateFields() {
        if(parameterReferencesList==null)return false;
        boolean valid=true;
        for(ParameterReferences reference:parameterReferencesList)
            valid&=isValid(reference);
        return valid;
    }

    private boolean isValid(ParameterReferences reference) {
        String text = reference.getValue();
        if(text.isEmpty()){
            reference.setError(R.string.cant_be_empty);
            return false;
        }
        float value;
        try {
            value = Float.parseFloat(text);
        }catch (NumberFormatException e){
            reference.setError(R.string.not_a_number);
            return false;
        }
        BrushParameter parameter = reference.getParameter();
        if(value>parameter.max()){
            reference.setError(R.string.value_too_big);
            return false;
        }
        if(value<parameter.min()){
            reference.setError(R.string.value_too_small);
            return false;
        }
        return true;
    }

    private void populateParameterList() {
        if(selected==null)return;
        parameterReferencesList=new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        Field[] fields = selected.getType().getFields();

        for (Field field:fields) {
            BrushParameter param = field.getAnnotation(BrushParameter.class);
            if(param==null)continue;
            ParameterReferences references = new ParameterReferences(param,field,inflater);
            parameterReferencesList.add(references);
            setupText(param, references);
            setupEdit(param, references);
            setupSeek(param, references);
            references.loadFromTemplate();
        }
    }

    private Brush parseBrush(){
        try {
            Brush brush =selected.getInstance();
            for(ParameterReferences reference:parameterReferencesList){
                Field field=reference.getField();
                field.set(brush, Float.parseFloat(reference.getValue()));
            }
            //Log.d(TAG,"Parsed: "+brush);
            return brush;
        } catch (Exception e) {
            Log.wtf(TAG,
                    "Brush creation terribly failed, " +
                            "someone didn't ensure constructor(void) for brush...",e);
            return null;
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
                try {
                    references.updateSeek(Float.parseFloat(v.getText().toString()));
                }catch (NumberFormatException e){
                    //Ignore for now I guess...
                }
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
        try {
            template=selected.getInstance();
        } catch (Exception e) {
            Log.wtf(TAG,"Oh hell...",e);
        }
    }

    public void copyParameters(Brush latestBrush) {
        if(template==null||latestBrush==null)return;
        Field[] latest_fields = latestBrush.getClass().getFields();
        Field[] template_fields = template.getClass().getFields();
        for(Field latest_field: latest_fields){
            BrushParameter annotation = latest_field.getAnnotation(BrushParameter.class);
            if(annotation==null)continue;
            for(Field template_field:template_fields){
                BrushParameter target_annotation = template_field.getAnnotation(BrushParameter.class);
                if(target_annotation==null)continue;
                if(template_field.getName().equals(latest_field.getName()) &&
                        template_field.getType().equals(latest_field.getType())){
                    try {
                        template_field.set(template,
                                latest_field.get(latestBrush));
                    } catch (IllegalAccessException e) {
                        Log.wtf(TAG,"Access denied, good luck",e);
                    }
                }
            }
        }
    }

    public Brush getBrushIfYouCan() {
        if(validateFields())
            return parseBrush();
        return null;
    }

    private class ParameterReferences{

        private final TextView textView;
        private final EditText edit;
        private final SeekBar seek;
        private final View view;
        private final Field field;
        private final BrushParameter parameter;

        ParameterReferences(BrushParameter param, Field field, LayoutInflater inflater){
            parameter=param;
            view = inflater.inflate(R.layout.fragment_brush_detail_element, null);
            textView = (TextView) view.findViewById(R.id.fbde_text);
            edit = (EditText) view.findViewById(R.id.fbde_edit);
            seek = (SeekBar) view.findViewById(R.id.fbde_slide);
            this.field=field;
            layout.addView(view);
        }

        public void loadFromTemplate() {
            if(template!=null){
                try {
                    Float o = (Float) field.get(template);
                    if(o==null)return;
                    updateText(o);
                    updateSeek(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        TextView getTextView() {
            return textView;
        }

        public EditText getEdit() {
            return edit;
        }

        SeekBar getSeek() {
            return seek;
        }

        public View getRoot() {
            return view;
        }

        public BrushParameter getParameter(){
            return parameter;
        }

        void updateSeek(float v) {
            float val = seek.getMax() * ((v-parameter.min())/(parameter.max()-parameter.min()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                seek.setProgress((int) val,true);
            else
                seek.setProgress((int) val);
        }

        void updateText(float v) {
            v=((float)(int)(v*100))/100;
            edit.setText(String.format("%s", v));
        }

        Field getField() {
            return field;
        }

        public String getValue() {
            return edit.getText().toString();
        }

        public void setError(@StringRes int error) {
            edit.setError(layout.getContext().getString(error));
        }
    }
}

