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

package halfardawid.notepadx.activity.generic;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileNotFoundException;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;

public class WidgetEditorOpen extends IntentService {

    public WidgetEditorOpen() {
        super("WidgetEditorOpen");
    }

    @Override
    protected void onHandleIntent(Intent income) {
        Toast.makeText(this, "yey", Toast.LENGTH_SHORT).show();
        if(income==null)return;
        Note note;
        try {
            note = Note.loadNote(this,income.getStringExtra(Note.UUID_EXTRA));
        } catch (Exception e) {
            Log.wtf("WidgetService","failed",e);
            Toast.makeText(this, R.string.sorry_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = note.getEditIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
