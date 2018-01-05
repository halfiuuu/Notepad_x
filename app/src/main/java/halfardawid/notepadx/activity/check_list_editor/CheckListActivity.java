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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.util.note.types.CheckListNote;

public class CheckListActivity extends GenericNoteActivity<CheckListNote> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        loadIntentData(CheckListNote.class);
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

    }

    @Override
    protected void loadSettings(Bundle s) {

    }

    @Override
    protected void saveSettings(Bundle s) {

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
}
