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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.TextNote;

public final class TextNoteActivity extends AppCompatActivity {

    public final String TAG="TEXTNOTE_ACTIVITY";

    private TextNote note;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        bindElements();
        loadIntentData();
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

    private boolean changeTitleDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(R.string.input_new_title);
        b.setTitle(R.string.change_title);
        final EditText et=new EditText(this);
        et.setText(getTitle());
        b.setView(et);
        b.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeTitle(et.getText().toString());
                dialog.dismiss();
            }
        });
        b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });


        AlertDialog dialog = b.create();

        dialog.show();

        return true;
    }

    private void changeTitle(String s) {
        note.setTitle(s);
        setTitle(s);
    }

    private void loadIntentData() {
        Intent intent=getIntent();
        try {
            note= intent.hasExtra(Note.UUID_EXTRA)? (TextNote) Note.loadNote(getApplicationContext(), intent.getStringExtra(Note.UUID_EXTRA)) :new TextNote();
        } catch (FileNotFoundException |NoSuchNoteTypeException |JSONException |ClassCastException e) {
            note=new TextNote();
            Log.wtf(TAG,"Loading note went terribly wrong",e);
            Toast.makeText(getApplicationContext(), R.string.loading_went_wrong,Toast.LENGTH_LONG);
        }
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

    private void saveNote() {
        note.setText(editText.getText().toString());
        try {
            note.saveToFile(this);
            Toast.makeText(this,R.string.saved,Toast.LENGTH_LONG);
        } catch (IOException|JSONException e){
            Log.wtf(TAG,"saving went terribad...?",e);
            Toast.makeText(this,R.string.saving_went_bad,Toast.LENGTH_LONG);
        }
    }
}
