package halfardawid.notepadx.activity.generic;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;

abstract public class GenericNoteActivity<T extends Note> extends AppCompatActivity {
    private static final String TAG = "GENERIC_NOTE";
    public static final String NOTE_JSON_DATA = "NOTE_JSON_DATA";
    public static final String NOTE_UUID = "NOTE_UUID";
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
                quit();
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

    abstract public void refreshDataToView();
    abstract public void prepareForSave();

    protected boolean handleGenericTasks(MenuItem item){
        switch (item.getItemId()) {
            case R.id.gnam_change_title:
                changeTitleDialog();
                break;
            case R.id.gnam_delete:
                deleteNote();
                break;
            case R.id.gnem_save:
                saveNote();
                break;
            case R.id.gnam_save_exit:
                saveAndQuit();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        quit();
    }


    private void saveAndQuit() {
        saveNote();
        quit();
    }

    protected void deleteNote() {
        note.deleteFile(this);
        quit();
    }

    private void quit() {
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle s) {
        super.onSaveInstanceState(s);
        prepareForSave();
        try {
            s.putString(NOTE_JSON_DATA,note.getParsedFileData());
        } catch (JSONException e) {
            Log.wtf(TAG,"Okey, json parsing blew up on saving...",e);
        }
        s.putString(NOTE_UUID,note.getUUID());
    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        super.onRestoreInstanceState(s);
        try {
            String nuuid=s.getString(NOTE_UUID);
            UUID uuid=(nuuid!=null)?UUID.fromString(s.getString(NOTE_UUID)):null;
            note=(T)Note.getNote(new JSONObject(s.getString(NOTE_JSON_DATA)), uuid);
        } catch (JSONException|NoSuchNoteTypeException e) {
            Log.wtf(TAG,"Okey, json parsing blew up on loading...",e);
        }
        refreshDataToView();
    }

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
