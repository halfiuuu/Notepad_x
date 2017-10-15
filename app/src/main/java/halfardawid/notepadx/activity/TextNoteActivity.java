package halfardawid.notepadx.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;
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

    private void refreshDataToView(){
        if(editText==null){
            Log.wtf(TAG,"called refresh with null element, good job dummy...");
            return;
        }
        editText.setText(note.getText());
        setTitle(note.getTitle());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.textnoteactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tna_change_title:
                return changeTitleDialog();
            case R.id.tna_delete:
                note.deleteFile(this);
                this.finish();
                return false;
            case R.id.tna_save:
                saveNote();
                return false;
            case R.id.tna_save_exit:
                saveNote();
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected void prepareForSave() {
        note.setText(editText.getText().toString());
    }
}
