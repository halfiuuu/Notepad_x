/*
 * Copyright (c) 2018 anno Domini.
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

package halfardawid.notepadx.activity.check_list_editor;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.note.types.CheckListNote;

public class CheckListEntryFragment extends Fragment implements ActionMode.Callback{

    public static final String TAG = "CLE_FRAG";
    private CheckListNote note;
    private int index;
    private ViewSwitcher switcher;
    private TextView text_view;
    private CheckBox check_box;
    private EditText edit_text;
    private CheckListEntryCallbacks callbacks;
    private boolean views_initialized;

    public void initialize(CheckListNote note, int index, CheckListEntryCallbacks callbacks){
        this.note = note;
        this.index = index;
        this.callbacks=callbacks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View master = inflater.inflate(R.layout.fragment_list_entry, null);
        switcher = (ViewSwitcher) master.findViewById(R.id.fle_switcher);
        edit_text = (EditText) switcher.findViewById(R.id.flee_edit_text);
        check_box = (CheckBox) switcher.findViewById(R.id.flep_check_box);
        text_view = (TextView) switcher.findViewById(R.id.flep_text_view);
        views_initialized=true;
        refresh();
        initClicks();
        return master;
    }

    private void initClicks() {
        text_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return tryToShowEdit(v.getContext());
            }

        });
        check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.getEntry(index).checked=check_box.isChecked();
            }
        });
    }

    /**
     * This should be used to invoke edit on fragment, only this!
     * @param c
     * @return Returns true if it managed to set as currently editing, otherwise false.
     */
    public boolean tryToShowEdit(Context c) {
        if(!canShowEdit()){
            Toast.makeText(c, R.string.already_editing, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(switcher==null) {
            return false;
        }
        setAsShowing();
        return true;
    }

    private boolean canShowEdit() {
        return !callbacks.alreadyShowingEdit();
    }

    private void setAsShowing() {
        callbacks.setShowingEdit(this);
        switcher.showNext();
    }

    private void clearShowing(){
        switcher.showPrevious();
        callbacks.clearShowing();
    }

    public void refresh() {
        if(!views_initialized||note==null||!note.hasEntry(index))
            return;
        CheckListNote.CheckListEntry entry = note.getEntry(index);
        //Log.d(TAG,String.format("Refreshing %s, to %s",index,entry));
        check_box.setChecked(entry.checked);
        text_view.setText(entry.text);
        edit_text.setText(entry.text);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.mode_check_list_entry, menu);
        if(edit_text!=null)
            edit_text.requestFocus();
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mcle_confirm:
                note.setEntry(index,new CheckListNote.CheckListEntry(
                        getEditTextString(),
                        check_box.isChecked()));
                mode.finish();
                return true;
            case R.id.mcle_delete:
                note.removeEntry(index);
                mode.finish();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        clearShowing();
    }

    public String getEditTextString() {
        return edit_text.getText().toString();
    }

    public int getIndex() {
        return index;
    }

    public void setEditText(String editText) {
        edit_text.setText(editText);
    }
}
