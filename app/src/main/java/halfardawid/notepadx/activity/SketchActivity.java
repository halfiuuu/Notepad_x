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
import halfardawid.notepadx.util.note.types.SketchNote;
import halfardawid.notepadx.util.note.types.TextNote;

public class SketchActivity extends GenericNoteActivity<SketchNote> {
    public static final String TAG="SKETCH_EDITOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);
        loadIntentData(SketchNote.class);
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

    }
}
