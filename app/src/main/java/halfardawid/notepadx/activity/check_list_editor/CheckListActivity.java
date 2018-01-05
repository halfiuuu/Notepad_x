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

import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.util.note.types.CheckListNote;

public class CheckListActivity
        extends GenericNoteActivity<CheckListNote>
        implements CheckListEntryCallbacks{

    List<CheckListEntry> fragments;
    CheckListEntry currentlyEditing=null;
    private View add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments=new ArrayList<>();
        loadIntentData(CheckListNote.class);
        setContentView(R.layout.activity_checklist);
        add_button = findViewById(R.id.acl_add_button);

        //don't mind it
        if(note.getEntriesCount()==0) {
            note.addEntry("Ayyy lmao");
            note.addEntry("Jeff plz buff road hog");
            note.addEntry("Gimme dat leg boi");
        }
        refreshDataToView();
    }

    private void refreshNoteFields() {
        while(fragments.size()!=note.getEntriesCount()){
            if(fragments.size()>note.getEntriesCount()) {
                int index = fragments.size() - 1;
                CheckListEntry fragment = fragments.get(index);
                fragments.remove(index);
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
            else {
                CheckListEntry entry = new CheckListEntry();
                entry.initialize(note, fragments.size(),this);
                getFragmentManager().beginTransaction().add(R.id.acl_main_list,entry).commit();
                fragments.add(entry);
            }
        }
        for(CheckListEntry fragment:fragments)
            fragment.refresh();
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

    }

    @Override
    protected void saveSettings(Bundle s) {

    }

    public void onAddClicked(View v){
        note.addEntry("");
        refreshNoteFields();
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

    @Override
    public void setShowingEdit(CheckListEntry entry) {
        currentlyEditing=entry;
        add_button.setEnabled(false);
        startActionMode(entry);
    }

    @Override
    public void clearShowing() {
        currentlyEditing=null;
        add_button.setEnabled(true);
    }
}
