package halfardawid.notepadx.activity.text_editor;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.util.note.types.TextNote;

public final class TextNoteActivity extends GenericNoteActivity<TextNote> {

    public final String TAG="TEXTNOTE_ACTIVITY";

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        bindElements();
        loadIntentData(TextNote.class);
        refreshDataToView();
    }

    private void bindElements(){
        editText=(EditText)findViewById(R.id.atn_note_editor);
    }

    public void inherentRefresh(){//Do not super anything, don't even dare
        if(editText==null){
            return;
        }
        editText.setText(note.getText());
        setTitle(note.getTitle());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_note_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!handleGenericTasks(item))return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    public void prepareForSave() {
        note.setText(editText.getText().toString());
    }
}
