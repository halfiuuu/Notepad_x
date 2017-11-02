package halfardawid.notepadx.activity.generic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.layouts.colorpicker.ColorPickReaction;
import halfardawid.notepadx.activity.generic.layouts.colorpicker.ColorPickerGrid;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;

abstract public class GenericNoteActivity<T extends Note> extends AppCompatActivity implements ColorPickReaction {
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
                finish();
                return;
            }
            Log.wtf(getTag(),"Loading note went terribly wrong",e);
        }
    }
    abstract protected String getTag();

    protected void changeTitleDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(R.string.input_new_title);
        //b.setTitle(R.string.change_title); Redundant...
        View v=getLayoutInflater().inflate(R.layout.activity_title_change,null);
        final EditText et=((EditText)v.findViewById(R.id.atch_title));
        et.setText(getTitle());
        b.setView(v);
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
        b.show();
    }

    protected void changeTitle(String s) {
        note.setTitle(s);
        setTitle(s);
    }

    abstract public void prepareForSave();

    @Override
    public final boolean onOptionsItemSelected(MenuItem item){
        if(!menuButtonPressed(item))
            switch (item.getItemId()) {
                case android.R.id.home:
                    onQuitSaveAsk();
                    break;
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
                case R.id.gnam_change_color:
                    changeColor();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        return true;
    }

    private void onQuitSaveAsk() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(R.string.any_unsaved_changes_will_be_discarded);
        b.setTitle(R.string.are_you_sure_quit);
        b.setPositiveButton(R.string.i_m_sure_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveAndQuit();
            }
        });
        b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        b.show();
    }

    protected void changeColor(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.pick_note_background_color);
        View v=getLayoutInflater().inflate(R.layout.colorpicker_list,null);
        b.setView(v);
        b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        Dialog d=b.show();
        ((ColorPickerGrid)v.findViewById(R.id.cpl_grid)).setClickListener(d,this);
    }


    private void saveAndQuit() {
        saveNote();
        finish();
    }

    protected void deleteNote() {
        note.deleteFile(this);
        finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK,new Intent());
        super.finish();
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
        saveSettings(s);
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
        loadSettings(s);
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

    public void refreshDataToView(){
        if(note==null){
            Log.wtf(TAG,"called refresh with null element, good job dummy...");
            return;
        }
        refreshColors();
        setTitle(note.getTitle());
        inherentRefresh();
    }

    private void refreshColors() {
        note.applyColors(this);
    }

    public abstract void inherentRefresh();
    protected abstract void loadSettings(Bundle s);
    protected abstract void saveSettings(Bundle s);
    protected abstract boolean menuButtonPressed(MenuItem item);

    @Override
    public void applyColorPick(int id){
        note.setColor(this,id);
        refreshColors();
    }
}
