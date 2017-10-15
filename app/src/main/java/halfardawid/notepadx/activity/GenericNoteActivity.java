package halfardawid.notepadx.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.types.SketchNote;

abstract public class GenericNoteActivity<T extends Note> extends AppCompatActivity {
    public T note;
    protected void loadIntentData(Class<T> ref) {
        Intent intent=getIntent();
        try {
            note=intent.hasExtra(Note.UUID_EXTRA)? (T) Note.loadNote(getApplicationContext(), intent.getStringExtra(Note.UUID_EXTRA)) :ref.newInstance();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.loading_went_wrong,Toast.LENGTH_LONG);
            try {
                note=ref.newInstance();
            } catch (Exception e1) {
                Log.wtf(getTag(),"What the bloody hell...?",e);
                finish();
                return;
            }
            Log.wtf(getTag(),"Loading note went terribly wrong",e);
        }
    }
    abstract protected String getTag();

    protected boolean changeTitleDialog(){
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

    protected void changeTitle(String s) {
        note.setTitle(s);
        setTitle(s);
    }

    abstract protected void prepareForSave();

    public void saveNote() {
        prepareForSave();
        try {
            note.saveToFile(this);
            Toast.makeText(this,R.string.saved,Toast.LENGTH_LONG);
        } catch (IOException |JSONException e){
            Log.wtf(getTag(),"saving went terribad...?",e);
            Toast.makeText(this,R.string.saving_went_bad,Toast.LENGTH_LONG);
        }
    }
}
