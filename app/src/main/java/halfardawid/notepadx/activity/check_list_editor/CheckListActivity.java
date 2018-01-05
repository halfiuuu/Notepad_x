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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.util.note.types.CheckListNote;

public class CheckListActivity
        extends GenericNoteActivity<CheckListNote>
        implements CheckListEntryCallbacks{

    public static final String EDITING_INDEX = "editing_index";
    public static final String EDITING_TEXT = "editing_text";
    List<CheckListEntryFragment> fragments;
    CheckListEntryFragment currentlyEditing=null;
    private View add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIntentData(CheckListNote.class);
        setContentView(R.layout.activity_checklist);
        add_button = findViewById(R.id.acl_add_button);
        refreshDataToView();
        if(note.getEntriesCount()==0)
            addEmptyEntry();
    }

    private void refreshNoteFields() {
        if(fragments==null){
            fragments=new LinkedList<>();
        }

        while(fragments.size()!=note.getEntriesCount()){
            if(fragments.size()>note.getEntriesCount()) {
                int index = fragments.size() - 1;
                CheckListEntryFragment fragment = fragments.get(index);
                fragments.remove(index);
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
            else {
                CheckListEntryFragment entry = new CheckListEntryFragment();
                entry.initialize(note, fragments.size(),this);
                getFragmentManager().beginTransaction().add(R.id.acl_main_list,entry).commit();
                fragments.add(entry);
            }
        }

        for(CheckListEntryFragment fragment:fragments)
            fragment.refresh();

        /*for(int index=0;index<fragments.size();index++){
            CheckListEntryFragment fragment=fragments.get(index);
            fragment.initialize(note, index,this);
            fragment.refresh();
        }*/
    }

    @Override
    protected String getTag() {
        return null;
    }

    @Override
    public void prepareForSave() {

    }

    @Override
    public void inherentRefresh() {
        refreshNoteFields();
    }

    @Override
    protected void loadSettings(Bundle s) {
        final Context c=this;
        final int index = s.getInt(EDITING_INDEX, -1);
        if(index<0)
            return;
        final String new_value=s.getString(EDITING_TEXT,"");
        runWithDelay(new Runnable() {
            @Override
            public void run() {
                CheckListEntryFragment fragment =fragments.get(index);
                fragment.setEditText(new_value);
                fragment.tryToShowEdit(c);
            }
        });
    }

    private void runWithDelay(final Runnable runnable){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { //This thing fixes a bug where actionbar becomes translucent, don't ask me...
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    //nah
                }
                runOnUiThread(runnable);
            }
        }).start();
    }

    @Override
    protected void saveSettings(Bundle s) {
        if(currentlyEditing!=null){
            s.putInt(EDITING_INDEX,currentlyEditing.getIndex());
            s.putString(EDITING_TEXT,currentlyEditing.getEditTextString());
        }
    }

    @Override
    protected void fragmentsPurge() {
        for(CheckListEntryFragment fragment:fragments){
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
        fragments.clear();
    }

    public void onAddClicked(View v){
        addEmptyEntry();
    }

    private void addEmptyEntry() {
        note.addEntry(new CheckListNote.CheckListEntry());
        refreshNoteFields();
        final Context context=this;
        runWithDelay(new Runnable() {
            @Override
            public void run() {
                fragments.get(fragments.size()-1).tryToShowEdit(context);
            }
        });
    }

    @Override
    protected boolean menuButtonPressed(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_note_editor_menu, menu);
        return true;
    }

    @Override
    public boolean alreadyShowingEdit() {
        return currentlyEditing!=null;
    }

    /**
     * Only place allowed to startActionMode, should be invoked from inside the fragment only
     * @param entry the fragment invoking
     * @see CheckListEntryFragment
     */
    @Override
    public void setShowingEdit(CheckListEntryFragment entry) {
        currentlyEditing=entry;
        add_button.setEnabled(false);
        startActionMode(entry);
    }

    @Override
    public void clearShowing() {
        currentlyEditing=null;
        clearFocus();
        add_button.setEnabled(true);
        refreshNoteFields();
    }

    private void clearFocus() {
        View currentFocus = getCurrentFocus();
        if(currentFocus!=null)
            currentFocus.clearFocus();
    }
}
